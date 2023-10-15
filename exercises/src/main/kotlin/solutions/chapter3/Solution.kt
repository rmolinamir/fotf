package solutions.chapter3

// Exercise 3.3: Function as Result
fun buildCharAtPos(s: String): (Int) -> Char = { pos: Int -> s.elementAt(pos) }

// Exercise 3.4: Template Engine
data class StringTag(val text: String)

infix fun String.tag(value: String): Pair<String, StringTag> = Pair(this, StringTag(value))

fun renderTemplate(template: String, data: Map<String, StringTag>): String {
	return data.entries.fold(template) { updatedTemplate, entry ->
		updatedTemplate.replace(
			"{${entry.key}}",
			entry.value.text
		)
	}
}