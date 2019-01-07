package com.androidtraining.conwaysgameoflife.UI.Home

import com.androidtraining.conwaysgameoflife.model.SquareCell

interface MainContract {
    interface Presenter{
        fun calculateRandomCells(activeCells:Int)
        fun activateCurrentGeneration()
        fun initializeEnvironment(columns: Int, rows: Int)
        fun calculateNextGeneration()
        fun play()
    }
    interface View{
        fun updateCell(cell: SquareCell)
        fun updateTextGeneration(currentGeneration:Int)
    }
}