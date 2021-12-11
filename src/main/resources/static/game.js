const MOVE_NONE = 0, MOVE_LEFT = 1, MOVE_RIGHT = 2, MOVE_UP = 4, MOVE_DOWN = 8;

class Player {
    constructor() {
        this.name = "Omae Wa";
        this.x = 0;
        this.y = 0;
        this.move = MOVE_NONE;
    }

    update() {
        if (this.move & MOVE_LEFT) {
            this.x -= 2;
        }
        if (this.move & MOVE_RIGHT) {
            this.x += 2;
        }
        if (this.move & MOVE_UP) {
            this.y -= 2;
        }
        if (this.move & MOVE_DOWN) {
            this.y += 2;
        }
    }
}

class Obstacle {
    constructor(x, y, w, h, r) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.r = r;
    }
}

export class Game {
    constructor() {
        let player1 = new Player();
        player1.x = 200;
        player1.y = 300;
        player1.name = "Watashi";
        let player2 = new Player();
        player2.x = 500;
        player2.y = 100;
        player2.name = "Omae";
        this.players = [player1, player2];
        this.obstacles = [new Obstacle(20, 20, 40, 40, 10), new Obstacle(500, 400, 100, 10, 45)];
        this.canvas = document.querySelector("#game-canvas");
        this.ctx = this.canvas.getContext("2d");
        this.draw();

        $(window).on('keydown', (e) => {
            this.players[0].move |= (e.keyCode === 37) * MOVE_LEFT | (e.keyCode === 39) * MOVE_RIGHT
                | (e.keyCode === 38) * MOVE_UP | (e.keyCode === 40) * MOVE_DOWN;
        });
        $(window).on('keyup', (e) => {
            this.players[0].move &= ~((e.keyCode === 37) * MOVE_LEFT | (e.keyCode === 39) * MOVE_RIGHT
                | (e.keyCode === 38) * MOVE_UP | (e.keyCode === 40) * MOVE_DOWN);
        });

        window.requestAnimationFrame(() => this.update());
    }

    update() {
        for (let p of this.players) {
            p.update();
        }
        this.draw();
        window.requestAnimationFrame(() => this.update());
    }

    draw() {
        let g = this.ctx;
        g.clearRect(0, 0, 800, 600);

        for (let o of this.obstacles) {
            g.save();
            g.translate(o.x + o.w / 2, o.y + o.h / 2);
            g.rotate(o.r / 180 * Math.PI);
            g.translate(-o.x, -o.y);
            g.fillRect(o.x, o.y, o.w, o.h);
            g.restore();
        }

        for (let p of this.players) {
            g.beginPath();
            g.arc(p.x, p.y, 10, 0, 2 * Math.PI);
            g.closePath();
            g.fill();
        }
    }
}
