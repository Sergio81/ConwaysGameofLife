package com.androidtraining.conwaysgameoflife.model

class SquareCell {
    var column:Int = 0
    var row:Int = 0
    var isAlive:Boolean = false
    var isAliveNew:Boolean = false

    override fun toString(): String {
        return "[R:$row, C:$column] ${if(isAlive) "isAlive" else ""}"
    }
}