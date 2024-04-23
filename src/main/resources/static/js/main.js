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
let selectedUserIdOg = null;
let whoSelected = null;

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
    stompClient.subscribe(`/user/${username}/queue/ack`, sendAck);
    stompClient.subscribe(`/user/${username}/queue/status`, updateMessageStatus);

    stompClient.subscribe('/topic/groupchat/20', onMessageReceived);
    

    // // Subscribe to the Like Channel to see if it work
    // stompClient.subscribe('/topic/like/1', function (ackMessage) {
    //     // Handle ACK message received
    //     const receivedAck = JSON.parse(ackMessage.body);
    //     console.log('Like update received :', receivedAck);
    // });

    // // Subscribe to the Comment count Channel to see if it work
    // stompClient.subscribe('/topic/commentCount/1', function (ackMessage) {
    //     // Handle ACK message received
    //     const receivedAck = JSON.parse(ackMessage.body);
    //     console.log('Comment received :', receivedAck);
    // });

    // // Subscribe to the Comment Reply Channel to see if it work
    // stompClient.subscribe('/topic/comment/reply/1', function (ackMessage) {
    //     // Handle ACK message received
    //     const receivedAck = JSON.parse(ackMessage.body);
    //     console.log('Comment Reply received :', receivedAck);
    // });

    // // Subscribe to the Comment Reply Channel to see if it work
    // stompClient.subscribe('/topic/comment/1', function (ackMessage) {
    //     // Handle ACK message received
    //     const receivedAck = JSON.parse(ackMessage.body);
    //     console.log('Comment received :', receivedAck);
    // });

    // register the connected user
    stompClient.send("/app/user/addUser",
        {},
        JSON.stringify({username : username, fullName: fullname, status: 'ONLINE'})
    );
    document.querySelector('#connected-user-fullname').textContent = fullname;
    findAndDisplayConnectedUsers().then();
    findAndDisplayConnectedGroups().then();
}

