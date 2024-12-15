# x,y -> (x+dx) % width, (y+dy) % height
# 2 times -> (((x+dx) % width)+dx % width),(((y+dy) % height)+dy % width)

import re
import sys

pattern = r"p=(\d+),(\d+) v=(-?\d+),(-?\d+)"
regex = re.compile(pattern)
window_to_search_bots = 30

input_file_path = ""

with open(input_file_path, "r") as file:
    def show_bots(bots, WIDTH, HEIGHT):
        display_grid = [(["."] * WIDTH) for _ in range(HEIGHT)]
        # print(len(display_grid))
        for bot in bots:
            x, y, _, _ = bot
            display_grid[y][x] = "a"
        # for row in display_grid:
        #     print("".join(row))
        return display_grid

    def num_bots_in_nearby_30(i, j, grid):
        num_bots = 0
        for k in range(i, i + window_to_search_bots):
            for l in range(j, j + window_to_search_bots):
                if grid[k][l] == "a":
                    num_bots += 1
        return num_bots

    def part1(WIDTH, HEIGHT):
        quadrants = [0] * 4
        for match in regex.findall(file.read()):
            p_x, p_y, d_x, d_y = tuple(map(int, match))
            result_x, result_y = ((p_x + 100 * d_x) % WIDTH, (p_y + 100 * d_y) % HEIGHT)
            if 0 <= result_x <= WIDTH // 2 - 1 and 0 <= result_y <= HEIGHT // 2 - 1:
                quadrants[0] += 1
            elif (
                0 <= result_x <= WIDTH // 2 - 1
                and HEIGHT // 2 + 1 <= result_y <= HEIGHT - 1
            ):
                quadrants[1] += 1
            elif (
                WIDTH // 2 + 1 <= result_x <= WIDTH - 1
                and 0 <= result_y <= HEIGHT // 2 - 1
            ):
                quadrants[2] += 1
            elif (
                WIDTH // 2 + 1 <= result_x <= WIDTH - 1
                and HEIGHT // 2 + 1 <= result_y <= HEIGHT - 1
            ):
                quadrants[3] += 1
        total = 1
        for value in quadrants:
            total *= value
        print(total)

    def part2(WIDTH, HEIGHT):
        bots = []
        for match in regex.findall(file.read()):
            p_x, p_y, d_x, d_y = tuple(map(int, match))
            bots.append((p_x, p_y, d_x, d_y))

        t = 0
        while True:
            current_grid = show_bots(bots, WIDTH, HEIGHT)
            for i in range(len(current_grid) - window_to_search_bots):
                for j in range(len(current_grid[i]) - window_to_search_bots):
                    num_bots = num_bots_in_nearby_30(i, j, current_grid)
                    if num_bots >= 200:
                        # If there are more than 200 bots in a given area,
                        # we can basically be sure that the tree is there.
                        print("tree found: t =", t)
                        sys.exit(0)

            for i, bot in enumerate(bots):
                p_x, p_y, d_x, d_y = bot
                result_x, result_y = ((p_x + d_x) % WIDTH, (p_y + d_y) % HEIGHT)
                bots[i] = (result_x, result_y, d_x, d_y)
            t += 1

    # part1(101, 103)
    part2(101, 103)
