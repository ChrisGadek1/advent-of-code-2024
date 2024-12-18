def load_stones(file_path):
    with open(file_path, "r") as file:
        return list(map(int, file.read().split(" ")))

memoization = {}

def recurrence_blink(left, current):
    if current in memoization:
        if left in memoization[current]:
            return memoization[current][left]
    else:
        memoization[current] = {}
    if left == 0:
        res = 1
    elif current == 0:
        res = recurrence_blink(left - 1, 1)
    elif len(str(current)) % 2 == 0:
        first = int(str(current)[0:len(str(current))//2])
        second = int(str(current)[len(str(current))//2:len(str(current))])
        res = recurrence_blink(left - 1, first) + recurrence_blink(left - 1, second)
    else:
        res = recurrence_blink(left - 1, current * 2024)
    memoization[current][left] = res
    return res

stones = load_stones("input.txt")

res_part_1 = 0
res_part_2 = 0
for stone in stones:
    res_part_1 += recurrence_blink(25, stone)
    res_part_2 += recurrence_blink(75, stone)

print("part one:", res_part_1)
print("part two:", res_part_2)
