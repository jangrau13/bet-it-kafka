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
        setSelectedGame(gameId);
    };

    return (
        <div>
            <h1>Game List</h1>
            <ul>
                {dataState.map((item) => (
                    <li key={item.gameId}>
                        <p>Game ID: {item.gameId}</p>
                        <p>Team 1: {item.team1}</p>
                        <p>Team 2: {item.team2}</p>
                        <p>Game State: {item.gameState}</p>
                        <p>Team 1 Wins: {item.team1Wins}</p>
                        <button onClick={() => handleStartGame(item)}>Start Game</button>
                    </li>
                ))}
            </ul>

            {selectedGame && <DotGame gameEvent={selectedGame} />}
        </div>
    );
}

export default ActiveGames;