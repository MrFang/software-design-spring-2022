package me.fang.game

sealed interface Action

object NoopAction : Action

class MoveAction(val direction: Direction) : Action
