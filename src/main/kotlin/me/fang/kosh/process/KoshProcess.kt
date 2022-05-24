package me.fang.kosh.process

import java.io.InputStream
import java.io.OutputStream

/**
 * Общий интерфейс для всего, что можно запускать
 * @property args список аргументов процесса вместе с именем команды, если таковое есть
 */
interface KoshProcess {
    val args: List<String>

    /**
     * Запускает процесс
     * @param stdin стандартный ввод
     * @return Стандартный вывод
     */
    fun run(stdin: InputStream, stdout: OutputStream, stderr: OutputStream): Int
}
