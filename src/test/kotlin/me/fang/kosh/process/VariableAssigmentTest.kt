package me.fang.kosh.process

import me.fang.kosh.Environment
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class VariableAssigmentTest {
    @Test
    fun assigmentTest() {
        assertEquals("123", VariableAssigment(listOf("X=2", "echo", "-n", "123")).run())
        assertEquals("2", Environment.vars["X"])
        assertEquals("\n", VariableAssigment(listOf("X=2")).run())
    }
}
