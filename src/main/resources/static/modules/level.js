import {Wall} from "./wall.js";
import {Component} from "./component.js";

export class Level extends Component {
    constructor(game) {
        super(game);

        this.walls = [new Wall(this, 200, 300, 50, 200, Math.PI / 8),
            new Wall(this, 260, 300, 50, 200, -Math.PI / 8),
            new Wall(this, 500, 400, 100, 30, 0.1),
            new Wall(this, 200, 50, 100, 30, 0),
            new Wall(this, 350, 50, 100, 30, 0)];

        // TODO: Add bananas
        this.bananas = [];
    }

    update(t) {
        super.update(t);
        this.walls.forEach(w => w.update(t));
    }

    draw(ctx) {
        super.draw(ctx);
        this.walls.forEach(w => w.draw(ctx));
    }
}
