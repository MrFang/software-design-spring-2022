package me.fang.kosh.process

import me.fang.kosh.Environment
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class VariableAssignmentTest {
    @Test
    fun assignmentTest() {
        assertEquals("123", VariableAssignment(listOf("X=2", "echo", "-n", "123")).run(""))
        assertEquals("2", Environment.vars["X"])
        assertEquals("", VariableAssignment(listOf("X=2")).run(""))
    }
}
