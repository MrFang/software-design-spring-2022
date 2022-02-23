package me.fang.kosh.process

/**
 * Общий интерфейс для всего, что можно запусукать
 * @property args список аргументов процесса вместе с именем команды, если таковое есть
 */
interface KoshProcess {
    val args: List<String>

    /**
     * Запускает процесс
     * @param stdin Стандартный ввод
     * @return Станлартный вывод
     */
    fun run(stdin: String = ""): String
}
