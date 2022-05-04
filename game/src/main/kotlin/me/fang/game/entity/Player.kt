package me.fang.game.entity

import me.fang.game.Action
import me.fang.game.Direction
import me.fang.game.MoveAction
import me.fang.game.NoopAction
import java.util.Queue

class Player() : Entity {
    private val queue: Queue<Action> = java.util.ArrayDeque()

    override fun tick(): Action = if (queue.isEmpty()) NoopAction else queue.poll()

    fun handleKeyPressed(direction: Direction) {
        queue.add(MoveAction(direction))
    }
}
