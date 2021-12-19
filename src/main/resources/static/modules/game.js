import {Player} from "./player.js";
import {Wall} from "./wall.js";

export class Game {
    static WIDTH = 800;
    static HEIGHT = 600;
    
    constructor() {
        let player1 = new Player(this, "Oma Wae");
        player1.x = 200;
        player1.y = 300;
        let player2 = new Player(this, "Opa Mo");
        player2.x = 500;
        player2.y = 100;
        this.entities = [
            player1,
            player2,
            new Wall(this, 200, 200, 100, 100, Math.PI / 4),
            new Wall(this, 500, 400, 100, 10, 0)
        ];
        this.player = player1;

        this.canvas = document.querySelector("#game-canvas");
        this.ctx = this.canvas.getContext("2d");
        this.lastUpdate = null;

        this.draw();

        $(window).on('keydown', (e) => {
            this.player.move |= (e.keyCode === 37) * Player.MOVE_LEFT | (e.keyCode === 39) * Player.MOVE_RIGHT
                | (e.keyCode === 38) * Player.MOVE_UP | (e.keyCode === 40) * Player.MOVE_DOWN;
        });
        $(window).on('keyup', (e) => {
            this.player.move &= ~((e.keyCode === 37) * Player.MOVE_LEFT | (e.keyCode === 39) * Player.MOVE_RIGHT
                | (e.keyCode === 38) * Player.MOVE_UP | (e.keyCode === 40) * Player.MOVE_DOWN);
        });

        // Initialize game loop
        window.requestAnimationFrame((t) => this.update(t));
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
        this.ctx.clearRect(0, 0, 800, 600);
        for (let e of this.entities) {
            e.draw(this.ctx);
        }
    }
}
