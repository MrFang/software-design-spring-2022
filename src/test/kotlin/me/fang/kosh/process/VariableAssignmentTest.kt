package me.fang.kosh.process

import me.fang.kosh.Environment
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class VariableAssignmentTest : AbstractProcessTest() {
    @Test
    fun assignmentWithCommandTest() {
        assertEquals(0, VariableAssignment(listOf("X=2", "echo", "-n", "123")).run(emptyIn, out, err))
        assertEquals(0, err.size())
        // TODO: Fix later when CLI abstraction will be added
        // assertEquals("123", out.toString(Charset.defaultCharset()))
        assertEquals("2", Environment.vars["X"])
    }

    @Test
    fun assigmentTest() {
        assertEquals(0, VariableAssignment(listOf("X=15")).run(emptyIn, out, err))
        assertEquals(0, err.size())
        assertEquals(0, out.size())
        assertEquals("15", Environment.vars["X"])
    }
}
