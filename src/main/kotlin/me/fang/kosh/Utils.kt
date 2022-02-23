package me.fang.kosh

import java.net.URL

/**
 * Вспомогательная функция для получения ресурса по имени
 */
internal inline fun <reified C> C.getResource(name: String): URL? = C::class.java.getResource(name)
