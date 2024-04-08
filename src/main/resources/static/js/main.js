'use strict';

const usernamePage = document.querySelector('#username-page');
const chatPage = document.querySelector('#chat-page');
const usernameForm = document.querySelector('#usernameForm');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const connectingElement = document.querySelector('.connecting');
const chatArea = document.querySelector('#chat-messages');
const logout = document.querySelector('#logout');

let stompClient = null;
let username = null;
let fullname = null;
let selectedUserId = null;

function connect(event) {
    username = document.querySelector('#nickname').value.trim();
    fullname = document.querySelector('#fullname').value.trim();

    if (username && fullname) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}


function onConnected() {
    stompClient.subscribe(`/user/${username}/queue/messages`, onMessageReceived);
    stompClient.subscribe(`/user/public`, onMessageReceived);
    

    // Subscribe to the Like Channel to see if it work
    stompClient.subscribe('/topic/like/1', function (ackMessage) {
        // Handle ACK message received
        const receivedAck = JSON.parse(ackMessage.body);
        console.log('Like update received :', receivedAck);
    });

    // Subscribe to the Comment count Channel to see if it work
    stompClient.subscribe('/topic/commentCount/1', function (ackMessage) {
        // Handle ACK message received
        const receivedAck = JSON.parse(ackMessage.body);
        console.log('Comment received :', receivedAck);
    });

    // Subscribe to the Comment Reply Channel to see if it work
    stompClient.subscribe('/topic/comment/reply/1', function (ackMessage) {
        // Handle ACK message received
        const receivedAck = JSON.parse(ackMessage.body);
        console.log('Comment Reply received :', receivedAck);
    });

    // Subscribe to the Comment Reply Channel to see if it work
    stompClient.subscribe('/topic/comment/1', function (ackMessage) {
        // Handle ACK message received
        const receivedAck = JSON.parse(ackMessage.body);
        console.log('Comment received :', receivedAck);
    });

    // register the connected user
    stompClient.send("/app/user.addUser",
        {},
        JSON.stringify({username : username, fullName: fullname, status: 'ONLINE'})
    );
    document.querySelector('#connected-user-fullname').textContent = fullname;
    findAndDisplayConnectedUsers().then();
}

async function findAndDisplayConnectedUsers() {
    const connectedUsersResponse = await fetch(`/users/${username}`);
    let connectedUsers = await connectedUsersResponse.json();
    connectedUsers = connectedUsers.filter(user => user.username !== username);
    const connectedUsersList = document.getElementById('connectedUsers');
    connectedUsersList.innerHTML = '';

    connectedUsers.forEach(user => {
        appendUserElement(user, connectedUsersList);
        if (connectedUsers.indexOf(user) < connectedUsers.length - 1) {
            const separator = document.createElement('li');
            separator.classList.add('separator');
            connectedUsersList.appendChild(separator);
        }
    });
}

function appendUserElement(user, connectedUsersList) {
    const listItem = document.createElement('li');
    listItem.classList.add('user-item');
    listItem.id = user.username;

    const userImage = document.createElement('img');
    userImage.src = '../img/user_icon.png';
    userImage.alt = user.name;

    const usernameSpan = document.createElement('span');
    usernameSpan.textContent = user.name;

    const receivedMsgs = document.createElement('span');
    receivedMsgs.textContent = '0';
    receivedMsgs.classList.add('nbr-msg', 'hidden');

    listItem.appendChild(userImage);
    listItem.appendChild(usernameSpan);
    listItem.appendChild(receivedMsgs);

    listItem.addEventListener('click', userItemClick);

    connectedUsersList.appendChild(listItem);
}

function userItemClick(event) {
    document.querySelectorAll('.user-item').forEach(item => {
        item.classList.remove('active');
    });
    messageForm.classList.remove('hidden');

    const clickedUser = event.currentTarget;
    clickedUser.classList.add('active');

    selectedUserId = clickedUser.getAttribute('id');
    fetchAndDisplayUserChat().then();

    const nbrMsg = clickedUser.querySelector('.nbr-msg');
    nbrMsg.classList.add('hidden');
    nbrMsg.textContent = '0';

}

