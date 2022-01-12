import {Game} from './modules/game.js'

function setStage(id) {
    $('#stage-join').toggle(id === 'join');
    $('#stage-game').toggle(id === 'game');
}

var stompClient = null;
var game;

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
    var socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
        stompClient.subscribe('/game/broadcast', function (data) {
            game.fromNetwork(JSON.parse(data.body));
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
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#username").val(), 'lobby': $("#lobby").val()}));
    game.localName = $("#username").val();
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(window).on('load', () => {
    setStage('join');
    connect();
    game = new Game(stompClient);
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#btn-join").click(() => {
        sendName();
        setStage('game');
    });
});