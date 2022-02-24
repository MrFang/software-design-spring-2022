package me.fang.kosh.process.commands

import me.fang.kosh.Environment
import me.fang.kosh.getResource
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PwdTest {
    @Test
    fun pwdTest() {
        assertEquals("${Environment.cwd}\n", Pwd(listOf("pwd")).run())
        assertEquals("${Environment.cwd.toRealPath()}\n", Pwd(listOf("pwd", "-P")).run())
        assertEquals(
            getResource("/messages/commands-help/pwd.txt")?.readText(),
            Pwd(listOf("pwd", "--help")).run()
        )
        assertEquals(
            getResource("/messages/commands-help/pwd.txt")?.readText(),
            Pwd(listOf("pwd", "--help", "-L")).run()
        )
        assertEquals("${Environment.cwd}\n", Pwd(listOf("pwd", "-PL")).run())
        assertEquals("${Environment.cwd.toRealPath()}\n", Pwd(listOf("pwd", "-PL", "-LP")).run())
    }
}
