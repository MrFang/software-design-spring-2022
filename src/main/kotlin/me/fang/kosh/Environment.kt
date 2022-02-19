package me.fang.kosh

import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.isDirectory

object Environment {
    var cwd: Path = Paths.get("").toAbsolutePath()
        set(value) {
            if (value.isDirectory()) {
                field = value.toAbsolutePath()
            }
        }
    var vars: MutableMap <String, String> = HashMap()
}
