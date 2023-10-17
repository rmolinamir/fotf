package solutions.chapter4

import com.ubertob.fotf.exercises.chapter4.ToDoItem
import com.ubertob.fotf.exercises.chapter4.ToDoStatus
import com.ubertob.fotf.exercises.chapter4.extractListData
import com.ubertob.fotf.exercises.chapter4.fetchListContent
import com.ubertob.fotf.exercises.chapter4.renderHtml
import com.ubertob.fotf.exercises.chapter4.createResponse
import com.ubertob.fotf.exercises.chapter4.Request
import com.ubertob.fotf.exercises.chapter4.Response
import com.ubertob.fotf.exercises.chapter4.Person
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.contains
import strikt.assertions.isEqualTo
import java.net.URI


class E1SolutionTest {
	@Test
	fun `conditionally discards`() {
		val itemInProgress = ToDoItem("doing something",
			status= ToDoStatus.InProgress)
		val itemBlocked = ToDoItem("must do something",
			status= ToDoStatus.Blocked)

		expectThat(itemInProgress.discardUnless { status == ToDoStatus.InProgress }).isEqualTo(itemInProgress)

		expectThat(itemBlocked.discardUnless { status == ToDoStatus.InProgress }).isEqualTo(null)
	}
}


class E2SolutionTest {
	val processUnlessNull = ::extractListData andUnlessNull
			::fetchListContent andUnlessNull
			::renderHtml andUnlessNull
			::createResponse

	private fun fetchList(request: Request): Response = processUnlessNull(request)
		?: Response(404, "Not found")

	@Test
	fun `chain nullable happy path`() {
		val req = Request("GET", URI("http://example.com/zettai/uberto/mylist"), "")
		val resp = fetchList(req)

		expectThat(resp) {
			get { status }.isEqualTo(200)
			get { body }.contains("<td>uberto buy milk</td>")
			get { body }.contains("<td>complete mylist</td>")
			get { body }.contains("<td>something else</td>")
		}
	}

	@Test
	fun `wrong request`() {
		val req = Request("GET", URI("http://example.com/somethingelse"), "")
		val resp = fetchList(req)

		expectThat(resp) {
			get { status }.isEqualTo(404)
			get { body }.isEqualTo("Not found")
		}
	}

	@Test
	fun `wrong user`() {
		val req = Request("GET", URI("http://example.com/zettai/ub/mylist"), "")
		val resp = fetchList(req)

		expectThat(resp) {
			get { status }.isEqualTo(404)
			get { body }.isEqualTo("Not found")
		}
	}
}

class E3SolutionTest {
	private fun sum(num1: Int, num2: Int) = num1 + num2
	private fun strConcat(s1: String, s2: String) = "$s1 $s2"

	@Test
	fun `currying functions`() {
		val plus3Fn = ::sum.curry()(3)
		expectThat(plus3Fn(4)).isEqualTo(7)

		val starPrefixFn = ::strConcat.curry()("*")
		expectThat(starPrefixFn("abc")).isEqualTo("* abc")
	}


	@Test
	fun `infix currying`() {
		val curriedConcat = ::strConcat.curry()
		expectThat(curriedConcat `++++` "head" `++++` "tail")
			.isEqualTo("head tail")

		val curriedSum = ::sum.curry()
		expectThat(curriedSum `++++` 4 `++++` 5).isEqualTo(9)
	}
}

class E4SolutionTest {
	private val john = Person("John", "Smith")
	private val jane = Person("Jane", "Austen")

	@Test
	fun `correctly generate the email text`() {
		val templateText = "Dear {firstName}, ..."
		val emailTemplate = EmailTemplate(templateText)

		expectThat(emailTemplate(john)).isEqualTo("Dear John, ...")
		expectThat(emailTemplate(jane)).isEqualTo("Dear Jane, ...")
	}
}
