import React from 'react';
import './EventList.css';

function EventList({ events }) {
    return (
        <div className="event-list">
            <table>
                <thead>
                <tr>
                    {Object.keys(events[0]).map((property) => (
                        <th key={property}>{property}</th>
                    ))}
                </tr>
                </thead>
                <tbody>
                {events.map((event, index) => (
                    <tr key={index}>
                        {Object.values(event).map((value, i) => (
                            <td key={i}>{value}</td>
                        ))}
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default EventList;
