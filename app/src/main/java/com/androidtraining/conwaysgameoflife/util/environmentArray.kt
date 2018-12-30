package com.androidtraining.conwaysgameoflife.util

import com.androidtraining.conwaysgameoflife.model.SquareCell

class EnvironmentArray : ArrayList<SquareCell>() {
    var columns: Int = 0
    var rows: Int = 0

    fun isAlive(cell: SquareCell): Boolean {
        return false
    }

    private fun numberOfNeighbors(cell: SquareCell): Int {
        return 0
    }

    fun initializeEnviorment(columns: Int, rows: Int) {
        this.clear()
        for (i in 0 until columns) {
            for (j in 0 until rows) {
                this.add(SquareCell().apply {
                    column = i
                    row = j
                })
            }
        }

        this.columns = columns
        this.rows = rows
    }

    fun get(column:Int, row:Int):SquareCell{
        return this[(row * columns) + column]
    }
}