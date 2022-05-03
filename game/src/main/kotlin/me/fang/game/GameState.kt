package me.fang.game

import me.fang.game.entity.Entity
import me.fang.game.map.GameMap

class GameState(
    val entities: List<Entity>,
    val coordinates: Map<Entity, Coordinates>,
    val map: GameMap,
    val currentRoom: Coordinates
) {
    class GameStateBuilder {
        var entities: List<Entity>? = null
        var coordinates: Map<Entity, Coordinates>? = null
        var map: GameMap? = null
        var currentRoom: Coordinates? = null

        fun isValid(): Boolean =
            entities != null &&
                coordinates != null &&
                map != null &&
                currentRoom != null &&
                map!!.rooms[currentRoom!!.first][currentRoom!!.second] != null &&
                entities == coordinates!!.keys.toList()

        fun build(): GameState {
            assert(isValid())

            return GameState(entities!!, coordinates!!, map!!, currentRoom!!)
        }
    }
}
