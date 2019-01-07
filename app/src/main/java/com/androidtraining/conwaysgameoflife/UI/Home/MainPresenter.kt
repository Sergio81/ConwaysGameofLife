package com.androidtraining.conwaysgameoflife.UI.Home

import android.util.Log
import com.androidtraining.conwaysgameoflife.util.EnvironmentArray
import java.util.*


class MainPresenter(view: MainContract.View) : MainContract.Presenter {
    private val environment = EnvironmentArray()
    private val view = view
    private var generation = 0
    private fun IntRange.random() = Random().nextInt((endInclusive + 1) - start) + start

    override fun calculateRandomCells(activeCells: Int) {
        if (environment.columns > 0 && environment.rows > 0) {
            var remainingCells = activeCells

            while (remainingCells > 0) {
                val cell = environment.get((0 until environment.columns).random(), (0 until environment.rows).random())

                if (cell != null) {
                    if (!cell.isAliveNew) {
                        cell.isAliveNew = true
                        remainingCells--
                    }
                }
            }
        }
    }

    override fun activateCurrentGeneration() {
        for (c in environment) {
            c.isAlive = c.isAliveNew
            view.updateCell(c)
        }
        generation++
        view.updateTextGeneration(generation)
    }

    override fun initializeEnvironment(columns: Int, rows: Int) {
        environment.initializeEnvironment(columns, rows)
    }

    override fun calculateNextGeneration() {
        environment.calculateNextGeneration()
    }

    override fun play() {
        calculateNextGeneration()
        activateCurrentGeneration()
    }
}