import React, { useState, useEffect } from 'react';
import DotGame from './DotGame';

function ActiveGames() {
    const [dataState, setDataState] = useState([]);
    const [selectedGame, setSelectedGame] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await fetch('http://localhost:8083/gamemanagement/activeGames');
                const jsonData = await response.json();
                console.log("got response: ", jsonData);
                setDataState(jsonData);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };
        fetchData();

    }, []);

    const handleStartGame = (gameId) => {
        console.log('starting game with projected hits: ', gameId.projectedHits)
        let objectToSend = {...gameId, username: gameId.team1, projectedHits: gameId.projectedHits}
        setSelectedGame(objectToSend);
        const socket = new WebSocket('ws://localhost:3001/start');

        socket.addEventListener('open', () => {
            let gameEventObj = { ...objectToSend, state: "STARTED" }
            socket.send(JSON.stringify(gameEventObj));
            console.log('Game Published');
        });

        socket.addEventListener('error', (error) => {
            console.error('WebSocket error:', error);
        });

    };

    const list = () => {
        if (dataState && dataState.length > 0) {
            return (
                dataState.map((item) => (
                    <li className={"gameDetails"} key={item.gameId}>
                        <p>Game ID: {item.gameId}</p>
                        <p>Description: {item.description}</p>
                        <p>Projected hits: {item.projectedHits}</p>
                        <p>Team 1: {item.team1}</p>
                        <p>Team 2: {item.team2}</p>
                        <p>Game State: {item.gameState}</p>
                        <p>Team 1 Wins: {item.team1Wins}</p>
                        <button onClick={() => handleStartGame(item)}>Start Game</button>
                    </li>
                ))
            )
        }
    }

    return (
        <div>
            <h1 id={"header"}>Game List</h1>
            <ul id={"Game Information"}>
                {list()}
            </ul>

            {selectedGame && <DotGame gameEvent={{...selectedGame, username: selectedGame.team1}} />}
        </div>
    );
}

export default ActiveGames;