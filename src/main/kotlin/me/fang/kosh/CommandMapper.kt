package me.fang.kosh

import me.fang.kosh.process.ExternalProcess
import me.fang.kosh.process.KoshProcess
import me.fang.kosh.process.VariableAssignment
import me.fang.kosh.process.commands.Cat
import me.fang.kosh.process.commands.Echo
import me.fang.kosh.process.commands.Pwd
import me.fang.kosh.process.commands.Wc

interface CommandMapper {
    val commandMapping: Map<String, (List<String>) -> KoshProcess>
    val variableAssigmentConstructor : (List<String>) -> KoshProcess
    val externalProcessConstructor : (List<String>) -> KoshProcess
}

class DefaultCommandMapper : CommandMapper {
    override val commandMapping: Map<String, (List<String>) -> KoshProcess> = mapOf(
        Pair("pwd") { Pwd(it) },
        Pair("cat") { Cat(it) },
        Pair("echo") { Echo(it) },
        Pair("wc") { Wc(it) },
    )

    override val variableAssigmentConstructor : (List<String>) -> KoshProcess = { VariableAssignment(it) }
    override val externalProcessConstructor: (List<String>) -> KoshProcess = { ExternalProcess(it) }
}