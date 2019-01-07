package com.androidtraining.conwaysgameoflife.util

import com.androidtraining.conwaysgameoflife.model.SquareCell
import java.lang.Exception

class EnvironmentArray : ArrayList<SquareCell>() {
    var columns: Int = 0
    var rows: Int = 0

    var somethingIsAlive = false
        private set

    fun getNumberNeighbors(cell: SquareCell): Int {
        //r-1,c-1	r-1,c	    r-1,c+1
        //r,c-1	    row,column	r,c+1
        //r+1,c-1	r+1,c	    r+1,c+1
        var total = 0

        for (r in -1..1)
            for (c in -1..1) {
                val currentNeighbor = get(cell.column + c, cell.row + r)

                if (currentNeighbor != null) {
                    if (currentNeighbor.isAlive && (c != 0 || r != 0))
                        total++
                }
            }

        return total
    }

    fun initializeEnvironment(columns: Int, rows: Int) {
        this.clear()
        for (i in 0 until rows) {
            for (j in 0 until columns) {
                try {
                    this.add(SquareCell().apply {
                        column = j
                        row = i
                    })
                } catch (e: Exception) {
                    print(e.message)
                }
            }
        }

        this.columns = columns
        this.rows = rows
    }

    fun get(column: Int, row: Int): SquareCell? {
        return if (column < this.columns && row < this.rows &&
            column >= 0 && row >= 0)
            this[getIndexFromCoordinates(column, row)]
        else
            null
    }

    private fun getIndexFromCoordinates(column: Int, row: Int): Int {
        return (column + row) + ((columns - 1) * row)
    }

    fun calculateNextGeneration() {
        // Rules:
        //  1. Any live cell with fewer than two live neighbors dies, as if by underpopulation.
        //  2. Any live cell with two or three live neighbors lives on to the next generation.
        //  3. Any live cell with more than three live neighbors dies, as if by overpopulation.
        //  4. Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.

        somethingIsAlive = false

        for (cell in this) {
            val numberNeighborhoods = getNumberNeighbors(cell)

            if (cell.isAlive) {
                cell.isAliveNew = when {
                    numberNeighborhoods < 2 -> false  // underpopulation die
                    numberNeighborhoods in (2..3) -> {
                        somethingIsAlive = true
                        true    // live
                    }
                    else -> false  // overpopulation die
                }
            } else {
                cell.isAliveNew = when (numberNeighborhoods) {
                    3 -> true // reproduction live
                    else -> false // not live then still die
                }
            }
        }
    }
}