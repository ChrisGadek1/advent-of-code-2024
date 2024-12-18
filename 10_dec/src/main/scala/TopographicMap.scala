class TopographicMap(filePath: String):
  val grid: Array[Array[Int]] = TopographicMapLoader.load(filePath)
  var visitedGrid: Array[Array[Boolean]] = Array.ofDim[Boolean](grid.length, grid(0).length)

  private def possibleMoves(position: Position, value: Int): Array[Position] = {
    val initialMoves: Array[Position] = Array(Position(position.i + 1, position.j), Position(position.i, position.j + 1), Position(position.i - 1, position.j), Position(position.i, position.j - 1))
    initialMoves.filter(move => move.i >= 0 && move.i < grid.length && move.j >= 0 && move.j < grid(0).length && grid(move.i)(move.j) == value + 1)
  }

  def findPath(position: Position, value: Int, distinctPath: Boolean): Int = {
    if(value == 9) {
      if(distinctPath) {
        if (!visitedGrid(position.i)(position.j)) {
          visitedGrid(position.i)(position.j) = true
          return 1
        }
        return 0
      }
      else {
        return 1
      }
    }
    possibleMoves(position, value).map(move => findPath(move, value + 1, distinctPath)).sum
  }

  def findAllPaths(distinctPath: Boolean): Int = {
    var counter = 0
    for (i <- Range.inclusive(0, grid.length - 1)) {
      for (j <- Range.inclusive(0, grid(i).length - 1)) {
        if(grid(i)(j) == 0) {
          counter = counter + findPath(Position(i, j), 0, distinctPath)
          visitedGrid = Array.ofDim[Boolean](grid.length, grid(0).length)
        }
      }
    }
    counter
  }
