import {Player} from "./player.js";
import {Level} from "./level.js";
import {Component} from "./component.js";

class Particle extends Component {
    constructor(game, x, y) {
        super(game);
        this.x = x + Math.random() * 50 - 25;
        this.y = y + Math.random() * 50 - 25;
        this.velX = Math.random() * 20 - 10;
        this.velY = Math.random() * 20;
        this.timeUntilDeath = Math.random();
        this.alive = true;
        this.size = Math.random() * 25 + 5;
    }

    update(t) {
        super.update(t);
        this.timeUntilDeath -= t;
        if (this.timeUntilDeath <= 0) {
            this.alive = false;
        }
        this.x += this.velX * t;
        this.y += this.velY * t;
        this.size += t * 10;
    }

    draw(ctx) {
        super.draw(ctx);
        ctx.globalAlpha = Math.sqrt(this.timeUntilDeath);
        ctx.drawImage(this.game.particleImg, this.x - this.size / 2, this.y - this.size / 2, this.size, this.size);
        ctx.globalAlpha = 1;
    }
}

export class Game extends Component {
    static WIDTH = 800;
    static HEIGHT = 600;

    constructor(socket) {
        super(null);

        this.socket = socket;

        this.level = new Level(this);
        this.players = [];

        this.particles = [];
        this.particleImg = new Image();
        this.particleImg.src = "particle.png";

        this.localName = null;

        this.canvas = document.querySelector("#game-canvas");
        this.ctx = this.canvas.getContext("2d");
        this.lastUpdate = null;

        this.time = 0;

        this.draw();

        $(window).on('keydown', (e) => {
            let local = this.getLocalPlayer();
            if (local === undefined) {
                return;
            }
            local.move |= (e.keyCode === 37) * Player.MOVE_LEFT | (e.keyCode === 39) * Player.MOVE_RIGHT
                | (e.keyCode === 38) * Player.MOVE_UP | (e.keyCode === 40) * Player.MOVE_DOWN;
        });
        $(window).on('keyup', (e) => {
            let local = this.getLocalPlayer();
            if (local === undefined) {
                return;
            }
            local.move &= ~((e.keyCode === 37) * Player.MOVE_LEFT | (e.keyCode === 39) * Player.MOVE_RIGHT
                | (e.keyCode === 38) * Player.MOVE_UP | (e.keyCode === 40) * Player.MOVE_DOWN);
        });

        // Initialize game loop
        window.requestAnimationFrame((t) => this.loop(t));

        // Add network update loop
        window.setInterval(() => this.toNetwork(), 100);
    }

    getLocalPlayer() {
        if (this.localName === null) {
            // We have not logged in yet
            return undefined;
        }
        // Find the player that has our local name
        return this.players.find(p => p.name === this.localName);
    }

    loop(t) {
        let delta = (t - this.lastUpdate) / 1000;
        this.time += delta;

        this.update(delta);
        this.draw();

        // Setup next game loop update
        this.lastUpdate = t;
        window.requestAnimationFrame((t) => this.loop(t));
    }

    update(t) {
        this.level.update(t);
        for (let e of this.players) {
            e.update(t);
        }

        this.particles.forEach(p => p.update(t));
        this.particles = this.particles.filter(p => p.alive);

        let faenger = this.players.find(p => p.isFaenger);
        if (faenger !== undefined && Math.random() > 0.9) {
            this.particles.push(new Particle(this, faenger.x, faenger.y));
        }
    }

    draw() {
        this.ctx.fillStyle = "#f0f3f6";
        this.ctx.fillRect(0, 0, 800, 600);
        this.level.draw(this.ctx);
        for (let e of this.players) {
            e.draw(this.ctx);
        }
        this.particles.forEach(p => p.draw(this.ctx));
    }

    toNetwork() {
        // Since updates only contain the local player state, networking is not done in `Player`
        let local = this.getLocalPlayer();
        if (local === undefined) {
            return;
        }
        this.socket.send("/app/update", {}, JSON.stringify({position: {x: local.x, y: local.y}, move: local.move}));
    }

    fromNetwork(data) {
        // Update the level state
        this.level.fromNetwork(data);

        // Keep track of all players that were contained in the update (to detect removed players)
        let updatedNames = [];

        // Update or add new players
        for (let pd of data.players) {
            updatedNames.push(pd.name);

            // Find the player with the name
            let player = this.players.find(p => p.name === pd.name);
            if (player === undefined) {
                // Player with that name does not exist yet, create it
                player = new Player(this, pd.name, pd);
                this.players.push(player);
            }

            // Ignore updates to local player
            if (pd.name !== this.localName) {
                // Update the player from network in both cases
                player.fromNetwork(pd);
            }

        }

        //Store fungability
        this.nonfungable = data.nonfungable;

        // Store in each player whether they are locally controlled or not
        this.players.forEach(p => p.isLocal = p.name === this.localName);

        // Remove all players that were not updated
        this.players = this.players.filter(p => updatedNames.includes(p.name));

        // Store in each player whether they are faenger or not
        this.players.forEach(p => {p.isFaenger = p.name === (data.faenger ? data.faenger.name : null) ; p.nonfungable = data.nonfungable});
    }
}
