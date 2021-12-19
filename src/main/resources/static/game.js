const MOVE_NONE = 0, MOVE_LEFT = 1, MOVE_RIGHT = 2, MOVE_UP = 4, MOVE_DOWN = 8;
const WIDTH = 800, HEIGHT = 600;

class Entity {
    constructor(game) {
        this.game = game;
    }

    update(t) {

    }

    draw(ctx) {

    }
}

class Player extends Entity {
    constructor(game, name) {
        super(game);
        this.name = name;
        this.x = 0;
        this.y = 0;
        this.move = MOVE_NONE;
    }

    update(t) {
        super.update(t);

        const SPEED = 200, RADIUS = 10;

        // Determine the movement based on flags
        let dx = 0, dy = 0;
        if (this.move & MOVE_LEFT) dx = -1;
        if (this.move & MOVE_RIGHT) dx = 1;
        if (this.move & MOVE_UP) dy = -1;
        if (this.move & MOVE_DOWN) dy = 1;

        // Ensure diagonal is as fast as going straight
        let len = (dx ** 2 + dy ** 2) ** 0.5;
        if (len > 0) {
            this.x += dx * SPEED / len * t;
            this.y += dy * SPEED / len * t;
        }

        // Check collision
        for (let o of this.game.getEntitiesOfClass(Obstacle)) {
            // Convert player coordinates relative to obstacle so that the obstacle is no longer rotated
            let lx = this.x - o.x;
            let ly = this.y - o.y;
            let r = (lx ** 2 + ly ** 2) ** 0.5;
            let theta = Math.atan2(ly, lx) - o.r;
            lx = r * Math.cos(theta);
            ly = r * Math.sin(theta);

            // Check for overlap of hit boxes
            if (lx > -o.w / 2 && lx < o.w / 2 && ly > -o.h / 2 && ly < o.h / 2) {
                // Compute overlap from left, right, top and bottom
                // Move so that the least overlap is resolved
                let overlap = [lx + o.w / 2, o.w / 2 - lx, ly + o.h / 2, o.h / 2 - ly];
                let min = Math.min(...overlap);
                if (min === overlap[0]) lx -= overlap[0];
                if (min === overlap[1]) lx += overlap[1];
                if (min === overlap[2]) ly -= overlap[2];
                if (min === overlap[3]) ly += overlap[3];
            }

            // Convert updated relative coordinates back again to player coordinates
            r = (lx ** 2 + ly ** 2) ** 0.5;
            theta = Math.atan2(ly, lx);
            lx = r * Math.cos(theta + o.r);
            ly = r * Math.sin(theta + o.r);
            this.x = o.x + lx;
            this.y = o.y + ly;
        }

        // Don't let player escape from window
        this.x = Math.min(Math.max(this.x, RADIUS), WIDTH - RADIUS);
        this.y = Math.min(Math.max(this.y, RADIUS), HEIGHT - RADIUS);
    }

    draw(ctx) {
        super.draw(ctx);
        ctx.fillStyle = "black";
        ctx.beginPath();
        ctx.arc(this.x, this.y, 10, 0, 2 * Math.PI);
        ctx.closePath();
        ctx.fill();
    }
}

class Obstacle extends Entity {
    constructor(game, x, y, w, h, r) {
        super(game);
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.r = r;
    }

    update(t) {
        super.update(t);
    }

    draw(ctx) {
        super.draw(ctx);
        ctx.fillStyle = "black";
        ctx.save();
        ctx.translate(this.x, this.y);
        ctx.rotate(this.r);
        ctx.translate(-this.x, -this.y);
        ctx.fillRect(this.x - this.w / 2, this.y - this.h / 2, this.w, this.h);
        ctx.restore();
    }
}

export class Game {
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
            new Obstacle(this, 200, 200, 100, 100, Math.PI / 4),
            new Obstacle(this, 500, 400, 100, 10, 0)
        ];
        this.player = player1;

        this.canvas = document.querySelector("#game-canvas");
        this.ctx = this.canvas.getContext("2d");
        this.lastUpdate = null;

        this.draw();

        $(window).on('keydown', (e) => {
            this.player.move |= (e.keyCode === 37) * MOVE_LEFT | (e.keyCode === 39) * MOVE_RIGHT
                | (e.keyCode === 38) * MOVE_UP | (e.keyCode === 40) * MOVE_DOWN;
        });
        $(window).on('keyup', (e) => {
            this.player.move &= ~((e.keyCode === 37) * MOVE_LEFT | (e.keyCode === 39) * MOVE_RIGHT
                | (e.keyCode === 38) * MOVE_UP | (e.keyCode === 40) * MOVE_DOWN);
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
