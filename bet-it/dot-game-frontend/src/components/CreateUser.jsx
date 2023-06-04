import { useState } from 'react';
import { v4 as uuidv4 } from 'uuid';

function CreateUser() {
  const [playerName, setPlayerName] = useState('');
  const [uuid, setUUID] = useState('');

  const handlePlayernameChange = (event) => {
    setPlayerName(event.target.value);
    const generatedUUID = uuidv4();
    setUUID(generatedUUID);
  };

  const handleButtonClick = () => {
    // Handle button click event here
    if (playerName && playerName.length > 0 && uuid && uuid.length > 0) {
        const socket = new WebSocket('ws://localhost:3001/newUser');
        const userObject = {
            playerName,
            playerId : uuid
        }

        socket.addEventListener('open', () => {
            socket.send(JSON.stringify(userObject));
            console.log('Player Published', JSON.stringify(userObject));
        });

        socket.addEventListener('error', (error) => {
            console.error('WebSocket error:', error);
        });
    }
  };

  return (
    <div>
      <label>
        Playername:
        <input type="text" value={playerName} onChange={handlePlayernameChange} />
      </label>
      <br />
      <button onClick={handleButtonClick}>Submit</button>
    </div>
  );
}

export default CreateUser;