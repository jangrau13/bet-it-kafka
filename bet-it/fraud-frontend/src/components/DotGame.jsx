import React, { useState, useEffect, useRef } from "react";

const GIVEN_ROUNDS = 20;
const WIDTH = 1500;
const LENGTH = 1000;
const DIFFICULTY = 700;
const DotGame = (props) => {
    const canvasRef = useRef(null);
    const [dot, setDot] = useState(null);
    const [friendlyFireDot, setFriendlyFireDot] = useState(null);
    const [rounds, setRounds] = useState(0);
    const [gameEnded, setGameEnded] = useState(false);



    const sendCreate = () => {
            if (dot && dot.visible === true && dot.size > 0) {
                let dotObject = {
                    timestamp: new Date().toISOString(),
                    size: dot.size,
                    color: dot.color,
                    username: props.gameEvent.username,
                    gameId: props.gameEvent.gameId
                };

                const socket = new WebSocket('ws://localhost:3001/spawn');

                socket.addEventListener('open', () => {
                    socket.send(JSON.stringify(dotObject));
                });

                socket.addEventListener('error', (error) => {
                    console.error('WebSocket error:', error);
                });
            }
    };

    // Generate dot
    const generateDot = () => {
            const newDot = {
                x: Math.random() * WIDTH,
                y: Math.random() * LENGTH,
                color: "black",
                size: Math.random() * 50 + 10,
                visible: true,
            };

        const friendlyFireDot = {
            x: Math.random() * WIDTH,
            y: Math.random() * LENGTH,
            color: "red",
            size: Math.random() * 50 + 10,
            visible: true,
        };

            setDot(newDot);
            setFriendlyFireDot(friendlyFireDot);
            sendCreate();

            setTimeout(() => {
                setDot((prevDot) => ({...prevDot, visible: false}));
                setFriendlyFireDot((prevDot) => ({...prevDot, visible: false}));
                setRounds((prevRounds) => prevRounds + 1);
            }, DIFFICULTY);
    };

    // Handle good hit
    const handleClick = () => {
        let dotObject = {
            timestamp: new Date().toISOString(),
            size: dot.size,
            color: dot.color,
            username: props.gameEvent.username,
            gameId: props.gameEvent.gameId
        };

        const socket = new WebSocket('ws://localhost:3001/hit');

        socket.addEventListener('open', () => {
            socket.send(JSON.stringify(dotObject));
            console.log('Dot object sent via WebSocket!');
        });

        socket.addEventListener('error', (error) => {
            console.error('WebSocket error:', error);
        });
    };

    // Handle good hit
    const handleFriendlyFireClick = () => {
        let friendlyFireDotObject = {
            timestamp: new Date().toISOString(),
            size: friendlyFireDot.size,
            color: friendlyFireDot.color,
            username: props.gameEvent.username,
            gameId: props.gameEvent.gameId
        };

        const socket = new WebSocket('ws://localhost:3001/friendlyfire');

        socket.addEventListener('open', () => {
            socket.send(JSON.stringify(friendlyFireDotObject));
        });

        socket.addEventListener('error', (error) => {
            console.error('WebSocket error:', error);
        });
    };

    const handleClickMiss = () => {
        let dotObject = {
            timestamp: new Date().toISOString(),
            size: dot.size,
            color: dot.color,
            username: props.gameEvent.username,
            gameId: props.gameEvent.gameId
        };

        const socket = new WebSocket('ws://localhost:3001/miss');

        socket.addEventListener('open', () => {
            socket.send(JSON.stringify(dotObject));
        });

        socket.addEventListener('error', (error) => {
            console.error('WebSocket error:', error);
        });
    };


    useEffect(() => {
            // Generate new dot every second until 100 rounds
            const interval = setInterval(() => {
                if (rounds <= GIVEN_ROUNDS) {
                    generateDot();
                } else {
                    clearInterval(interval);
                    setGameEnded(true);
                    const gameObject = {
                        ...props.gameEvent,
                        state: "ENDED"
                    }

                    const socket = new WebSocket('ws://localhost:3001/end');

                    socket.addEventListener('open', () => {
                        socket.send(JSON.stringify(gameObject));
                    });

                    socket.addEventListener('error', (error) => {
                        console.error('WebSocket error:', error);
                    });
                }
            }, DIFFICULTY);

            return () => clearInterval(interval);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [rounds]);


    useEffect(() => {
        // Update canvas
        const canvas = canvasRef.current;
        if(canvas !== null){
            const context = canvas.getContext("2d");
            context.clearRect(0, 0, canvas.width, canvas.height);

            if (dot && dot.visible) {
                context.beginPath();
                context.arc(dot.x, dot.y, dot.size, 0, 2 * Math.PI);
                context.fillStyle = dot.color;
                context.fill();
            }
            if (friendlyFireDot && friendlyFireDot.visible) {
                context.beginPath();
                context.arc(friendlyFireDot.x, friendlyFireDot.y, friendlyFireDot.size, 0, 2 * Math.PI);
                context.fillStyle = friendlyFireDot.color;
                context.fill();
            }

        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [dot]);

    const canvasScreen = () =>{
        if(!gameEnded){
            return (
                <canvas
                    id={"canvas"}
                    ref={canvasRef}
                    style={{ background: "lightgray" }}
                    width={WIDTH}
                    height={LENGTH}
                    onClick={(e) => {
                        //catching good hit/miss
                        if (dot && dot.visible) {
                            const { x, y, size } = dot;
                            const rect = e.target.getBoundingClientRect();
                            const offsetX = e.clientX - rect.left;
                            const offsetY = e.clientY - rect.top;

                            if (
                                Math.abs(x - offsetX) < size &&
                                Math.abs(y - offsetY) < size
                            ) {
                                handleClick();
                                setDot((prevDot) => ({ ...prevDot, visible: false }));
                            }else {
                                // Clicked outside the dot
                                handleClickMiss();
                            }
                        }
                        // catch friendly fire
                        if (friendlyFireDot && friendlyFireDot.visible) {
                            const { x, y, size } = friendlyFireDot;
                            const rect = e.target.getBoundingClientRect();
                            const offsetX = e.clientX - rect.left;
                            const offsetY = e.clientY - rect.top;

                            if (
                                Math.abs(x - offsetX) < size &&
                                Math.abs(y - offsetY) < size
                            ) {
                                handleFriendlyFireClick();
                                setFriendlyFireDot((prevDot) => ({ ...prevDot, visible: false }));
                            }
                        }
                    }}
                />
            )
        }
    }

    return (
        <div>
            {canvasScreen()}
        </div>
    );
};

export default DotGame;
