package me.fang.kosh.process

import me.fang.kosh.Environment

/**
 * Процесс -- присваивание переменной
 */
class VariableAssigment(override val args: List<String>) : KoshProcess {
    /**
     * Создаёт новую переменную и присваивает ей значение
     * или перезаписывает старое.
     *
     * Присваивание -- первый аргумент в args разбивается по первому =
     * Если передано больше аргументов следующие трактуются как команда и запускаются
     * @return Если нет последующей команды, возвращает пустой stdout
     * @see [processSingleCommand]
     */
    override fun run(stdin: String): String {
        val assigment = args[0].split("=", limit = 2)
        Environment.vars[assigment[0]] = assigment[1]

        if (args.size > 1) return processSingleCommand(args.drop(1))

        return "\n"
    }
}
