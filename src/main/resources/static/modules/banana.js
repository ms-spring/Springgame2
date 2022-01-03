import {Component} from "./component.js";

export class Banana extends Component {
    constructor(game, x, y) {
        super(game);

        this.img = new Image();
        this.img.src = "banana.png";

        this.x = x;
        this.y = y;
    }

    update(t) {
        super.update(t);
    }

    draw(ctx) {
        super.draw(ctx);

        ctx.save();
        ctx.translate(this.x, this.y);
        ctx.rotate(Math.sin(new Date().getTime() / 1000 * 5) * 0.25);
        ctx.translate(-this.x, -this.y);
        ctx.drawImage(this.img, this.x - 20, this.y - 20, 40, 40);
        ctx.restore();
    }
}