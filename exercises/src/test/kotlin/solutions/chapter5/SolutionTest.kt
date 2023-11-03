package solutions.chapter5

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo


class E1SolutionTest {
	@Test
	fun `collatzR returns the correct sequence`() {
		expectThat(13.collatz()).isEqualTo(listOf(13, 40, 20, 10, 5, 16, 8, 4, 2, 1))
		expectThat(8.collatz()).isEqualTo(listOf(8, 4, 2, 1))
	}
}

class E2SolutionTest {
	@Test
	fun `elevator is not stuck in the shaft`() {
		val values = listOf(Up, Up, Down, Up, Down, Down, Up, Up, Up, Down)

		val tot = values.fold(Elevator(0)){ elevator, direction ->
			when(direction) {
				Up -> Elevator(elevator.floor + 1)
				Down -> Elevator(elevator.floor - 1)
			}
		}

		expectThat(tot).isEqualTo(Elevator(2))
	}
}

class E3SolutionTest {
	@Test
	fun `compacts json`() {
		val jsonText = """{ "my greetings" :   "hello world! \"How are you?\"" }"""
		val expected = """{"my greetings":"hello world! \"How are you?\""}"""
		expectThat(compactJson(jsonText)).isEqualTo(expected)
	}

	@Test
	fun `compacts multi-line json`() {
		val jsonText = """{ 
            "my greetings"   : "hello world! \"How are you?\"" 
            }"""
		val expected = """{"my greetings":"hello world! \"How are you?\""}"""
		expectThat(compactJson(jsonText)).isEqualTo(expected)
	}
}

class E4SolutionTest {


	@Test
	fun `monoid folding lists`() {
		with( Monoid(0, Int::plus)){
			expectThat( listOf(1,2,3,4,10).fold() )
				.isEqualTo(20)
		}
		
		with( Monoid("", String::plus)){
			expectThat( listOf( "My", "Fair", "Lady").fold() )
				.isEqualTo("MyFairLady")
		}
	
	
	 	data class Money(val amount: Double) {
			fun sum(other: Money) = Money(this.amount + other.amount)
		}
		val zeroMoney = Money(0.0)

		with( Monoid(zeroMoney, Money::sum) ) {
			expectThat( listOf(Money(2.1), Money(3.9), Money(4.0)).fold() )
				.isEqualTo(Money(10.0))
		}
	}
}

