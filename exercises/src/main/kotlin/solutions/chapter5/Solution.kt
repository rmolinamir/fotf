package solutions.chapter5

import com.ubertob.fotf.exercises.chapter4.Person

// Exercise 5.1: Recursion
fun Int.collatz() = collatzR(listOf(this), this)

tailrec fun collatzR(seq: List<Int>, curr: Int): List<Int> {
    if (curr == 1) return seq
    val next = if (curr % 2 == 0) curr / 2 else (curr * 3) + 1
    return collatzR(seq + listOf(next), next)
}

// Exercise 5.2: Fold
data class Elevator(val floor: Int)

    sealed class Direction

    object Up : Direction()

    object Down : Direction()

// Exercise 5.3: Union Types
sealed class JsonCompactor{
    abstract val jsonCompacted: String
    abstract fun compact(c: Char): JsonCompactor
}

data class OutQuotes(override val jsonCompacted: String): JsonCompactor() {
    override fun compact(c: Char): JsonCompactor = when(c) {
        '"' -> InQuotes(jsonCompacted + '"')
        ' ' -> OutQuotes(jsonCompacted)
        '\n' -> OutQuotes(jsonCompacted)
        else -> OutQuotes(jsonCompacted + c)
    }
}

data class InQuotes(override val jsonCompacted: String): JsonCompactor() {
    override fun compact(c: Char): JsonCompactor = when(c) {
        '"' -> OutQuotes(jsonCompacted + '"')
        '\\' -> Escaped(jsonCompacted + '\\')
        else -> InQuotes(jsonCompacted + c)
    }
}

data class Escaped(override val jsonCompacted: String): JsonCompactor() {
    override fun compact( c: Char): JsonCompactor = InQuotes(jsonCompacted + c)
}

fun compactJson(json: String): String {
    val jsonCompactor = json.fold<JsonCompactor>(OutQuotes("")) { jsonCompactor, c -> jsonCompactor.compact(c) }
    return jsonCompactor.jsonCompacted
}

// Exercise 5.4: Monoid
data class Monoid<T: Any>(val zero: T, val combinator: (T, T) -> T) {
    fun List<T>.fold(): T = fold(zero, combinator)
}