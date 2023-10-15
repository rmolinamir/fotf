package solutions.chapter2


// Exercise 2.1: Chain Functions
typealias FUN<A, B> = (A) -> B

infix fun <A, B, C> FUN<A, B>.andThen(c: FUN<B, C>): FUN<A, C> = fun(a: A): C {
    val b = this
    return c(b(a))
}

// Exercise 2.2: A Functional Stack
data class FunStack<T>(private val elements: List<T> = emptyList()) {
    fun push(aValue: T): FunStack<T> = FunStack(elements.plus(listOf(aValue)))

    fun pop(): Pair<T, FunStack<T>> = Pair(elements.last(), FunStack(elements.dropLast(1)))

    fun size(): Int = elements.size
}

// Exercise 2.3: An RPN Calculator
fun calcRpn(exp: String): Double {
    fun isNumeric(str: String): Boolean {
        return str.toDoubleOrNull() != null
    }

    fun performOperation(operator: String, i: Double, j: Double): Double {
        return when (operator) {
            "+" -> j + i
            "*" -> j * i
            "-" -> j - i
            "/" -> j / i
            else -> throw IllegalArgumentException("Invalid operator: $operator")
        }
    }

    fun operation(stack: FunStack<Double>, current: String): FunStack<Double> {
        return if (isNumeric(current)) {
            stack.push(current.toDouble())
        } else {
            val (i, tmpStack1) = stack.pop()
            val (j, tmpStack2) = tmpStack1.pop()
            tmpStack2.push(performOperation(current, i, j))
        }
    }

    return exp.split(" ").fold(FunStack(), ::operation).pop().first
}
