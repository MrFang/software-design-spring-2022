package me.fang.game.map

import me.fang.game.Config
import me.fang.game.Coordinates

internal const val MAX_RADIUS: Int = Config.mapRadius
internal const val SIZE = MAX_RADIUS * 2 + 1

class GameMap(val rooms: List<List<Room?>>) : java.io.Serializable {
    fun getStartRoomCoordinates(): Coordinates = Pair(Config.mapRadius, Config.mapRadius)
}
