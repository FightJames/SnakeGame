package com.jamestech.snakegame.model

import android.util.Log
import java.util.*


// snake is composed by pointBean
class Snake {
    var points: Queue<Point> = LinkedList<Point>()
    val initX = 0
    val initY = 0
    var endX: Int = Integer.MAX_VALUE
    var endY: Int = Integer.MAX_VALUE
    private var move = 5

    fun moveLeft() {
        val priPoint = points.poll()
        Log.d("Left size ", points.size.toString())
        if (priPoint.x - move < initX) {
            points.add(Point(endX, priPoint.y))
            return
        }
        points.add(Point(priPoint.x - move, priPoint.y))
    }

    fun moveRight() {
        Log.d("Right size ", points.size.toString())
        val priPoint = points.poll()
        if (priPoint.x + move > endX) {
            points.add(Point(initX, priPoint.y))
            return
        }
        points.add(Point(priPoint.x + move, priPoint.y))
    }

    fun moveUp() {
        val priPoint = points.poll()
        if (priPoint.y - move < initY) {
            points.add(Point(priPoint.x, endY))
            return
        }
        points.add(Point(priPoint.x, priPoint.y - move))
    }

    fun moveDown() {
        val priPoint = points.poll()
        if (priPoint.y + move > endY) {
            points.add(Point(priPoint.x, initY))
            return
        }
        points.add(Point(priPoint.x, priPoint.y + move))
    }
}