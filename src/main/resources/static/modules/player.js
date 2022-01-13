import {Game} from "./game.js";
import {Component} from "./component.js";

function shortAngleDist(from, to) {
    let max_angle = Math.PI * 2;
    let difference = (to - from) % max_angle;
    return 2 * difference % max_angle - difference;
}

function lerpAngle(from, to, weight) {
    return from + shortAngleDist(from, to) * weight;
}

// input: h in [0,360] and s,v in [0,1] - output: r,g,b in [0,255]
function hsv2rgb(h, s, v) {
    let f = (n, k = (n + h / 60) % 6) => v - v * s * Math.max(Math.min(k, 4 - k, 1), 0);
    return [f(5), f(3), f(1)];
}

function colorize(img, hue) {
    let canvas = document.createElement("canvas");
    canvas.setAttribute('width', img.width);
    canvas.setAttribute('height', img.height);

    let ctx = canvas.getContext("2d");
    ctx.drawImage(img, 0, 0);

    // RGBA (4 bytes) per pixel
    let color = hsv2rgb(hue, 1, 1);

    let imgData = ctx.getImageData(0, 0, img.width, img.height);
    for (let i = 0; i < imgData.data.length; i += 4) {
        imgData.data[i] = Math.floor(imgData.data[i] * color[0]);
        imgData.data[i + 1] = Math.floor(imgData.data[i + 1] * color[1]);
        imgData.data[i + 2] = Math.floor(imgData.data[i + 2] * color[2]);
    }

    ctx.putImageData(imgData, 0, 0);

    let colorized = new Image();
    colorized.src = canvas.toDataURL();
    return colorized;
}


function transparize(img, transparency) {
    let canvas = document.createElement("canvas");
    canvas.setAttribute('width', img.width);
    canvas.setAttribute('height', img.height);

    let ctx = canvas.getContext("2d");
    ctx.drawImage(img, 0, 0);


    let imgData = ctx.getImageData(0, 0, img.width, img.height);
    for (let i = 3; i < imgData.data.length; i += 4) {
        imgData.data[i] = Math.floor(imgData.data[i]*transparency);
    }

    ctx.putImageData(imgData, 0, 0);

    let colorized = new Image();
    colorized.src = canvas.toDataURL();
    return colorized;
}

export class Player extends Component {
    static MOVE_NONE = 0;
    static MOVE_LEFT = 1;
    static MOVE_RIGHT = 2;
    static MOVE_UP = 4;
    static MOVE_DOWN = 8;

    constructor(game, name, pd, lobby) {
        super(game);

        this.name = name;

        // The coordinates at which the player is rendered
        this.x = 0;
        this.y = 0;

        // The coordinates at which the player is according to the server
        this.serverX = 0;
        this.serverY = 0;

        this.isLocal = false;
        this.isFaenger = false;
        this.nofungable = false;
        this.move = Player.MOVE_NONE;
        number: this.lobby = lobby;

        this.animTime = 0;
        this.animDir = 0;

        this.img = new Image();
        this.img.onload = (e) => {
            this.img = colorize(this.img, [0, 60, 120, 180, 240, 300][pd.player.color]);
        };
        this.img.src = "player.png";

        this.img2 = new Image();
        this.img2.src = "player2.png"

        this.img3 = new Image();
        this.img3.onload = (e) => {
            this.img3 = transparize(this.img3,0.5);
        };
        this.img3.src = "player2.png"

    }

    update(t) {
        super.update(t);

        // Determine the movement based on flags
        let dx = 0, dy = 0;
        if (this.move & Player.MOVE_LEFT) dx = -1;
        if (this.move & Player.MOVE_RIGHT) dx = 1;
        if (this.move & Player.MOVE_UP) dy = -1;
        if (this.move & Player.MOVE_DOWN) dy = 1;

        // Ensure diagonal is as fast as going straight

        if (this.move !== Player.MOVE_NONE) {
            // Keep the animation move forward
            this.animTime += t;
            // Update the direction smoothly
            this.animDir = lerpAngle(this.animDir, Math.atan2(dy, dx), 0.1);
        } else {
            // Walk stop animation by moving to the closest "standing" frame (0 or 5)
            let animFrame = Math.round(this.animTime / 0.1) % 10;
            if ([1, 2, 6, 7].includes(animFrame)) {
                this.animTime -= t;
            } else if ([3, 4, 8, 9].includes(animFrame)) {
                this.animTime += t;
            }
            if (this.animTime < 0) {
                this.animTime = 1 + (this.animTime % 1);
            }
        }
        this.animTime %= 1;

        let punish = Math.abs(shortAngleDist(this.animDir, Math.atan2(dy, dx))) / Math.PI;
        let SPEED = 175 * (1 - punish), RADIUS = 15;

        if (this.isLocal) {
            // Update x, y directly if we are controlling the local player
            let len = (dx ** 2 + dy ** 2) ** 0.5;
            if (len > 0) {
                this.x += dx * SPEED / len * t;
                this.y += dy * SPEED / len * t;
            }
        } else {
            // Otherwise interpolate the networked x, y to the rendered x, y
            const INTERPOLATION_RATE = 0.25;
            this.x = this.serverX * INTERPOLATION_RATE + this.x * (1 - INTERPOLATION_RATE);
            this.y = this.serverY * INTERPOLATION_RATE + this.y * (1 - INTERPOLATION_RATE);
        }

        // Check collision
        for (let o of this.game.level.walls) {
            // Convert player coordinates relative to obstacle so that the obstacle is no longer rotated
            let lx = this.x - o.x;
            let ly = this.y - o.y;
            let r = (lx ** 2 + ly ** 2) ** 0.5;
            let theta = Math.atan2(ly, lx) - o.r;
            lx = r * Math.cos(theta);
            ly = r * Math.sin(theta);

            // Check for overlap of hit boxes
            if (lx > -o.w / 2 - RADIUS && lx < o.w / 2 + RADIUS && ly > -o.h / 2 - RADIUS && ly < o.h / 2 + RADIUS) {
                // Compute overlap from left, right, top and bottom
                // Move so that the least overlap is resolved
                let overlap = [lx + o.w / 2 + RADIUS, o.w / 2 - lx + RADIUS, ly + o.h / 2 + RADIUS, o.h / 2 - ly + RADIUS];
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

        // Draw the rotated and animated player character
        ctx.save();
        ctx.translate(this.x, this.y);
        ctx.rotate(this.animDir);
        ctx.translate(-this.x, -this.y);

        // Pick the animation frame based on the time 0.0 to 0.99 split over 10 frames

        ctx.drawImage(this.isFaenger ? (this.nonfungable ? this.img3 : this.img2): this.img, (Math.round(this.animTime / 0.1) % 10) * 128, 0, 128, 128, this.x - 32, this.y - 32, 64, 64);
        ctx.restore();

        // Draw the player name
        ctx.textAlign = "center";
        ctx.textBaseline = "bottom";
        ctx.font = "10pt sans-serif";
        ctx.fillStyle = this.isFaenger ? "red" : "black";
        ctx.fillText(this.name, this.x, this.y - 24);
    }

    fromNetwork(data) {
        super.fromNetwork(data);
        if(!this.isLocal) {
            this.serverX = data.player.position.x;
            this.serverY = data.player.position.y;
            this.move = data.player.move;
        }
        this.isFaenger = data.player.isfaenger;
        this.lobby = parseInt(data.lobby);
        console.log("KWAKADUDU " + this.isFaenger + " + asdfasfd " + this.lobby + " asdf  " + typeof(this.lobby));
    }
}