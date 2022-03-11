package me.fang.kosh.exceptions

class UnexpectedTokenException(s: String) : IllegalStateException("Unexpected token: $s")
