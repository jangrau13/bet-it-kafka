import React, { useState } from 'react';
import { v4 as uuidv4 } from 'uuid';
import DotGame from './DotGame';

function CreateGame() {
    const [username, setUsername] = useState('');
    const [hits, setHits] = useState(0);
    const [gameName, setGameName] = useState('');
    const [startGame, setStartGame] = useState(false);
    const [uuid, setUUID] = useState('');
    const [showStart, setShowStart] = useState(false);
    const [gameEvent, setGameEvent] = useState({})

    const handleUsernameChange = (event) => {
        setUsername(event.target.value);
    };

    const handleHitsChange = (event) => {
        setHits(parseInt(event.target.value));
    };

    const handleGameNameChange = (event) => {
        setGameName(event.target.value);
    };

    const handleButtonClickStart = () => {
        if(username && username.length > 0 && uuid && uuid.length > 0 && showStart){
            const socket = new WebSocket('ws://localhost:3001/start');

            socket.addEventListener('open', () => {
                let gameEventObj = { ...gameEvent, state: "STARTED" }
                setGameEvent(gameEventObj)
                socket.send(JSON.stringify(gameEventObj));
                console.log('Game Published');
            });

            socket.addEventListener('error', (error) => {
                console.error('WebSocket error:', error);
            });
            setStartGame(true);
        }

    };

    const handleButtonClickPublish = () => {
        const generatedUUID = uuidv4();
        setUUID(generatedUUID);
        if(username && username.length > 0){
            const socket = new WebSocket('ws://localhost:3001/publish');

            socket.addEventListener('open', () => {
                let gameEventObj = {
                    gameId: generatedUUID,
                    username,
                    team1: username,
                    team2: "rest of the world",
                    description: gameName + ": " + username,
                    hits,
                    gameName,
                    gameType: "DOT",
                    state: "STARTED"
                }
                setGameEvent(gameEventObj)
                socket.send(JSON.stringify(gameEventObj));
                console.log('Game Published');
            });

            socket.addEventListener('error', (error) => {
                console.error('WebSocket error:', error);
            });
            setShowStart(true);
        }

    };

    const startGameButton = () =>{
        if(showStart){
            return (
                <button id={"handleStart"} onClick={handleButtonClickStart}>Start Game</button>
            )
        }
    }

    return (
        <div>
            {startGame ? (
                <DotGame gameEvent={gameEvent} />
            ) : (
                <>
                    <label id={"username"}>
                        Username:
                        <input type="text" value={username} onChange={handleUsernameChange} />
                    </label>
                    <br />
                    <label id={"hits"}>
                        Hits:
                        <input type="number" value={hits} onChange={handleHitsChange} />
                    </label>
                    <br />
                    <label id={"gameName"}>
                        Game Name:
                        <input type="text" value={gameName} onChange={handleGameNameChange} />
                    </label>
                    <br />
                    <button id={"handlePublish"} onClick={handleButtonClickPublish}>Publish Game</button>
                    {startGameButton()}
                </>
            )}
        </div>
    );
}

export default CreateGame;
