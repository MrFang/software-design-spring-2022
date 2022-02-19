package me.fang.kosh

fun String.applyEnv(): String {
    // TODO: Process quoting
    val vars = Environment.vars
    var result = this

    "\$[a-zA-z_][a-zA-Z0-9_]*".toRegex() // $varname
        .findAll(this)
        .forEach {
            run {
                val varName = it.value.slice(1 until it.value.length)
                result = result.replace(varName, vars[varName] ?: "")
            }
        }

    return result.trim()
}
