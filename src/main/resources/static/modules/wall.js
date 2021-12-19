import {Entity} from "./entity.js";

export class Wall extends Entity {
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
