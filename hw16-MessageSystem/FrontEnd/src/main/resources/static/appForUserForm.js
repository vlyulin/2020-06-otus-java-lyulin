let stompClient = null;
let origUser = null;

const prepareForm = (userId) => {

    if( userId > 0 ) {
        console.log("urlParams.has userId");
        $("#formTitle").html("Изменить");
    }
    else {
        console.log("urlParams does not has userId");
        $("#formTitle").html("Создать");
    }
    $("#errorsTable").hide();
}

const connect = () => {
    console.log('In Connected function');
    // https://www.sitepoint.com/get-url-parameters-with-javascript/
    const queryString = window.location.search;
    console.log(queryString);
    const urlParams = new URLSearchParams(queryString);

    let userId = 0;
    if( urlParams.has('userId') ) {
        userId = urlParams.get('userId');
    }
    prepareForm(userId);

    stompClient = Stomp.over(new SockJS('/gs-guide-websocket'));
    console.log('After Stomp.over');

    stompClient.connect({}, (frame) => {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/saveUserOperationStatus',
            (operationStatusJson) => saveUserOperationStatus(JSON.parse(operationStatusJson.body)));
        stompClient.subscribe('/topic/getUser', (userJson) => fillUserData(JSON.parse(userJson.body)));
        if(userId != 0) {
            console.log("ask data for userId = " + userId);
            getUser(userId);
        }
    });
}

const disconnect = () => {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

const getUser = (userId) => {
    stompClient.send("/app/user/" + userId, {}, {});
}

const fillUserData = (user) => {
    origUser = user;
    console.log("fillUserData");
    document.getElementById("userId").value = user.id;
    document.getElementById("age").value = user.age;
    document.getElementById("name").value = user.name;
    document.getElementById("login").value = user.login;
    document.getElementById("password").value = user.password;
}

const resetForm = () => {
    console.log("resetForm");

    if(origUser != null) {
        document.getElementById("age").value = origUser.age;
        document.getElementById("name").value = origUser.name;
        document.getElementById("login").value = origUser.login;
        document.getElementById("password").value = origUser.password;
    }
    else {
        document.getElementById("age").value = "";
        document.getElementById("name").value = "";
        document.getElementById("login").value = "";
        document.getElementById("password").value = "";
    }
}

const saveUser = () => {
    let user = new Object();
    user.id = document.getElementById("userId").value;
    user.name = document.getElementById("name").value;
    user.age = document.getElementById("age").value;
    user.login = document.getElementById("login").value;
    user.password = document.getElementById("password").value;
    stompClient.send("/app/user/save", {}, JSON.stringify(user));
}

const saveUserOperationStatus = (operationStatus) => {
    console.log("operationStatus = " + operationStatus);

    if (operationStatus.status.toLocaleString().localeCompare("SUCCESS") == 0) {
        console.log("backToUserList()")
        backToUserList();
    } else {
        $("#errorLines").html("");
        console.log("ERROR constraintViolations = " + operationStatus.constraintViolations);
        operationStatus.constraintViolations.forEach(function(error){
            $("#errorLines").append("<tr><td>" + error + "</td></tr>");
        });
        $("#errorsTable").show();
    }
}

const backToUserList = () => {
    // console.log("ask backToUserList")
    disconnect();
    window.location.replace("/userList.html");
}

$(function () {
    $("#connect").click(connect);
    $("#disconnect").click(disconnect);
    $("#save").click(saveUser);
    $("#reset").click(resetForm);
});
