package me.fang.game.entity

import me.fang.game.Action
import me.fang.game.NoopAction

class Wall() : Entity {
    override fun tick(): Action = NoopAction
}
