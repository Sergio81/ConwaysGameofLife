package com.androidtraining.conwaysgameoflife.UI.VisualGrid

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class VisualGridView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    //region fields
    var columns: Int = 30
    var rows: Int = 0
    var autoColumns = true

    private var margin = 10f
    private var maxWidth = 0f
    private var maxHeight = 0f
    private lateinit var canvas: Canvas

    private var cells = mutableListOf<MutableList<Cell>>()
    private var recreateCells = true
    private var showGrid = false
    private var allowClick = false

    var cellWidth: Float = 0f
    var cellHeight: Float = 0f

    private var clicked = false
    private var clickX = 0f
    private var clickY = 0f

    private val lineStyle = Paint().apply {
        color = Color.BLACK
        strokeWidth = 3f
    }

    var cellsCount = 0
        private set
        get() {
            return columns * rows
        }

    //endregion

    init {
        if (attrs != null) {
            val nameSpace = "http://schemas.android.com/apk/res-auto"

            margin = attrs.getAttributeFloatValue(nameSpace, "margin", 10f)
            columns = attrs.getAttributeIntValue(nameSpace, "columns", 10)
            showGrid = attrs.getAttributeBooleanValue(nameSpace, "showGrid", true)
            allowClick = attrs.getAttributeBooleanValue(nameSpace, "allowClick", true)

            if (!autoColumns)
                rows = attrs.getAttributeIntValue(nameSpace, "rows", 10)
        }
    }

    class Cell {
        var column: Int = 0
        var row: Int = 0
        var x: Float = 0f
        var y: Float = 0f
        var width: Float = 0f
        var height: Float = 0f
        var isActive: Boolean = false

        fun pointIsInside(xPoint: Float, yPoint: Float): Boolean =
            (xPoint >= x && yPoint >= y) && (xPoint <= x + width && yPoint <= y + height)

        fun changeUIStatus(active: Boolean) {
            isActive = active
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        maxWidth = w.toFloat()
        maxHeight = h.toFloat()

        maxWidth -= 2 * margin
        maxHeight -= 2 * margin

        cellWidth = maxWidth / columns.toFloat()

        if (autoColumns)
            rows = Math.round(maxHeight / cellWidth)

        cellHeight = maxHeight / rows.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        this.canvas = canvas
        drawGrid()
    }

    private fun drawGrid() {
        canvas.apply {
            drawRect(
                margin,
                margin,
                maxWidth + margin,
                maxHeight + margin,
                Paint().apply { color = Color.WHITE })

            for (i in 0..rows) {
                var y = if (i == 0) margin else (cellHeight * i) + margin
                var rowOfCells = mutableListOf<Cell>()

                if (showGrid)
                    drawLine(margin, y, maxWidth + margin, y, lineStyle)

                if (recreateCells)
                    cells.add(rowOfCells)
                else
                    rowOfCells = cells[i]

                for (j in 0..columns) {
                    var x = if (j == 0) margin else (cellWidth * j) + margin

                    if (recreateCells) {
                        rowOfCells.add(Cell().apply {
                            column = j
                            row = i
                            this.x = x
                            this.y = y
                            width = cellWidth
                            height = cellHeight
                        })
                    } else if (rowOfCells[j].isActive) {
                        activateCell(j, i, true)
                    }

                    if (showGrid)
                        drawLine(x, margin, x, maxHeight + margin, lineStyle)
                }
            }
        }
        recreateCells = false
    }

    fun clearCells() {
        for (rowOfCells in cells)
            for (cell in rowOfCells)
                cell.isActive = false
        drawGrid()
    }

    fun activateCell(column: Int, row: Int, active: Boolean) {
        var x = if (column == 0) margin else (cellWidth * column) + margin
        var y = if (row == 0) margin else (cellHeight * row) + margin

        canvas.drawRect(x, y, x + cellWidth, y + cellHeight, Paint().apply {
            color = if (active) Color.BLACK else Color.WHITE
        })
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (allowClick) {
            if (event.action == MotionEvent.ACTION_DOWN) {
                clickX = event.x
                clickY = event.y
                clicked = true

                for (row: MutableList<Cell> in cells) {
                    for (column: Cell in row) {
                        if (column.pointIsInside(clickX, clickY)) {
                            column.changeUIStatus(!column.isActive)
                            break
                        }
                    }
                }
                invalidate()
            }
        }



        return super.onTouchEvent(event)
    }
}