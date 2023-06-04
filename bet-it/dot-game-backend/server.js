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

app.listen(3001, () => {
    console.log("Server started");
    producer.connect()

    // Additional code to execute on server start up
});

process.on("SIGINT", () => {
    server.close(() => {
        console.log("Server shut down");

        // Additional code to execute on server shutdown

        producer.disconnect()
    });
});