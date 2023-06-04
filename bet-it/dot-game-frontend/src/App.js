import React from 'react';
import './App.css'
import CreateGame from "./components/CreateGame";
import ActiveGames from "./components/ActiveGames";
import CreateUser from "./components/CreateUser";

function App() {


  return (
      <div className="container">
        <h1 id={"overallTitle"}>Kafka Events</h1>
        <CreateGame/>
          <ActiveGames/>
          <CreateUser/>
      </div>
  );
}

export default App;