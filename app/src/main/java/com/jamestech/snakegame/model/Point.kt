package com.jamestech.snakegame.model

import android.util.Log

data class Point(var x: Int, var y: Int) {
    fun isPointOverLap(point: Point, width: Int): Boolean {
        val xOffset = Math.abs(x - point.x)
        val yOffset = Math.abs(y - point.y)
        Log.d("offset ", xOffset.toString())
        Log.d("offset ", "${this.x} ${point.x}")
        if (xOffset == 0 && yOffset < width)
            return true
        if (yOffset == 0 && xOffset < width)
            return true
        if (yOffset < width && xOffset < width && 0 < yOffset && 0 < xOffset)
            return true
        if (xOffset == 0 && yOffset == 0)
            return true
        return false
    }
}
