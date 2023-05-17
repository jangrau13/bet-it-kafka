import React, { useState, useEffect } from 'react';
import EventList from './components/EventList';
import './App.css'

function App() {
  const [topics, setTopics] = useState([]);
  const [activeTopic, setActiveTopic] = useState(null);
  const [events, setEvents] = useState({});

  useEffect(() => {
    // WebSocket connection setup
    const socket = new WebSocket('ws://localhost:3001');

    socket.onopen = () => {
      console.log('Connected to the WebSocket server');
    };

    socket.onmessage = (event) => {
      const data = JSON.parse(event.data);
      const { topic, message } = data;

      // Update events object with new event
      setEvents((prevEvents) => {
        const updatedEvents = { ...prevEvents };
        if (!updatedEvents[topic]) {
          updatedEvents[topic] = [];
          // Add new topic to the list of topics
          setTopics((prevTopics) => [...prevTopics, topic]);
        }
        updatedEvents[topic].push(message);
        return updatedEvents;
      });
    };

    socket.onclose = () => {
      console.log('Disconnected from the WebSocket server');
    };

    // Clean up WebSocket connection
    return () => {
      socket.close();
    };
  }, []);

  const handleTabClick = (topic) => {
    setActiveTopic(topic);
  };

  return (
      <div className="container">
        <h1>Kafka Events</h1>

        <div className="tabs">
          {topics.map((topic) => (
              <div
                  key={topic}
                  className={`tab ${topic === activeTopic ? 'active' : ''}`}
                  onClick={() => handleTabClick(topic)}
              >
                {topic}
              </div>
          ))}
        </div>

        {activeTopic && (
            <EventList topic={activeTopic} events={events[activeTopic] || []} />
        )}
      </div>
  );
}

export default App;