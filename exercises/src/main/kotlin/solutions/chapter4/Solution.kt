package solutions.chapter4

import com.ubertob.fotf.exercises.chapter4.Person

// Exercise 4.1: DiscardUnless
fun <T> T.discardUnless(predicate: T.() -> Boolean): T? = if (predicate()) this else null;

// Exercise 4.2: Chain Nullable Functions
typealias FUN<A, B> = (A) -> B

// First try:
// infix fun <A: Any, B: Any, C: Any> FUN<A, B?>.andUnlessNull(other: FUN<B, C?>): FUN<A, C?> = fun(a: A): C? {
//     val b = this(a)
//     if (b != null) return other(b)
//     return null
// }

// More functional:
infix fun <A: Any, B: Any, C: Any> FUN<A, B?>.andUnlessNull(other: FUN<B, C?>): FUN<A, C?> = fun(a: A): C? = this(a)?.let(other)

// Exercise 4.3: Currying
fun <A,B,C> ((A, B) -> C).curry(): (A) -> (B) -> C = fun(a: A) = fun(b: B) = this(a, b)

infix fun <A,B> ((A) -> B).`++++`(a: A): B = this(a)

// Exercise 4.4: Invokable
data class EmailTemplate(private val templateText: String): (Person) -> String {
  override fun invoke(aPerson: Person): String = this.templateText
      .replace("{familyName}",aPerson.familyName)
      .replace("{firstName}",aPerson.firstName)
}

