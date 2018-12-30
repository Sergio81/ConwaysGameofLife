package com.androidtraining.conwaysgameoflife.UI.Home

import com.androidtraining.conwaysgameoflife.model.SquareCell

interface MainContract {
    interface Presenter{
        fun activateRandomCells(activeCells:Int)
        fun initializeEnvironment(columns: Int, rows: Int)
        fun PlayGame()
    }
    interface View{
        fun updateCell(cell: SquareCell)
    }
}