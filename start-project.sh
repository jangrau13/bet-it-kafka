#!/bin/bash


docker-compose -f bet-it/docker-compose.yml up kafdrop --build
docker-compose -f bet-it/docker-compose.yml up ksqldb-server --build


# Check if game-master container is running
    if [[ $(docker-compose -f docker-compose.yml ps -q kafka) ]]; then
        echo "Kafka container started successfully."
    else
        echo "Failed to start game-master container. Attempting restart..."
        docker-compose -f docker-compose.yml restart kafka
        sleep 10

        # Check if game-master container restarted successfully
        if [[ $(docker-compose -f docker-compose.yml ps -q kafka) ]]; then
            echo "game-master container restarted successfully."
        else
            echo "Failed to restart game-master container. Exiting..."
            exit 1
        fi
    fi

timeout /t 60 > nul
docker-compose -f bet-it/docker-compose.yml up game-master --build
docker-compose -f docker-compose.yml up zeebe-addon --build
docker-compose -f docker-compose.yml up fraud-frontend --build
docker-compose -f docker-compose.yml up fraud-backend --build
