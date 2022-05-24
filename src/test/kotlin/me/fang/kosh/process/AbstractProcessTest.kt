package me.fang.kosh.process

import me.fang.kosh.DefaultCommandMapper
import org.junit.jupiter.api.AfterEach
import java.io.ByteArrayOutputStream
import java.io.InputStream

abstract class AbstractProcessTest {
    protected val emptyIn = "".byteInputStream()
    protected val out = ByteArrayOutputStream()
    protected val err = ByteArrayOutputStream()
    protected val cli = DefaultCli(DefaultCommandMapper(), emptyIn, out, err)

    @AfterEach
    fun clearOut() {
        out.reset()
        err.reset()
    }

    protected fun createDefaultCliWithStdin(stdin: InputStream): Cli = DefaultCli(
        DefaultCommandMapper(),
        stdin,
        out,
        err
    )
}
