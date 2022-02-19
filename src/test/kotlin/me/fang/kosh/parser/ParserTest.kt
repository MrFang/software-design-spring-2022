package me.fang.kosh.parser

import me.fang.kosh.parser.token.BareString
import me.fang.kosh.parser.token.DoubleQuotedString
import me.fang.kosh.parser.token.SingleQuotedString
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ParserTest {
    @Test
    fun basicParserTest() {
        assertEquals(Pair("+2", '1'), char { c -> c == '1' }.parse("1+2"))
        assertNull(char { c -> c == ' ' }.parse("1+2"))

        assertEquals(Pair("og", 'c'), char { c -> c == 'd' }.or(char { c -> c == 'c' }).parse("cog"))
        assertNull(char { c -> c == 'a' }.or(char { c -> c == 'b' }).parse("cog"))

        assertNull(
            bareString
                .then(bareString)
                .map { t -> t.s }
                .parse("hello world")
        )
        assertEquals(
            Pair("", "world"),
            bareString
                .then(space)
                .then(bareString)
                .map { t -> t.s }
                .parse("hello world")
        )

        assertEquals(
            Pair("", "16+27"),
            some(char { c -> c.isDigit() })
                .before(spaces)
                .and(plus)
                .map { (s1, c) -> s1.plus(c) }
                .before(spaces)
                .and(some(char { c -> c.isDigit() }))
                .map { (s1, s2) -> s1.plus(s2) }
                .map { s -> s.joinToString("") }
                .parse("16 + 27")
        )
    }

    @Test
    fun testTokens() {
        assertEquals(Pair(" hello", BareString("echo")), bareString.parse("echo hello"))
        assertNotEquals(Pair(" hello", BareString("echo")), bareString.parse("'echo' hello"))
        assertEquals(Pair(" hello", SingleQuotedString("echo")), singleQuotedString.parse("'echo' hello"))
        assertEquals(Pair(" hello", BareString("123-echo_")), bareString.parse("123-echo_ hello"))
        assertEquals(Pair("", DoubleQuotedString("echo hello")), doubleQuotedString.parse("\"echo hello\""))
    }

    @Test
    fun testCommands() {
        assertEquals(BareString("hello"), some(token).parse("echo hello")?.second?.get(1))
        assertNotNull(many(token).parse(""))
        assertEquals(2, commands.parse("cat file.txt | wc")?.second?.size)
    }
}
