package com.androidtraining.conwaysgameoflife.UI.Home

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.androidtraining.conwaysgameoflife.R
import com.androidtraining.conwaysgameoflife.UI.VisualGrid.VisualGridView
import com.androidtraining.conwaysgameoflife.model.SquareCell
//import com.androidtraining.conwaysgameoflife.R.id.gridGame
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : MainContract.View, AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val presenter: MainContract.Presenter = MainPresenter(this)
    private lateinit var gridGame: VisualGridView

    override fun updateCell(cell: SquareCell) {
        gridGame.activateCell(cell.column, cell.row, !cell.isDead)
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

        gridGame = findViewById(R.id.gridGame)

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
                gridGame.clearCells()
                presenter.initializeEnvironment(gridGame.columns, gridGame.rows)
                presenter.activateRandomCells(Math.round((20 * gridGame.cellsCount) / 100.0).toInt())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
