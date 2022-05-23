package me.fang.kosh.process

import org.junit.jupiter.api.AfterEach
import java.io.ByteArrayOutputStream

abstract class AbstractProcessTest {
    protected val emptyIn = "".byteInputStream()
    protected val out = ByteArrayOutputStream()
    protected val err = ByteArrayOutputStream()

    @AfterEach
    fun clearOut() {
        out.reset()
        err.reset()
    }
}