async function findAndDisplayConnectedUsers() {
    const connectedUsersResponse = await fetch(`/users/chat-history/${username}`);
    let connectedUsers = await connectedUsersResponse.json();
    console.log(connectedUsers);
    //connectedUsers = connectedUsers.filter(user => user.id != username);
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

async function findAndDisplayConnectedGroups() {
    const connectedGroupsResponse = await fetch(`/groups/${username}`);
    let connectedGroups = await connectedGroupsResponse.json();
    console.log(connectedGroups);
    //connectedUsers = connectedUsers.filter(user => user.id != username);
    const connectedGroupsList = document.getElementById('connectedGroup');
    connectedGroupsList.innerHTML = '';

    connectedGroups.forEach(user => {
        appendGroupElement(user, connectedGroupsList);
        if (connectedGroups.indexOf(user) < connectedGroups.length - 1) {
            const separator = document.createElement('li');
            separator.classList.add('separator');
            connectedGroupsList.appendChild(separator);
        }
    });
}

function appendUserElement(user, connectedUsersList) {
    const listItem = document.createElement('li');
    listItem.classList.add('user-item');
    listItem.id = "e" + user.profileId;

    const userImage = document.createElement('img');
    userImage.src = '../img/user_icon.png';
    userImage.alt = user.username;

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

function appendGroupElement(user, connectedUsersList) {
    const listItem = document.createElement('li');
    listItem.classList.add('group-item');
    listItem.id = "e" + user.groupId;

    const userImage = document.createElement('img');
    userImage.src = '../img/user_icon.png';
    userImage.alt = user.groupId;

    const usernameSpan = document.createElement('span');
    usernameSpan.textContent = user.groupName;

    const receivedMsgs = document.createElement('span');
    receivedMsgs.textContent = '0';
    receivedMsgs.classList.add('nbr-msg', 'hidden');

    listItem.appendChild(userImage);
    listItem.appendChild(usernameSpan);
    listItem.appendChild(receivedMsgs);

    listItem.addEventListener('click', groupItemClick);

    connectedUsersList.appendChild(listItem);
}

function userItemClick(event) {
    document.querySelectorAll('.user-item').forEach(item => {
        item.classList.remove('active');
    });
    messageForm.classList.remove('hidden');

    const clickedUser = event.currentTarget;
    clickedUser.classList.add('active');

    whoSelected = "user";

    selectedUserIdOg = clickedUser.getAttribute('id');
    selectedUserId = selectedUserIdOg.substring(1);
    fetchAndDisplayUserChat().then();

    const nbrMsg = clickedUser.querySelector('.nbr-msg');
    nbrMsg.classList.add('hidden');
    nbrMsg.textContent = '0';

}

function groupItemClick(event) {
    document.querySelectorAll('.group-item').forEach(item => {
        item.classList.remove('active');
    });
    messageForm.classList.remove('hidden');

    const clickedUser = event.currentTarget;
    clickedUser.classList.add('active');

    whoSelected = "group";

    selectedUserIdOg = clickedUser.getAttribute('id');
    selectedUserId = selectedUserIdOg.substring(1);
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
    messageContainer.id =  "c" + messageId;
    messageContainer.classList.add('message');
    if (senderId == username) {
        messageContainer.classList.add('sender');

        // Create message delivery indicator (gray tick)
    const deliveryIndicator = document.createElement('span');
    if (status == 'DELIVERED') {
        deliveryIndicator.textContent = '✓✓';
        deliveryIndicator.classList.add('delivery-indicator');
        deliveryIndicator.style.color = 'gray';
    } else if (status == 'READ') {
        deliveryIndicator.textContent = '✓✓';
        deliveryIndicator.classList.add('delivery-indicator');
        deliveryIndicator.style.color = 'blue';
    }else if (status == 'SENT') {
        deliveryIndicator.textContent = '✓';
        deliveryIndicator.classList.add('delivery-indicator');
        deliveryIndicator.style.color = 'gray';
    }else{
        deliveryIndicator.textContent = '✘';
        deliveryIndicator.classList.add('delivery-indicator');
        deliveryIndicator.style.color = 'red';
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
    const userChatResponse = await fetch(`/group/messages/${username}/${selectedUserId}`);
    const userChat = await userChatResponse.json();
    userChat.sort((a, b) => a.id - b.id);
    chatArea.innerHTML = '';
    userChat.forEach(chat => {
        displayMessage(chat.senderId, chat.content,chat.status,chat.id);
    });
    chatArea.scrollTop = chatArea.scrollHeight;
}


function onError() {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}



function sendMessage(event) {
    const messageContent = messageInput.value.trim();
    const messageId = Date.now().toString(); // Generate a unique message ID
if (messageContent && stompClient) {
    const chatMessage = {
        senderId: username,
        recipientId: selectedUserId,
        content: messageInput.value.trim(),
        tempMessageId : messageId
    };

    
    // Send the message
    stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));
    
    // Display the message immediately (optimistic update)
    displayMessage(username, messageInput.value.trim(), 'PENDING', messageId);
    
    // Clear message input
    messageInput.value = '';
}
chatArea.scrollTop = chatArea.scrollHeight;
event.preventDefault();

}

function sendGroupMessage(event) {
    const messageContent = messageInput.value.trim();
    const messageId = Date.now().toString(); // Generate a unique message ID
if (messageContent && stompClient) {
    const chatMessage = {
        senderId: username,
        recipientId: selectedUserId,
        content: messageInput.value.trim(),
        tempMessageId : messageId
    };

    
    // Send the message
    stompClient.send("/app/group-chat", {}, JSON.stringify(chatMessage));
    
    // Display the message immediately (optimistic update)
    displayMessage(username, messageInput.value.trim(), 'PENDING', messageId);
    
    // Clear message input
    messageInput.value = '';
}
chatArea.scrollTop = chatArea.scrollHeight;
event.preventDefault();

}

function sendAck(ackMessage){
    
    const acknowledgment = JSON.parse(ackMessage.body);
        
    
    const messageContainer = document.getElementById("c" + acknowledgment.tempMessageId);
    if (!messageContainer) {
        console.log('Message container not found for acknowledgment:', acknowledgment);
        return;
    }

// Find the delivery indicator element within the message container
    const deliveryIndicator = messageContainer.querySelector('.delivery-indicator');
    if (!deliveryIndicator) {
        console.log('Delivery indicator not found in message container:', acknowledgment);
        return;
    }

    if(acknowledgment.status === 'SENT'){
        deliveryIndicator.textContent = '✓';
        deliveryIndicator.style.color = 'gray';

        messageContainer.setAttribute('id', 'c'+ acknowledgment.messageId);
    }
}

function updateMessageStatus(payload){
    const acknowledgment = JSON.parse(payload.body);
        
    
    const messageContainer = document.getElementById("c" + acknowledgment.messageId);
    if (!messageContainer) {
        console.log('Message container not found for acknowledgment:', acknowledgment);
        return;
    }

// Find the delivery indicator element within the message container
    const deliveryIndicator = messageContainer.querySelector('.delivery-indicator');
    if (!deliveryIndicator) {
        console.log('Delivery indicator not found in message container:', acknowledgment);
        return;
    }

    if(acknowledgment.status === 'DELIVERED'){

        deliveryIndicator.textContent = '✓✓';
        deliveryIndicator.style.color = 'gray';

    }else if(acknowledgment.status === 'READ'){

        deliveryIndicator.textContent = '✓✓';
        deliveryIndicator.style.color = 'blue';
    }
}

async function onMessageReceived(payload) {
    await findAndDisplayConnectedUsers();
    console.log('Message received', payload);
    const message = JSON.parse(payload.body);

    
    
    if (selectedUserId && selectedUserId == message.senderId) {
        const statusChange = {
            messageId: message.id,
            status: 'DELIVERED'
        };
        stompClient.send("/app/status", {}, JSON.stringify(statusChange));
        displayMessage(message.senderId, message.content, message.status,message.id);
        chatArea.scrollTop = chatArea.scrollHeight;
    }

    if (selectedUserId) {
        document.querySelector(`#${selectedUserIdOg}`).classList.add('active');
    } else {
        messageForm.classList.add('hidden');
    }

    const notifiedUser = document.querySelector(`#${"e"+message.senderId}`);
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

usernameForm.addEventListener('submit', connect, true);
// messageForm.addEventListener('submit', sendMessage, true);
logout.addEventListener('click', onLogout, true);
window.onbeforeunload = () => onLogout();

messageForm.addEventListener('submit', function(event) {
    if (whoSelected == "group") {
        sendGroupMessage(event);
    } else {
        sendMessage(event);
    }
}, true);
