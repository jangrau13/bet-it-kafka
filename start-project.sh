#!/bin/bash

docker-compose -f bet-it/docker-compose.yml up kafdrop --build -d
docker-compose -f bet-it/docker-compose.yml up ksqldb-server --build -d

docker-compose -f bet-it/docker-compose.yml up control-center --build -d


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


echo "done"
