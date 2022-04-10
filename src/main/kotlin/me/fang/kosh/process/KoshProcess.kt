package me.fang.kosh.process

/**
 * Общий интерфейс для всего, что можно запускать
 * @property args список аргументов процесса вместе с именем команды, если таковое есть
 */
internal interface KoshProcess {
    val args: List<String>

    /**
     * Запускает процесс
     * @param stdin стандартный ввод
     * @return Стандартный вывод
     */
    fun run(stdin: String = ""): String
}
