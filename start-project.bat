@echo off

docker-compose -f bet-it/docker-compose.yml up kafdrop --build -d
docker-compose -f bet-it/docker-compose.yml up ksqldb-server --build -d

REM Check if kafka container is running
docker-compose -f bet-it/docker-compose.yml ps kafka | findstr /C:"Up" > nul
if %errorlevel% equ 0 (
    echo Kafka container started successfully.
) else (
    echo Failed to start kafka container. Attempting restart...
    docker-compose -f docker-compose.yml restart kafka -q
    timeout /t 10 > nul

    REM Check if kafka container restarted successfully
    docker-compose -f docker-compose.yml ps kafka -d | findstr /C:"Up" > nul
    if %errorlevel% equ 0 (
        echo Kafka container restarted successfully.
    ) else (
        echo Failed to restart kafka container. Exiting...
        exit /b 1
    )
)

echo compiling Microservices

cd bet-it
call mvn clean install -DskipTests
cd ..

if %errorlevel% neq 0 (
    echo Maven clean install failed. Exiting...
    pause
    exit /b 1
)


echo compiling done

docker-compose -f bet-it/docker-compose.yml up zeebe-addon --build -d
docker-compose -f bet-it/docker-compose.yml up game-master --build -d
docker-compose -f bet-it/docker-compose.yml up dot-game-frontend --build -d
docker-compose -f bet-it/docker-compose.yml up dot-game-backend --build -d
docker-compose -f bet-it/docker-compose.yml up bet-it-platform --build -d
docker-compose -f bet-it/docker-compose.yml up bank --build -d


call :check_container game-master
call :check_container zeebe-addon
call :check_container dot-game-frontend
call :check_container dot-game-backend
call :check_container bet-it-platform
call :check_container bank

REM Restart zeebe-addon service
docker-compose -f bet-it/docker-compose.yml restart zeebe-addon


call :check_logs dot-game-frontend "Compiled successfully!"
call :check_logs dot-game-backend "Server started"
call :check_logs zeebe-addon "Tomcat started on port"
call :check_logs bank "Tomcat started on port"
call :check_logs bet-it-platform "Tomcat started on port"

echo done

GOTO :EOF
REM Function to check and retry starting a Docker container
:check_container
setlocal
set retries=0
:retry
docker-compose -f bet-it/docker-compose.yml ps %1 | findstr /C:"Up" > nul
if %errorlevel% neq 0 (
    echo %1 container not started. Retry attempt %retries%...
    set /a retries+=1
    if %retries% leq 3 (
        timeout /t 10 > nul
        goto retry
    ) else (
        echo Failed to start %1 container. Exiting...
        pause
        exit /b 1
    )
)
endlocal

REM Function to check if a string exists in the logs of a Docker container
:check_logs
docker-compose -f bet-it/docker-compose.yml logs %1 > tmp.txt
findstr /C:%2 tmp.txt > nul
if %errorlevel% equ 0 (
    echo %1 seems to have started successfully.
) else (
    echo %1 is not up yet, we have to wait a bit
    exit /b 1
)

:EOF
echo ...