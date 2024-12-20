import fs from 'node:fs';

class Button {
    static loadFromLine(line) {
        const values = line.split(" ");
        const button = new Button()
        button.cost = values[1] == "A:" ? 3 : 1
        button.x = parseInt(values[2].split("+")[1].slice(0, -1))
        button.y = parseInt(values[3].split("+")[1])
        return button
    }
}

class Game {
    static loadFromLines(lines, partTwo) {
        const shift = partTwo ? 10000000000000 : 0
        const game = new Game()
        game.a = Button.loadFromLine(lines[0])
        game.b = Button.loadFromLine(lines[1])
        game.pX = parseInt(parseInt(lines[2].split(" ")[1].split("=")[1].slice(0, -1)) + shift)
        game.pY = parseInt(parseInt(lines[2].split(" ")[2].split("=")[1]) + shift)
        return game;
    }

    get optimalXAndY() {
        const aRes = (this.pY * this.b.x - this.b.y * this.pX) / (this.a.y * this.b.x - this.b.y * this.a.x)
        const bRes = (this.pX - this.a.x * aRes) / this.b.x
        if(this.a.x * this.b.y - this.a.y * this.b.x === 0 || (aRes * 3 + bRes).toString().includes(".")) {
            return Infinity
        }
        return aRes * 3 + bRes
    }
}

function runGameAndGatherResult(lines, i, partTwo) {
    const game = Game.loadFromLines(lines.slice(i, i + 4), partTwo)
    return game.optimalXAndY === Infinity ? 0 : game.optimalXAndY
}

fs.readFile('input.txt', 'utf8', (err, data) => {
    const lines = data.split("\n")
    let sumPartOne = 0, sumPartTwo = 0
    for(let i = 0; i < lines.length; i += 4) {
        sumPartOne += runGameAndGatherResult(lines, i, false)
        sumPartTwo += runGameAndGatherResult(lines, i, true)
    }
    console.log("part one: "+sumPartOne)
    console.log("part two: "+sumPartTwo)
})
