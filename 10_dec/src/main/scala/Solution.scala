@main def main(args: String*): Unit = {
  println("part one: "+TopographicMap("input.txt").findAllPaths(true))
  println("part two: "+TopographicMap("input.txt").findAllPaths(false))
}
