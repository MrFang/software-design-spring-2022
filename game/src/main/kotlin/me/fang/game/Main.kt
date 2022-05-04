package me.fang.game

import me.fang.game.entity.Entity
import me.fang.game.entity.Player
import me.fang.game.entity.Wall
import me.fang.game.map.generateMap
import org.hexworks.zircon.api.CP437TilesetResources
import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.application.AppConfig
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.graphics.Symbols
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.KeyboardEventType
import kotlin.concurrent.thread

typealias Coordinates = Pair<Int, Int>

const val roomSize = Config.roomRadius * 2 + 1
const val windowSize = roomSize + 2
val appConfig = AppConfig.newBuilder()
    .withSize(windowSize, windowSize)
    .withDefaultTileset(CP437TilesetResources.rexPaint16x16())
    .build()

fun main() {
    val p = Player()
    val m = generateMap()
    var state = GameState(
        listOf(p),
        mapOf(Pair(p, Pair(Config.roomRadius, Config.roomRadius))).plus(getRoomWalls()),
        m,
        m.getStartRoomCoordinates(),
    )

    val grid = SwingApplications.startTileGrid(appConfig)
    Screen.create(grid)

    grid.processKeyboardEvents(KeyboardEventType.KEY_PRESSED) { event, _ ->
        when (event.code) {
            KeyCode.UP -> p.handleKeyPressed(Direction.TOP)
            KeyCode.RIGHT -> p.handleKeyPressed(Direction.RIGHT)
            KeyCode.DOWN -> p.handleKeyPressed(Direction.BOTTOM)
            KeyCode.LEFT -> p.handleKeyPressed(Direction.LEFT)
            else -> {}
        }
    }

    state.coordinates.forEach { (_, coordinates) ->
        grid.draw(
            Tile.newBuilder().withCharacter(Symbols.MALE).build(),
            Position.create(coordinates.first, coordinates.second)
        )
    }

    while (true) {
        val gkdThread = thread(start = true) { Thread.sleep(250) }
        val mainThread = thread(start = true) {
            run {
                val actionQueue = state.entities.map { Pair(it, it.tick()) }

                val stateBuilder = GameState.GameStateBuilder()
                stateBuilder.entities = state.entities
                stateBuilder.map = state.map
                stateBuilder.currentRoom = state.currentRoom
                val coordinates = mutableMapOf<Entity, Coordinates>()

                state.coordinates.forEach { coordinates[it.key] = it.value } // Clone

                actionQueue
                    .filter { it.second is MoveAction }
                    .forEach { pair ->
                        run {
                            val c = newCoordinates(state.coordinates[pair.first]!!, pair.second as MoveAction)

                            if (!coordinates.any { it.value == c }) {
                                coordinates[pair.first] = c
                            }
                        }
                    }

                stateBuilder.coordinates = coordinates

                state = stateBuilder.build()

                grid.clear()
                state.coordinates.forEach { (_, coordinates) ->
                    grid.draw(
                        Tile.newBuilder().withCharacter('x').build(),
                        Position.create(coordinates.first, coordinates.second)
                    )
                }
            }
        }

        mainThread.join()
        gkdThread.join()
    }
}

private fun newCoordinates(coordinates: Coordinates, action: MoveAction): Coordinates = when (action.direction) {
    Direction.TOP -> Pair(coordinates.first, coordinates.second - 1)
    Direction.RIGHT -> Pair(coordinates.first + 1, coordinates.second)
    Direction.BOTTOM -> Pair(coordinates.first, coordinates.second + 1)
    Direction.LEFT -> Pair(coordinates.first - 1, coordinates.second)
}

private fun getRoomWalls(): Map<Entity, Coordinates> {
    val res = mutableMapOf<Entity, Coordinates>()

    for (i in 1..roomSize) {
        res[Wall()] = Pair(1, i)
        res[Wall()] = Pair(roomSize, i)
        res[Wall()] = Pair(i, 1)
        res[Wall()] = Pair(i, roomSize)
    }

    return res
}
