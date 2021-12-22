import {Player} from "./player.js";
import {Wall} from "./wall.js";

export class Game {
    static WIDTH = 800;
    static HEIGHT = 600;

    constructor(socket) {
        this.socket = socket;

        this.entities = [
            new Wall(this, 200, 300, 50, 200, Math.PI / 8),
            new Wall(this, 260, 300, 50, 200, -Math.PI / 8),
            new Wall(this, 500, 400, 100, 30, 0.1),
            new Wall(this, 200, 50, 100, 30, 0),
            new Wall(this, 350, 50, 100, 30, 0)
        ];
        this.localName = null;

        this.canvas = document.querySelector("#game-canvas");
        this.ctx = this.canvas.getContext("2d");
        this.lastUpdate = null;

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
        window.requestAnimationFrame((t) => this.update(t));

        // Add network update loop
        window.setInterval(() => this.toNetwork(), 100);
    }

    getLocalPlayer() {
        if (this.localName === null) {
            // We have not logged in yet
            return undefined;
        }
        // Find the player that has our local name
        return this.getEntitiesOfClass(Player).find(p => p.name === this.localName);
    }

    getEntitiesOfClass(cls) {
        return this.entities.filter(e => e instanceof cls);
    }

    update(t) {
        let delta = (t - this.lastUpdate) / 1000;
        for (let e of this.entities) {
            e.update(delta);
        }
        this.draw();

        // Setup next game loop update
        this.lastUpdate = t;
        window.requestAnimationFrame((t) => this.update(t));
    }

    draw() {
        this.ctx.fillStyle = "#f0f3f6";
        this.ctx.fillRect(0, 0, 800, 600);
        for (let e of this.entities) {
            e.draw(this.ctx);
        }
    }

    toNetwork() {
        let local = this.getLocalPlayer();
        if (local === undefined) {
            return;
        }
        this.socket.send("/app/update", {}, JSON.stringify({position: {x: local.x, y: local.y}, move: local.move}));
    }

    fromNetwork(data) {
        // Keep track of all players that were contained in the update (to detect removed players)
        let updatedNames = [];

        // Update or add new players
        for (let pd of data.players) {
            updatedNames.push(pd.name);

            // Find the player with the name
            let player = this.getEntitiesOfClass(Player).find(p => p.name === pd.name);
            if (player === undefined) {
                // Player with that name does not exist yet, create it
                player = new Player(this, pd.name);
                this.entities.push(player);
            }
            // Ignore updates to local player
            if (pd.name !== this.localName) {
                // Update the player from network in both cases
                player.fromNetwork(pd);
            }
        }

        // Remove all players that were not updated
        let toRemove = [];
        for (let p of this.getEntitiesOfClass(Player)) {
            p.isLocal = p.name === this.localName;
            if (!updatedNames.includes(p.name)) {
                toRemove.push(p);
            }
        }
        this.entities = this.entities.filter(p => !toRemove.includes(p));
    }
}
