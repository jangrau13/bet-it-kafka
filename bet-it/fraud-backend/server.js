const express = require('express');
const { Kafka } = require('kafkajs');
const {Server} = require('ws');
const topics = require('./topics.json');
const app = express();

const server = app.listen(3001, () => {
    console.log('Backend server is listening on port 3001');
});

const kafka = new Kafka({
    clientId: 'backend-consumer',
    brokers: ['kafka:9092'],
});
const wss = new Server({ server });
console.log("ADRESS", wss.address())
const consumer = kafka.consumer({ groupId: 'fraud-backend2' });

all_messages = [];
wss.on('connection', (ws) => {
    console.log('New client connected!');
    all_messages.forEach((message) => {
        ws.send(message)
    })
    ws.on('close', () => console.log('Client has disconnected!'));
});


async function run() {
    await consumer.connect();
    for (const topic of topics.topics) {
        console.log("Subscribing to", topic);
        await consumer.subscribe({ topic, fromBeginning: true });
    }

    await consumer.run({
        eachMessage: async ({ topic, partition, message }) => {
            const value = message.value.toString();
            const value2 = JSON.parse(value);
            console.log(value2)
            console.log(`Received message from topic ${topic}, partition ${partition}: ${value}`);
            const sending = JSON.stringify({topic: topic, message: value2})
            console.log("SENDING", sending)
            all_messages.push(sending)
            wss.clients.forEach((client) => {
                    client.send(sending);
            });
        },
    });
}

run().catch((error) => {
    console.error('Error consuming messages:', error);
});