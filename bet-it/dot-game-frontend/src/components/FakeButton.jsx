import React, { useState } from 'react';

const FakeButton = () => {
    const [buttonClicked, setButtonClicked] = useState(false);

    const handleClick = () => {
        const dotObject = {
            size: 20,
            color: 'red',
            timestamp: new Date().toISOString(),
            username: 'john_doe'
        };

        const socket = new WebSocket('ws://localhost:3001/');

        socket.addEventListener('open', () => {
            socket.send(JSON.stringify(dotObject));
            setButtonClicked(true);
            console.log('Dot object sent via WebSocket!');
        });

        socket.addEventListener('error', (error) => {
            console.error('WebSocket error:', error);
        });
    };

    return (
        <div>
            <button onClick={handleClick} disabled={buttonClicked}>
                Send Fake Dot Object
            </button>
            {buttonClicked && <p>Dot object sent via WebSocket</p>}
        </div>
    );
};

export default FakeButton;
