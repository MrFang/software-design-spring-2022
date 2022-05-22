package me.fang.kosh

import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.isDirectory

/**
 * Представление окружения команды
 * @property cwd текущая рабочая директория
 * @property vars переменные окружения
 */
internal object Environment {
    var cwd: Path = Paths.get("").toAbsolutePath()
        set(value) {
            if (value.isDirectory()) {
                field = value.toAbsolutePath()
            }
        }
    val vars: MutableMap <String, String> = HashMap()
}
