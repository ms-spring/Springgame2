import { Game } from './modules/game.js'

function setStage(id) {
    $('#stage-connect').toggle(id === 'connect');
    $('#stage-login').toggle(id === 'login');
    $('#stage-lobby').toggle(id === 'lobby');
    $('#stage-game').toggle(id === 'game');
}

var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#username").val()}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(window).on('load', () => {
    setStage('connect');
})

$(function () {
    new Game();
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#btn-connect").click(() => {
        connect();
        setStage('login');
    });
    $("#btn-login").click(() => {
        sendName();
        setStage('lobby');
    });
    $("#btn-game-1").click(() => {
        setStage('game');
    })
    $("#btn-game-2").click(() => {
        setStage('game');
    })
    $("#btn-game-3").click(() => {
        setStage('game');
    })
});