import re


pattern = r"p=(\d+),(\d+) v=(-?\d+),(-?\d+)"
regex = re.compile(pattern)

input_file_path = ""
output_file_path = ""

with open(input_file_path, "r") as file:
    output_file = open(output_file_path, "a")
    for match in regex.findall(file.read()):
        p_x, p_y, d_x, d_y = tuple(map(int, match))
        output_file.write(f"Bot(x = {p_x}, y = {p_y}, dx = {d_x}, dy = {d_y}),")
    output_file.close()