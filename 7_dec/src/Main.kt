fun loadData(fileName: String): List<Equation> {
    val fileContent = {}.javaClass.getResource(fileName)!!.readText().split("\n")
    return fileContent.map { line ->
        val equationResult = line.split(":")[0].toLong()
        val equationComponents = line.split(":")[1].trim().split(" ").map { it.toLong() }
        Equation(equationResult, equationComponents)
    }
}

fun applyOperator(first: Long, second: Long, operator: String): Long {
    return when(operator){
        "+" -> first + second
        "||" -> (first.toString() + second.toString()).toLongOrNull() ?: -1
        else -> first * second
    }
}

fun findOperators(equation: Equation, currentResult: Long, currentComponentIndex: Int, operatorList: List<String>): Boolean {
    if(equation.components.size == currentComponentIndex) {
        return currentResult == equation.result
    }

    val res =  operatorList.map { operator ->
        when(applyOperator(currentResult, equation.components[currentComponentIndex], operator)) {
            -1L -> false
            in (equation.result + 1..Long.MAX_VALUE) -> false
            else -> findOperators(equation, applyOperator(currentResult, equation.components[currentComponentIndex], operator), currentComponentIndex + 1, operatorList)
        }
    }.reduce { acc, b -> acc || b }
    return res
}

fun getResult(data: List<Equation>, operatorList: List<String>): Long {
    return data
        .filter { eq -> findOperators(eq, eq.components[0], 1, operatorList) }
        .map{ eq -> eq.result }
        .reduce { acc, equation -> acc + equation }
}

fun main() {
    val data: List<Equation> = loadData("input.txt")
    println("part 1: "+ getResult(data, listOf("+", "*")))
    println("part 2: "+ getResult(data, listOf("+", "*", "||")))
}
