import React from 'react';
import './App.css'
import CreateGame from "./components/CreateGame";
import ActiveGames from "./components/ActiveGames";

function App() {


  return (
      <div className="container">
        <h1>Kafka Events</h1>
        <CreateGame/>
          <ActiveGames/>
      </div>
  );
}

export default App;