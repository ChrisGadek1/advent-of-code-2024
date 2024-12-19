# frozen_string_literal: true

class Cell
  attr_accessor :i, :j, :value

  def initialize(i, j, value)
    @i = i
    @j = j
    @value = value
  end
end
