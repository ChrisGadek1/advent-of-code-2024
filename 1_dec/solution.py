def process(list_1, list_2):
    sorted_list_1 = sorted(list_1)
    sorted_list_2 = sorted(list_2)
    current_sum = 0
    for el_1, el_2 in zip(sorted_list_1, sorted_list_2):
        current_sum += abs(el_1 - el_2)
    return current_sum

with open('input.txt', 'r') as file:
    lines = file.readlines()
    list_1 = list(map(lambda line: int(line.split("   ")[0].strip()), lines))
    list_2 = list(map(lambda line: int(line.split("   ")[1].strip()), lines))
    print(process(list_1, list_2))


