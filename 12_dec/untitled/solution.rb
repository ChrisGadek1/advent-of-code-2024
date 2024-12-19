# frozen_string_literal: true

require_relative './cell'
require_relative './grid'

grid = Grid.new('input.txt')
puts grid.regions.map(&:total_cost).reduce(:+)
puts grid.regions.map { |region| region.total_cost_with_discount(grid.cells) }.reduce(:+)
