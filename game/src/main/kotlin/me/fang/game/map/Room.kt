package me.fang.game.map

class Room(val type: RoomType) : java.io.Serializable

enum class RoomType {
    START,
    COMMON,
}
