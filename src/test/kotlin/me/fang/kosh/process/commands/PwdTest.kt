package me.fang.kosh.process.commands

import me.fang.kosh.Environment
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PwdTest {
    @Test
    fun pwdTest() {
        assertEquals(Environment.cwd.toString(), Pwd(listOf("pwd")).run())
        assertEquals(Environment.cwd.toRealPath().toString(), Pwd(listOf("pwd", "-P")).run())
        assertEquals(
            this::class.java.getResource("/messages/commands-help/pwd.txt")?.readText(),
            Pwd(listOf("pwd", "--help")).run()
        )
        assertEquals(
            this::class.java.getResource("/messages/commands-help/pwd.txt")?.readText(),
            Pwd(listOf("pwd", "--help", "-L")).run()
        )
        assertEquals(Environment.cwd.toString(), Pwd(listOf("pwd", "-PL")).run())
        assertEquals(Environment.cwd.toRealPath().toString(), Pwd(listOf("pwd", "-PL", "-LP")).run())
    }
}
