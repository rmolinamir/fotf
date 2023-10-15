package solutions.chapter2

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotEqualTo


class E1SolutionTest {
    @Test
    fun `concat additions`() {
        fun plusOne(num: Int) = num + 1
        fun plusTwo(num: Int) = num + 2
        fun plusThree(num: Int) = num + 3

        val hof = ::plusOne andThen
                ::plusTwo andThen
                ::plusThree

        expectThat(hof(0)).isEqualTo(6)
    }
}

class E2SolutionTest {
    @Test
    fun push() {
        val stack1 = FunStack<Char>()
        val stack2 = stack1.push('A')
        expectThat(stack1).isNotEqualTo(stack2)
        expectThat(stack1.size()).isEqualTo(0)
        expectThat(stack2.size()).isEqualTo(1)
    }

    @Test
    fun pop() {
        val (q, stack) = FunStack<Char>().push('Q').pop()
        expectThat(stack.size()).isEqualTo(0)
        expectThat(q).isEqualTo('Q')
    }

    @Test
    fun `push & pop`() {
        val (q, stack) = FunStack<Char>().push('A').push('B').pop()
        expectThat(stack.size()).isEqualTo(1)
        expectThat(q).isEqualTo('B')
    }
}

class E3SolutionTest {

    @Test
    fun `a simple sum`() {
        expectThat(calcRpn("4 5 +")).isEqualTo(9.0)
    }

    @Test
    fun `a double operation`() {
        expectThat(calcRpn("3 2 1 - +")).isEqualTo(4.0)
    }

    @Test
    fun `a division`() {
        expectThat(calcRpn("6 2 /")).isEqualTo(3.0)
    }

    @Test
    fun `a more complicated operation`() {
        expectThat(calcRpn("6 2 1 + /")).isEqualTo(2.0)
        expectThat(calcRpn("5 6 2 1 + / *")).isEqualTo(10.0)
    }

    @Test
    fun `a bit of everything`() {
        expectThat(calcRpn("2 5 * 4 + 3 2 * 1 + /")).isEqualTo(2.0)
    }

}