package com.androidtraining.conwaysgameoflife.util

import com.androidtraining.conwaysgameoflife.model.SquareCell
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class EnvironmentArrayTest {
    val array = EnvironmentArray()

    @Before
    fun setup(){
        array.initializeEnvironment(20,20)
    }

    @Test
    fun allCellsAlive() {
        var cell = array.get(2,2)

        if(cell != null){
            for(c in array)
                c.isAlive = true

            assertEquals(8, array.getNumberNeighbors(cell))
        }else{
            assertNull("cell is null", cell)
        }
    }

    @Test
    fun randomCellAlive(){
        var cell = array.get(0,1)

        array.get(1,0)?.isAlive = true
        array.get(0,1)?.isAlive = true

        if(cell != null){
            assertEquals(1, array.getNumberNeighbors(cell))
            assertEquals(1, array.getNumberNeighbors(cell))
        }else{
            assertNull("cell is null", cell)
        }
    }
}