import React, { useState, useEffect } from 'react';
import { v4 as uuidv4 } from 'uuid';
import DotGame from './DotGame';

function CreateGame() {
    const [username, setUsername] = useState('placeholder');
    const [hits, setHits] = useState(0);
    const [gameName, setGameName] = useState('');
    const [startGame, setStartGame] = useState(false);
    const [uuid, setUUID] = useState('');
    const [showStart, setShowStart] = useState(false);
    const [gameEvent, setGameEvent] = useState({});
    const [selectedPlayer, setSelectedPlayer] = useState(null);
    const [players, setPlayers] = useState(null);


    useEffect(() => {
        // Fetch players data from the API
        const fetchPlayers = async () => {
            try {
                const response = await fetch('http://localhost:8083/gamemanagement/players');
                const data = await response.json();
                setPlayers(uniquePlayers(data));
            } catch (error) {
                console.error('Error fetching players:', error);
            }
        };

        fetchPlayers();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const uniquePlayers = (players) => players.filter(
        (player, index, self) =>
            index === self.findIndex((p) => p.playerName === player.playerName)
    );

    const handlePlayerSelect = (playerId) => {
        setSelectedPlayer(playerId);
        setUsername(playerId);
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
                    description: gameName + ": " + username + "; projected hits: " + hits,
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
                <div>
                    <h2>Player List</h2>
                    <ul>
                        {players && Object.entries(players).map(([playerId, playerData]) => (
                            <li key={playerData.playerId}>
                                <button onClick={() => handlePlayerSelect(playerData.playerId)}>
                                    {playerData.playerName}
                                </button>
                            </li>
                        ))}
                    </ul>
                    {selectedPlayer && (
                        <div>
                            <h3>Selected Player</h3>
                            <p>Player ID: {players.find((player) => player.playerId === selectedPlayer).playerId}</p>
                            <p>Name: {players.find((player) => player.playerId === selectedPlayer).playerName}</p>
                            <p>Spawns: {players.find((player) => player.playerId === selectedPlayer).spawns}</p>
                            <p>Hits: {players.find((player) => player.playerId === selectedPlayer).hits}</p>
                            <p>Misses: {players.find((player) => player.playerId === selectedPlayer).misses}</p>
                            <p>Friendly Fire: {players.find((player) => player.playerId === selectedPlayer).friendlyFire}</p>
                            <p>Hits per Game: {players.find((player) => player.playerId === selectedPlayer).hitsPerGame}</p>
                            <p>Accuracy: {players.find((player) => player.playerId === selectedPlayer).accuracy}</p>
                            <p>Friendly Fire Rate: {players.find((player) => player.playerId === selectedPlayer).friendlyFireRate}</p>
                            <p>Games: {players.find((player) => player.playerId === selectedPlayer).games}</p>
                        </div>
                    )}

                    <label id="hits">
                        Hits:
                        <input type="number" value={hits} onChange={handleHitsChange} />
                    </label>
                    <br />
                    <label id="gameName">
                        Game Name:
                        <input type="text" value={gameName} onChange={handleGameNameChange} />
                    </label>
                    <br />
                    <button id="handlePublish" onClick={handleButtonClickPublish}>
                        Publish Game
                    </button>
                    {startGameButton()}
                </div>
            )}
        </div>
    );
}

export default CreateGame;
