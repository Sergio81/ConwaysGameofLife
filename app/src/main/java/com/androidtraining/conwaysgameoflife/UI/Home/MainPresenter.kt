package com.androidtraining.conwaysgameoflife.UI.Home

import com.androidtraining.conwaysgameoflife.util.EnvironmentArray
import java.util.*

class MainPresenter(view:MainContract.View) : MainContract.Presenter {
    private val environment = EnvironmentArray()
    private val view = view

    private fun IntRange.random() = Random().nextInt((endInclusive + 1) - start) + start

    override fun activateRandomCells(activeCells: Int) {
        if(environment.columns > 0 && environment.rows > 0) {
            var remainingCells = activeCells

            while (remainingCells > 0) {
                val cell = environment.get((0 until environment.columns).random(), (0 until environment.rows).random())

                if (cell.isDead) {
                    cell.isDead = false
                    remainingCells--
                    view.updateCell(cell)
                }
            }
        }
    }

    override fun initializeEnvironment(columns: Int, rows: Int) {
        environment.initializeEnviorment(columns, rows)
    }

    override fun PlayGame() {
        // Rules:
        //  1. Any live cell with fewer than two live neighbors dies, as if by underpopulation.
        //  2. Any live cell with two or three live neighbors lives on to the next generation.
        //  3. Any live cell with more than three live neighbors dies, as if by overpopulation.
        //  4. Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.

        
    }
}