# frozen_string_literal: true

require_relative './region'
require_relative './cell'

class Grid
  attr_accessor :cells, :regions_grid, :regions

  def initialize(path)
    @cells = []
    @regions = []
    load_from_file(path)
    @regions_grid = Array.new(@cells.length) { Array.new(@cells[0].length) }
    create_regions
  end

  def create_regions
    @cells.each_index do |i|
      @cells[i].each_index do |j|
        generate_region(i, j) if @regions_grid[i][j].nil?
      end
    end
  end

  private

  def generate_region(i, j)
    region = Region.new
    @regions.push(region)
    stack = [@cells[i][j]]
    possible_moves = [[0, 1], [1, 0], [0, -1], [-1, 0]]
    while stack.length.positive?
      cell = stack.pop
      @regions_grid[cell.i][cell.j] = region
      region.cells.push(cell)
      current_i = cell.i
      current_j = cell.j
      potential_region_members = possible_moves
                                 .select { |move| can_be_neighbour?(move, @cells[current_i][current_j]) }
                                 .map { |move| @cells[current_i + move[0]][current_j + move[1]] }
      potential_region_members.map { |nei| @regions_grid[nei.i][nei.j] = region }
      stack += potential_region_members
    end
  end

  def can_be_neighbour?(move, cell)
    cell.i + move[0] >= 0 &&
      cell.i + move[0] < @cells.length &&
      cell.j + move[1] >= 0 &&
      cell.j + move[1] < @cells[0].length &&
      cell.value == @cells[cell.i + move[0]][cell.j + move[1]].value &&
      @regions_grid[cell.i + move[0]][cell.j + move[1]].nil?
  end

  def load_from_file(path)
    File.read(path).split("\n").map.with_index do |line, i|
      @cells.push(line.chars.map.with_index { |char, j| Cell.new(i, j, char) })
    end
  end
end
