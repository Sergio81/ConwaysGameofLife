package com.androidtraining.conwaysgameoflife.UI.Home

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.androidtraining.conwaysgameoflife.R
import com.androidtraining.conwaysgameoflife.UI.VisualGrid.VisualGridView
import com.androidtraining.conwaysgameoflife.model.SquareCell
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import android.os.Handler
import java.util.*
import android.support.v4.os.HandlerCompat.postDelayed
import android.widget.TextView
import kotlin.concurrent.schedule


class MainActivity : MainContract.View, AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val presenter: MainContract.Presenter = MainPresenter(this)
    private lateinit var gridGame: VisualGridView
    private lateinit var mHandler: Handler
    private lateinit var txtGeneration: TextView

    override fun updateCell(cell: SquareCell) {
        runOnUiThread {
            gridGame.activateCell(cell.column, cell.row, cell.isAlive)
        }
    }

    override fun updateTextGeneration(currentGeneration: Int) {
        val tmpString = "Current Generation: $currentGeneration"
        txtGeneration.text = tmpString
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        txtGeneration = findViewById(R.id.txtGeneration)
        gridGame = findViewById(R.id.gridGame)
        mHandler = Handler()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                gridGame.clearCells()
                true
            }
            R.id.action_random_cells -> {
                showRandomGrid()
                true
            }
            R.id.action_play -> {
//                val runnable = object : Runnable {
//                    override fun run() {
//                        runOnUiThread {
//                            presenter.play()
//                        }
//                        mHandler.postDelayed(this, 3000)
//                    }
//                }
//
//                mHandler.postDelayed(runnable, 0)

                Timer("", false).schedule(0,500){
                    runOnUiThread {
                        presenter.play()
                    }
                }
                //presenter.play()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showRandomGrid() {
        gridGame.clearCells()
        presenter.initializeEnvironment(gridGame.columns, gridGame.rows)
        presenter.calculateRandomCells(Math.round((20 * gridGame.cellsCount) / 100.0).toInt())
        presenter.activateCurrentGeneration()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
