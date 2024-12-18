import java.nio.file.{Files, Paths}

object TopographicMapLoader {
  private def loadFromFile(filePath: String): String = {
    Files.readString(Paths.get(this.getClass.getClassLoader.getResource(filePath).getPath))
  }

  private def generateGrid(input: String): Array[Array[Int]] = {
    input.split("\n").map(line => line.toCharArray.map(char => (char.toInt - 48)))
  }

  def load(filePath: String): Array[Array[Int]] = {
    val loaded = loadFromFile(filePath)
    generateGrid(loaded)
  }
}

