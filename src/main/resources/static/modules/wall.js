import {Component} from "./component.js";

export class Wall extends Component {
    static FLAG_STATIC = 0;
    static FLAG_ROTATE = 1;

    constructor(game, x, y, w, h, r, flags) {
        super(game);
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.r = r;
        this.origR = r;
        this.flags = flags;
    }

    update(t) {
        super.update(t);
        if(this.flags & Wall.FLAG_ROTATE) {
            this.r = this.origR + (new Date().getTime() / 1000) % (2 * Math.PI);
        }
    }

    draw(ctx) {
        super.draw(ctx);

        ctx.fillStyle = "#919dae";
        ctx.strokeStyle = "black";
        ctx.save();
        ctx.translate(this.x, this.y);
        ctx.rotate(this.r);
        ctx.translate(-this.x, -this.y);
        ctx.fillRect(this.x - this.w / 2, this.y - this.h / 2, this.w, this.h);
        ctx.strokeRect(this.x - this.w / 2, this.y - this.h / 2, this.w, this.h);
        ctx.restore();
    }
}
