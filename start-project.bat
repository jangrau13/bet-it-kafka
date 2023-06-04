@echo off

docker-compose -f bet-it/docker-compose.yml up kafdrop --build -d
docker-compose -f bet-it/docker-compose.yml up ksqldb-server --build -d

echo compiling Microservices

cd bet-it
call mvn clean install -DskipTests
cd ..

echo compiling done

docker-compose -f bet-it/docker-compose.yml up zeebe-addon --build -d
docker-compose -f bet-it/docker-compose.yml up game-master --build -d
docker-compose -f bet-it/docker-compose.yml up dot-game-frontend --build -d
docker-compose -f bet-it/docker-compose.yml up dot-game-backend --build -d
docker-compose -f bet-it/docker-compose.yml up bet-it-platform --build -d
docker-compose -f bet-it/docker-compose.yml up bank --build -d
docker-compose -f bet-it/docker-compose.yml up fraud-detector --build -d
docker-compose -f bet-it/docker-compose.yml up control-center --build -d
docker-compose -f bet-it/docker-compose.yml up api2kafka --build -d



REM Restart zeebe-addon service
docker-compose -f bet-it/docker-compose.yml restart zeebe-addon


echo done