// function displayMessage(senderId, content) {
//     const messageContainer = document.createElement('div');
//     messageContainer.classList.add('message');
//     if (senderId === username) {
//         messageContainer.classList.add('sender');
//     } else {
//         messageContainer.classList.add('receiver');
//     }
//     const message = document.createElement('p');
//     message.textContent = content;
//     messageContainer.appendChild(message);
//     chatArea.appendChild(messageContainer);
// }

function displayMessage(senderId, content, status,messageId) {
    const messageContainer = document.createElement('div');
    messageContainer.id =  messageId;
    messageContainer.classList.add('message');
    if (senderId === username) {
        messageContainer.classList.add('sender');

        // Create message delivery indicator (gray tick)
    const deliveryIndicator = document.createElement('span');
    deliveryIndicator.textContent = '✓'; // Unicode check mark symbol
    deliveryIndicator.classList.add('delivery-indicator');
    if (status === 'delivered') {
        deliveryIndicator.style.color = 'gray'; // Gray color for delivered but not read
    } else if (status === 'read') {
        deliveryIndicator.style.color = 'blue'; // Blue color for read
    }else if (status === 'pending') {
        deliveryIndicator.style.color = 'red'; // Blue color for read
    }

    const message = document.createElement('p');
    message.textContent = content;
    

    messageContainer.appendChild(message);
    messageContainer.appendChild(deliveryIndicator);
    } else {
        messageContainer.classList.add('receiver');

        const message = document.createElement('p');
        message.textContent = content;
        messageContainer.appendChild(message);
    }

    
    chatArea.appendChild(messageContainer);
}

async function fetchAndDisplayUserChat() {
    const userChatResponse = await fetch(`/messages/${username}/${selectedUserId}`);
    const userChat = await userChatResponse.json();
    chatArea.innerHTML = '';
    userChat.forEach(chat => {
        displayMessage(chat.senderId, chat.content,"read",chat.id);
    });
    chatArea.scrollTop = chatArea.scrollHeight;
}


function onError() {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    const messageContent = messageInput.value.trim();
if (messageContent && stompClient) {
    const chatMessage = {
        senderId: username,
        recipientId: selectedUserId,
        content: messageInput.value.trim(),
        timestamp: new Date()
    };
    
    const messageId = Date.now().toString(); // Generate a unique message ID
    
    // Subscribe to the ACK destination to receive ACK messages
    stompClient.subscribe(`/topic/ack/${username}`, function (ackMessage) {
        // Handle ACK message received
        const receivedAck = JSON.parse(ackMessage.body);
        console.log('ACK received for message:', receivedAck);
    });
    
    // Send the message
    stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));
    
    // Display the message immediately (optimistic update)
    displayMessage(username, messageInput.value.trim(), 'pending', messageId);
    
    // Clear message input
    messageInput.value = '';
}
chatArea.scrollTop = chatArea.scrollHeight;
event.preventDefault();

}



async function onMessageReceived(payload) {
    await findAndDisplayConnectedUsers();
    console.log('Message received', payload);
    const message = JSON.parse(payload.body);
    if (selectedUserId && selectedUserId === message.senderId) {
        displayMessage(message.senderId, message.content);
        chatArea.scrollTop = chatArea.scrollHeight;
    }

    if (selectedUserId) {
        document.querySelector(`#${selectedUserId}`).classList.add('active');
    } else {
        messageForm.classList.add('hidden');
    }

    const notifiedUser = document.querySelector(`#${message.senderId}`);
    if (notifiedUser && !notifiedUser.classList.contains('active')) {
        const nbrMsg = notifiedUser.querySelector('.nbr-msg');
        nbrMsg.classList.remove('hidden');
        nbrMsg.textContent = '';
    }
}

function onLogout() {
    stompClient.send("/app/user.disconnectUser",
        {},
        JSON.stringify({username: username, fullName: fullname, status: 'OFFLINE'})
    );
    window.location.reload();
}

usernameForm.addEventListener('submit', connect, true); // step 1
messageForm.addEventListener('submit', sendMessage, true);
logout.addEventListener('click', onLogout, true);
window.onbeforeunload = () => onLogout();
