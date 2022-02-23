package me.fang.kosh

import java.net.URL

internal inline fun <reified C> C.getResource(name: String): URL? = C::class.java.getResource(name)
