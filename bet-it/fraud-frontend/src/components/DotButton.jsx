import React, { useState } from 'react';

const DotButton = () => {
    const [buttonClicked, setButtonClicked] = useState(false);

    const handleClick = () => {

    };

    return (
        <div>
            <button onClick={handleClick} disabled={buttonClicked}>
                Send Dot Object
            </button>
            {buttonClicked && <p>Dot object sent via WebSocket</p>}
        </div>
    );
};

export default DotButton;
