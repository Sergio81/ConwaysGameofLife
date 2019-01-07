package com.androidtraining.conwaysgameoflife.UI.VisualGrid

import android.arch.lifecycle.LifecycleObserver
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.androidtraining.conwaysgameoflife.BuildConfig

class VisualGridView(context: Context, attrs: AttributeSet?) : View(context, attrs), LifecycleObserver {
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

    private var strokeGridWidth = 3f

    private var lineStyle = Paint().apply {
        color = Color.BLACK
        strokeWidth = strokeGridWidth
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

            if (!showGrid) {
                strokeGridWidth = 0f
                lineStyle = Paint().apply {
                    strokeWidth = strokeGridWidth
                }
            }

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

    private fun updateMeasures(w: Int, h: Int) {
        maxWidth = w.toFloat()
        maxHeight = h.toFloat()

        maxWidth -= 2 * margin
        maxHeight -= 2 * margin

        cellWidth = maxWidth / columns.toFloat()

        if (autoColumns)
            rows = Math.round(maxHeight / cellWidth)

        cellHeight = maxHeight / rows.toFloat()
    }

    private fun measureDimension(desiredSize: Int, measureSpec: Int): Int {
        var result: Int
        val specMode = View.MeasureSpec.getMode(measureSpec)
        val specSize = View.MeasureSpec.getSize(measureSpec)

        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = desiredSize
            if (specMode == View.MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize)
            }
        }

        if (result < desiredSize) {
            Log.e("ChartView", "The view is too small, the content might get cut")
        }
        return result
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.v("Chart onMeasure w", View.MeasureSpec.toString(widthMeasureSpec))
        Log.v("Chart onMeasure h", View.MeasureSpec.toString(heightMeasureSpec))

        val desiredWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val desiredHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        setMeasuredDimension(
            measureDimension(desiredWidth, widthMeasureSpec),
            measureDimension(desiredHeight, heightMeasureSpec)
        )
        updateMeasures(desiredWidth, desiredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        this.canvas = canvas
        updateMeasures(width, height)
        drawGrid()
    }

    private fun drawGrid() {
        val offset = strokeGridWidth / 2
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
                        //activateCell(j, i, true)
                        canvas.drawRect(
                            x + offset,
                            y + offset,
                            x + cellWidth - offset,
                            y + cellHeight - offset,
                            Paint().apply {
                                color = if (rowOfCells[j].isActive) Color.BLACK else Color.WHITE
                            })
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
        cells[row][column].isActive = active
        postInvalidate()
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