import fs from 'node:fs';

function isCorrectMul(input, begin, end) {
    return input.substring(begin, end + 1).match(/mul\([0-9]{1,3},[0-9]{1,3}\)/)
}

function extractFromMul(mul) {
    const removedFirstChars = mul.substring(4)
    const onlyNumbers = removedFirstChars.substring(0, removedFirstChars.length - 1)
    return onlyNumbers.split(",").map((number) => parseInt(number))
}

function scanInput(input) {
    const foundMuls = []
    let begin = 0
    let end = 1
    while(end < input.length && begin < input.length) {
        if(input[begin] === 'm') {
            if(input[end] === 'm'){
                begin = end
                end = begin + 1
            }
            else if(input[end] === ')') {
                if(isCorrectMul(input, begin, end)) {
                    foundMuls.push(input.substring(begin, end + 1))
                }
                begin = end + 1
                end = begin + 1
            }
            else {
                end = end + 1
            }
        }
        else {
            begin += 1
            end += 1
        }
    }
    return foundMuls
                .map((mul) => extractFromMul(mul))
                .map((arr) => arr[0] * arr[1])
                .reduce((prev, curr) => prev + curr, 0)
}

fs.readFile('mul_input.txt', 'utf8', (err, data) => {
    console.log(scanInput(data))
})
