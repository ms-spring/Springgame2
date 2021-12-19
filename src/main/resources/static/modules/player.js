import {Entity} from "./entity.js";
import {Wall} from "./wall.js";
import {Game} from "./game.js";

export class Player extends Entity {
    static MOVE_NONE = 0;
    static MOVE_LEFT = 1;
    static MOVE_RIGHT = 2;
    static MOVE_UP = 4;
    static MOVE_DOWN = 8;

    constructor(game, name) {
        super(game);
        this.name = name;
        this.x = 0;
        this.y = 0;
        this.move = Player.MOVE_NONE;
    }

    update(t) {
        super.update(t);

        const SPEED = 200, RADIUS = 10;

        // Determine the movement based on flags
        let dx = 0, dy = 0;
        if (this.move & Player.MOVE_LEFT) dx = -1;
        if (this.move & Player.MOVE_RIGHT) dx = 1;
        if (this.move & Player.MOVE_UP) dy = -1;
        if (this.move & Player.MOVE_DOWN) dy = 1;

        // Ensure diagonal is as fast as going straight
        let len = (dx ** 2 + dy ** 2) ** 0.5;
        if (len > 0) {
            this.x += dx * SPEED / len * t;
            this.y += dy * SPEED / len * t;
        }

        // Check collision
        for (let o of this.game.getEntitiesOfClass(Wall)) {
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
        this.x = Math.min(Math.max(this.x, RADIUS), Game.WIDTH - RADIUS);
        this.y = Math.min(Math.max(this.y, RADIUS), Game.HEIGHT - RADIUS);
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