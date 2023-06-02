#!/bin/bash

docker-compose -f bet-it/docker-compose.yml up kafdrop --build -d
docker-compose -f bet-it/docker-compose.yml up ksqldb-server --build -d

# Check if kafka container is running
docker-compose -f bet-it/docker-compose.yml ps kafka | grep -q "Up"
if [ $? -eq 0 ]; then
    echo "Kafka container started successfully."
else
    echo "Failed to start kafka container. Attempting restart..."
    docker-compose -f docker-compose.yml restart kafka -q
    sleep 10

    # Check if kafka container restarted successfully
    docker-compose -f docker-compose.yml ps kafka -d | grep -q "Up"
    if [ $? -eq 0 ]; then
        echo "Kafka container restarted successfully."
    else
        echo "Failed to restart kafka container. Exiting..."
        exit 1
    fi
fi

echo "compiling Microservices"

cd bet-it
mvn clean install -DskipTests
cd ..

if [ $? -ne 0 ]; then
    echo "Maven clean install failed. Exiting..."
    exit 1
fi

echo "compiling done"

docker-compose -f bet-it/docker-compose.yml up zeebe-addon --build -d
docker-compose -f bet-it/docker-compose.yml up game-master --build -d
docker-compose -f bet-it/docker-compose.yml up dot-game-frontend --build -d
docker-compose -f bet-it/docker-compose.yml up dot-game-backend --build -d
docker-compose -f bet-it/docker-compose.yml up bet-it-platform --build -d
docker-compose -f bet-it/docker-compose.yml up bank --build -d
docker-compose -f bet-it/docker-compose.yml up fraud-detector --build -d

check_container() {
    retries=0
    while true; do
        docker-compose -f bet-it/docker-compose.yml ps "$1" | grep -q "Up"
        if [ $? -ne 0 ]; then
            retries=$((retries+1))
            echo "$1 container not started. Retry attempt $retries..."
            if [ $retries -le 3 ]; then
                sleep 10
            else
                echo "Failed to start $1 container. Exiting..."
                exit 1
            fi
        else
            break
        fi
    done
}

check_logs() {
    docker-compose -f bet-it/docker-compose.yml logs "$1" > tmp.txt
    grep -q "$2" tmp.txt
    if [ $? -eq 0 ]; then
        echo "$1 seems to have started successfully."
    else
        echo "$1 is not up yet, we have to wait a bit"
        exit 1
    fi
}

check_container game-master
check_container zeebe-addon
check_container dot-game-frontend
check_container dot-game-backend
check_container bet-it-platform
check_container bank
check_container fraud-detector

# Restart zeebe-addon service
docker-compose -f bet-it/docker-compose.yml restart zeebe-addon

check_logs dot-game-frontend "Compiled successfully!"
check_logs dot-game-backend "Server started"
check_logs zeebe-addon "Tomcat started on port"
check_logs bank "Tomcat started on port"
check_logs bet-it-platform "Tomcat started on port"
check_logs fraud-detector "Tomcat started on port"

echo "done"
