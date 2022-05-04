package me.fang.game.map

import me.fang.game.Config
import kotlin.random.Random

fun generateMap(): GameMap {
    val init = mutableListOf<MutableList<Room?>>()

    for (i in 0 until SIZE) {
        init.add(mutableListOf())

        for (j in 0 until SIZE) {
            init[i].add(null)
        }
    }

    val startRoomCoordinates = Pair(MAX_RADIUS, MAX_RADIUS)
    init[startRoomCoordinates.first][startRoomCoordinates.second] = Room(RoomType.START)

    randomisedDfs(startRoomCoordinates, init, 0)

    return GameMap(init)
}

private fun randomisedDfs(
    currentRoomCoordinates: Pair<Int, Int>,
    preMap: MutableList<MutableList<Room?>>,
    stackDepth: Int,
) {
    if (stackDepth > Config.maxRoomsSequenceSize) {
        return
    }

    while (getNeighbours(currentRoomCoordinates).any { preMap[it.first][it.second] == null }) {
        val availableNeighbours = getNeighbours(currentRoomCoordinates).filter { preMap[it.first][it.second] == null }
        val next = availableNeighbours[Random.nextInt(availableNeighbours.size)]
        preMap[next.first][next.second] = Room(RoomType.COMMON)
        randomisedDfs(next, preMap, stackDepth + 1)
    }
}

private fun getNeighbours(pair: Pair<Int, Int>): List<Pair<Int, Int>> {
    val result = mutableListOf<Pair<Int, Int>>()

    if (pair.first + 1 < SIZE) {
        result.add(Pair(pair.first + 1, pair.second))
    }

    if (pair.first - 1 > 0) {
        result.add(Pair(pair.first - 1, pair.second))
    }

    if (pair.second + 1 < SIZE) {
        result.add(Pair(pair.first, pair.second + 1))
    }

    if (pair.second - 1 > 0) {
        result.add(Pair(pair.first, pair.second - 1))
    }

    return result
}
