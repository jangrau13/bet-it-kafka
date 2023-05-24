const cors = require('cors');

const express = require('express')
const enableWs = require('express-ws')
const { Kafka } = require('kafkajs')

const kafka = new Kafka({
    clientId: 'backend-producer',
    brokers: ['kafka:9092'],
});

const app = express()
enableWs(app)

const producer = kafka.producer()

async function createTopic(admin, topic) {
    await admin.createTopics({
        topics: [
            {
                topic,
                numPartitions: 1,
                replicationFactor: 1,
            },
        ],
    });
}

async function initializeTopic() {
    try {
        await producer.connect();
        const admin = kafka.admin();
        await admin.connect();

        // const topics = ['game.dot.started', 'game.dot.hit', 'game.dot.miss', 'game.dot.friendlyfire', 'game.player', 'camunda.game.started', 'game.dot.spawn', 'game.published'];
        //
        // await topics.forEach(topic => {
        //     createTopic(admin, topic)
        // })


        // Create the topic with the desired configuration
        await admin.createTopics({
            topics: [
                {
                    topic: 'game.dot.started',
                    numPartitions: 1,
                    replicationFactor: 1,
                },
                {
                    topic: 'game.dot.hit',
                    numPartitions: 1,
                    replicationFactor: 1,
                },
                {
                    topic: 'game.dot.miss',
                    numPartitions: 1,
                    replicationFactor: 1,
                },
                {
                    topic: 'game.dot.friendlyfire',
                    numPartitions: 1,
                    replicationFactor: 1,
                },
                {
                    topic: 'game.player',
                    numPartitions: 1,
                    replicationFactor: 1,
                },
                {
                    topic: 'game.dot.spawn',
                    numPartitions: 1,
                    replicationFactor: 1,
                },
                {
                    topic: 'camunda.game.started',
                    numPartitions: 1,
                    replicationFactor: 1,
                },
                {
                    topic: 'game.published',
                    numPartitions: 1,
                    replicationFactor: 1,
                },
                {
                    topic: 'camunda.game.ended',
                    numPartitions: 1,
                    replicationFactor: 1,
                },
                {
                    topic: 'game.dot.ended',
                    numPartitions: 1,
                    replicationFactor: 1,
                }
            ],
        });

        await admin.disconnect();
    } catch (error) {
        console.error('Error initializing topic:', error);
        process.exit(1);
    }
}


const testID = '';

//if a dot has been hit
app.ws('/hit', (ws, req) => {
    handleWebSocketRequest(ws, 'game.dot.hit' + testID);
})

app.ws('/newUser', (ws, req) => {
    ws.on('message', msg => {
        dotObject = JSON.parse(msg);
        key = dotObject.playerId;
        producer.send({
            topic: "game.player",
            messages: [
                {
                    key: key,
                    value: msg
                }
            ],
        })
    })

    ws.on('close', () => {
        console.log('WebSocket was closed')
    })
})


app.ws('/friendlyfire', (ws, req) => {
    handleWebSocketRequest(ws, 'game.dot.friendlyfire' + testID);
})

app.ws('/miss', (ws, req) => {
    handleWebSocketRequest(ws, 'game.dot.miss' + testID);
})

app.ws('/spawn', (ws, req) => {
    handleWebSocketRequest(ws, 'game.dot.spawn'+ testID);
})

app.ws('/publish', (ws, req) => {
    handleWebSocketRequest(ws, 'game.published'+ testID);
})

app.ws('/start', (ws, req) => {
    handleWebSocketRequest(ws, 'camunda.game.started'+ testID);
    handleWebSocketRequest(ws, 'game.dot.started'+ testID);
})

app.ws('/end', (ws, req) => {
    handleWebSocketRequest(ws, 'game.dot.ended' + testID);
})


function handleWebSocketRequest(ws, topic) {
    ws.on('message', msg => {
        dotObject = JSON.parse(msg);
        key = dotObject.gameId;
        producer.send({
            topic: topic,
            messages: [
                {
                    key: key,
                    value: msg
                }
            ],
        })
    })

    ws.on('close', () => {
        console.log('WebSocket was closed')
    })
}


app.use(cors()); // Enable CORS for all routes

async function startServer() {
    try {
        await producer.connect();
        await initializeTopic();
        // Start your server logic here
        app.listen(3001, () => {
            console.log('Server started on port 3001');
        });
    } catch (error) {
        console.error('Error starting server:', error);
        process.exit(1);
    }
}

startServer()

process.on("SIGINT", () => {
    server.close(() => {
        console.log("Server shut down");

        // Additional code to execute on server shutdown

        producer.disconnect()
    });
});