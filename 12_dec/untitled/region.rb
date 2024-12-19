# frozen_string_literal: true

class Region
  attr_accessor :cells

  def initialize
    @cells = []
  end

  def perimeter
    @cells.map { |cell| 4 - neighbours(cell).length }.reduce(0, :+)
  end

  def total_cost
    perimeter * @cells.length
  end

  def total_cost_with_discount(grid)
    slides(grid) * @cells.length
  end

  def slides(grid)
    @cells.map { |cell| corners_number(cell, grid) }.reduce(0, :+)
  end

  private

  def neighbours(cell)
    possible_moves = [[0, 1], [1, 0], [0, -1], [-1, 0]]
    @cells
      .select { |other_cell| possible_moves.include?([(other_cell.i - cell.i).abs, (other_cell.j - cell.j).abs]) }
  end

  def corners_number(cell, grid)
    possible_corners = [[[1, 0], [0, 1]], [[1, 0], [0, -1]], [[-1, 0], [0, 1]], [[-1, 0], [0, -1]]]
    possible_corners.select do |f, s|
      (!in_region?(cell.i + f[0], cell.j, cell.value, grid) &&
        !in_region?(cell.i, cell.j + s[1], cell.value, grid)) ||
        (in_region?(cell.i + f[0], cell.j, cell.value, grid) &&
          in_region?(cell.i, cell.j + s[1], cell.value, grid) &&
          !in_region?(cell.i + f[0], cell.j + s[1], cell.value, grid))
    end.length
  end

  def in_region?(i, j, value, grid)
    i >= 0 && i < grid.length && j >= 0 && j < grid[0].length && grid[i][j].value == value
  end
end
