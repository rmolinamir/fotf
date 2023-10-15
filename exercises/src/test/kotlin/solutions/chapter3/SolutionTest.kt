package solutions.chapter3

import com.ubertob.pesticide.core.*
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo


internal object DDTs {
    interface CashierActions : DdtActions<DdtProtocol> {
        fun setupPrices(prices: Map<Item, Double>)
        fun totalFor(actorName: String): Double
        fun addItem(actorName: String, qty: Int, item: Item)
        fun setup3x2(item: Item)
    }

    object DomainActions : CashierActions {
        private val cashier = Cashier()

        override fun setupPrices(prices: Map<Item, Double>) {
            cashier.putAll(prices)
        }

        override fun totalFor(actorName: String): Double =
            cashier.totalFor(actorName)

        override fun addItem(actorName: String, qty: Int, item: Item) =
            cashier.addItem(actorName, qty, item)

        override fun setup3x2(item: Item) =
            cashier.setup3x2(item)

        override val protocol: DdtProtocol
            get() = DomainOnly

        override fun prepare(): DomainSetUp = Ready
    }

    val allActions = setOf(DomainActions)

    data class Customer(override val name: String) : DdtActor<CashierActions>() {
        fun `can add #qty #item`(qty: Int, item: Item) = step(qty, item.name) {
            addItem(name, qty, item)
        }

        fun `check total is #total`(total: Double) = step(total) {
            expectThat(totalFor(name)).isEqualTo(total)
        }
    }

    class E1CashierDDT : DomainDrivenTest<CashierActions>(allActions) {
        private val alice by NamedActor(::Customer)

        @DDT
        fun `customer can buy an item`() = ddtScenario {
            val prices = mapOf(Item.Carrot to 2.0, Item.Milk to 5.0)

            setUp {
                setupPrices(prices)
            }.thenPlay(
                alice.`can add #qty #item`(3, Item.Carrot),
                alice.`can add #qty #item`(1, Item.Milk),
                alice.`check total is #total`(11.0)
            )
        }
    }

    class E2DiscountDDT : DomainDrivenTest<CashierActions>(allActions) {
        private val alice by NamedActor(::Customer)

        @DDT
        fun `customer can benefit from 3x2 offer`() = ddtScenario {
            val prices = mapOf(Item.Carrot to 2.0, Item.Milk to 5.0)

            setUp {
                setupPrices(prices)
                setup3x2(Item.Milk)
            }.thenPlay(
                alice.`can add #qty #item`(3, Item.Carrot),
                alice.`can add #qty #item`(3, Item.Milk),
                alice.`check total is #total`(16.0)
            )
        }
    }
}

class E3SolutionTest {
    @Test
    fun `position at 0`() {
        val myCharAtPos = buildCharAtPos("Kotlin")
        expectThat(myCharAtPos(0)).isEqualTo('K')
    }
}

class E4SolutionTest {
    @Test
    fun `rendered template`() {
        val template = """
            Happy Birthday {name} {surname}!
            from {sender}.
        """.trimIndent()

        val data = mapOf("name" tag "Uberto",
                         "surname" tag "Barbini",
                         "sender" tag "PragProg")

        val actual = renderTemplate(template, data)

        val expected = """
            Happy Birthday Uberto Barbini!
            from PragProg.
        """.trimIndent()

        expectThat(actual).isEqualTo(expected)
    }
}
