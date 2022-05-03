package me.fang.game.entity

import me.fang.game.Action

interface Entity {
    fun tick(): Action
}
