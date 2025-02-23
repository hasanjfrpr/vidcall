const { json } = require("body-parser");
const http = require("http");
const socketIo = require("socket.io");
const server = http.createServer(() => {});

server.listen(3000, () => {
    console.log("Server is running on port 3000");
});

const io = socketIo(server);

const users = [];

io.on('connection', (socket) => {
    console.log("A user connected");

    socket.on('message', (message) => {

      
      const user = findUser(message.name)
      const data = message

    

        switch (data.type) {

            case "store_user":
                if (user != null) {
                    // User exists
                    socket.emit('message', { type: 'user already exists' });
                    return;
                }

                const newUser = { name: data.name, conn: socket };
                users.push(newUser);
                console.log(`users is : ${users}`)
                break;

            case "start_call":
              console.log(`user class is : ${users}`)
                let userToCall = findUser(data.target);
                console.log(  `userTocall is : ${userToCall}`)
                if (userToCall) {
                    socket.emit('message', { type: "call_response", data: "user is ready for call" });
                } else {
                    socket.emit('message', { type: "call_response", data: "user is not online" });
                }
                break;

            case "create_offer":
                let userToReceiveOffer = findUser(data.target);
                console.log(`create offer data is : ${data}`)
                if (userToReceiveOffer) {
                    userToReceiveOffer.conn.emit('message', {
                        type: "offer_received",
                        name: data.name,
                        data: data.data.sdp
                    });
                }
                break;

            case "create_answer":
                let userToReceiveAnswer = findUser(data.target);
                console.log(`creatge answer data is : ${data}`)
                if (userToReceiveAnswer) {
                    userToReceiveAnswer.conn.emit('message', {
                        type: "answer_received",
                        name: data.name,
                        data: data.data.sdp
                    });
                }
                break;

            case "ice_candidate":
                let userToReceiveIceCandidate = findUser(data.target);
            
                if (userToReceiveIceCandidate) {
                    
                    const d = {
                        type: "ice_candidate",
                        name: data.name,
                        data: {
                            sdpMLineIndex: data.data.sdpMLineIndex,
                            sdpMid: data.data.sdpMid,
                            sdpCandidate: data.data.sdpCandidate
                        }
                    }
                    console.log(`ddddddddddddddddddddd : ${JSON.stringify(d, null, 2)}`);
                    userToReceiveIceCandidate.conn.emit('message', d);
                }
                break;
                
            case "contacts":
                    
                        const userNameList = []
                        users.forEach((user)=>{
                           
                                userNameList.push(user.name)
                            
                            
                        })
                      
                       io.emit('message' , {
                            type: "contacts",
                             name: data.name,
                            data: userNameList
                        } );
                        
                    
                    break;
                    
        }
    });

    socket.on('disconnect', () => {
          
        users.forEach(user => {
            if (user.conn === socket) {
                users.splice(users.indexOf(user), 1);
            }
        });
       
        const userNameList = []
        users.forEach((user)=>{
            
                userNameList.push(user.name)
            
            
        })
    
       io.emit('message' , {
            type: "contacts",
             name: '',
            data: userNameList
        } );
        
       

        console.log("User disconnected");
    });
});

const findUser = (username) => {
    return users.find(user => user.name === username);
};
