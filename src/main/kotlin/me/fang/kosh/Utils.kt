package me.fang.kosh

fun String.applyEnv(): String {
    val vars = Environment.vars
    var result = this

    "\\$[a-zA-z_][a-zA-Z0-9_]*".toRegex() // $var_name
        .findAll(this)
        .forEach {
            run {
                val varName = it.value.drop(3)
                result = result.replace("\$$varName", vars[varName] ?: "")
            }
        }

    return result.trim()
}
