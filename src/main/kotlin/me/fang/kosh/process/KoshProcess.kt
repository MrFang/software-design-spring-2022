package me.fang.kosh.process

/**
 * Общий интерфейс для всего, что можно запускать
 * @property args список аргументов процесса вместе с именем команды, если таковое есть
 */
interface KoshProcess {
    val args: List<String>

    /**
     * Запускает процесс
     * @param cli Контекст CLI в котором бежит команда
     * @return Стандартный вывод
     */
    fun run(cli: Cli): Int
}
