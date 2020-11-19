let stompClient = null;

const setConnected = (connected) => {
    $("#connect").prop("disabled", connected);
    $("#send").prop("disabled", !connected);
    $("#findByMask").prop("disabled", !connected);
    $("#newUser").prop("disabled", !connected);
    $("#disconnect").prop("disabled", !connected);
    console.log(connected);
    $("#saveButton").prop("disabled", !connected);
    $("#errorsTable").hide();
    console.log(connected);
}

const connect = () => {
    console.log('In Connected function');
    stompClient = Stomp.over(new SockJS('/gs-guide-websocket'));
    console.log('After Stomp.over');
    stompClient.connect({}, (frame) => {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/userList', (usersJson) => showUsers(JSON.parse(usersJson.body)));
        stompClient.subscribe('/topic/operationStatus',
            (operationStatusJson) => showOperationStatus(JSON.parse(operationStatusJson.body)));
        sendGetAllUserListMsg();
    });
}

const disconnect = () => {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

const sendGetAllUserListMsg = () => {
    let searchForm = new Object();
    stompClient.send("/app/user/list", {}, {});
}

const findUsersByMaskMsg = () => {
    let searchForm = new Object();
    searchForm.id = document.getElementById("search-id-input").value;
    searchForm.name = document.getElementById("search-name-input").value;
    searchForm.login = document.getElementById("search-login-input").value;
    stompClient.send("/app/user/search", {}, JSON.stringify(searchForm));
}

const showUsers = (allUsers) => {
    console.log("showUsers");
    $("#errorsTable").hide();
    $("#usersTable").html("");
    allUsers.forEach(function(user) {
        $("#usersTable").append("<tr>" +
                        "<td>" + user.id + "</td>" +
                        "<td>" + user.name + "</td>" +
                        "<td>" + user.login + "</td>" +
                        "<td>" + user.password + "</td>" +
                        '<td>' +
                            '<a href="/userForm.html?userId=' + user.id + '">Изменить</a>' +
                            '&nbsp;' +
                            '<a href="#" onclick="deleteUser(' + user.id + ');return false;">Удалить</a>' +
                        '</td>' +
                        "</tr>");
    });
}

const newUser = () => {
    window.location.replace("/userForm.html");
}

const deleteUser = (userId) => {
    console.log("deleteUser");
    if(userId > 0) {
        stompClient.send("/app/user/delete/" + userId, {}, {});
        findUsersByMaskMsg();
    }
}

const showOperationStatus = (operationStatus) => {
    console.log("operationStatus = " + operationStatus);

    if (operationStatus.status.toLocaleString().localeCompare("SUCCESS") == 0) {
        $("#errorLines").html("");
        $("#errorLines").append("<tr><td>SUCCESS</td></tr>");
    } else {
        $("#errorLines").html("");
        console.log("ERROR constraintViolations = " + operationStatus.constraintViolations);
        operationStatus.constraintViolations.forEach(function(error){
            $("#errorLines").append("<tr><td>" + error + "</td></tr>");
        });
        $("#errorsTable").show();
    }
}

$(function () {
    $("#connect").click(connect);
    $("#disconnect").click(disconnect);
    $("#send").click(sendGetAllUserListMsg);
    $("#findByMask").click(findUsersByMaskMsg);
    $("#newUser").click(newUser);
});
