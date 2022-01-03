import {Wall} from "./wall.js";
import {Component} from "./component.js";
import {Banana} from "./banana.js";

export class Level extends Component {
    constructor(game) {
        super(game);

        this.walls = [new Wall(this, 500, 350, 300, 30, 0, Wall.FLAG_STATIC),
            new Wall(this, 500, 500, 300, 30, 0, Wall.FLAG_STATIC),
            new Wall(this, 500, 425, 120, 10, 0, Wall.FLAG_ROTATE),
            new Wall(this, 200, 200, 200, 20, 0, Wall.FLAG_ROTATE),
            new Wall(this, 200, 200, 200, 20, Math.PI / 2, Wall.FLAG_ROTATE),
            new Wall(this, 600, 200, 200, 30, 0, Wall.FLAG_ROTATE),
            new Wall(this, 500, 200, 200, 30, 0, Wall.FLAG_ROTATE)];

        // TODO: Add bananas
        this.bananas = [new Banana(this, 50, 50)];
    }

    update(t) {
        super.update(t);
        this.bananas.forEach(b => b.update(t));
        this.walls.forEach(w => w.update(t));
    }

    draw(ctx) {
        super.draw(ctx);
        this.bananas.forEach(b => b.draw(ctx));
        this.walls.forEach(w => w.draw(ctx));
    }
}
