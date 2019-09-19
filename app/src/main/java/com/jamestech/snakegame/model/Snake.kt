package com.jamestech.snakegame.model

import android.util.Log
import com.jamestech.snakegame.view.GameView
import java.util.*


// snake is composed by point
class Snake {
    var points: Queue<Point> = LinkedList<Point>()
    val initX = 0
    val initY = 0
    var endX: Int = Integer.MAX_VALUE
    var endY: Int = Integer.MAX_VALUE
    var snakeWay: GameView.SnakeWay = GameView.SnakeWay.LEFT
    val snakeBodyWidth = 30

    fun moveLeft(): Boolean {
        snakeWay = GameView.SnakeWay.LEFT
        points.poll()
        var newPoint = Point(points.last().x - snakeBodyWidth, points.last().y)
        if (newPoint.x < initX) {
            newPoint = Point(endX, newPoint.y)
        }
        val isHitBody = checkPointHitBody(newPoint)
        Log.d("left ", "${newPoint.toString()} ${points.last()}")
        points.add(newPoint)
        return isHitBody
    }

    fun moveRight(): Boolean {
        snakeWay = GameView.SnakeWay.RIGHT
        var newPoint = Point(points.last().x + snakeBodyWidth, points.last().y)
        points.poll()
        if (newPoint.x > endX) {
            newPoint = Point(initX, newPoint.y)
        }
        val isHitBody = checkPointHitBody(newPoint)
        points.add(newPoint)
        return isHitBody
    }

    fun moveUp(): Boolean {
        snakeWay = GameView.SnakeWay.UP
        var newPoint = Point(points.last().x, points.last().y - snakeBodyWidth)
        points.poll()
        if (newPoint.y < initY) {
            newPoint = Point(newPoint.x, endY - snakeBodyWidth)
        }
        val isHitBody = checkPointHitBody(newPoint)
        points.add(newPoint)
        return isHitBody
    }

    fun moveDown(): Boolean {
        snakeWay = GameView.SnakeWay.DOWN
        var newPoint = Point(points.last().x, points.last().y + snakeBodyWidth)
        points.poll()
        if (newPoint.y > endY) {
            newPoint = Point(newPoint.x, initY)
        }
        val isHitBody = checkPointHitBody(newPoint)
        points.add(newPoint)
        return isHitBody
    }

    fun growUpOneBody() {
        val point = points.last()
        val newPoint: Point
        when (snakeWay) {
            GameView.SnakeWay.DOWN -> {
                newPoint = Point(point.x, point.y + snakeBodyWidth)
            }
            GameView.SnakeWay.UP -> {
                newPoint = Point(point.x, point.y - snakeBodyWidth)
            }
            GameView.SnakeWay.RIGHT -> {
                newPoint = Point(point.x + snakeBodyWidth, point.y)
            }
            GameView.SnakeWay.LEFT -> {
                newPoint = Point(point.x - snakeBodyWidth, point.y)
            }
        }
        points.add(newPoint)
    }

    fun checkPointHitBody(point: Point): Boolean {
        points.forEachIndexed { index, element ->
            if (point.isPointOverLap(element, snakeBodyWidth)) {
                return true
            }
        }
        return false
    }

    fun eatPoint(point: Point): Boolean {
        val head = points.last()
        if (checkPointHitBody(point)) {
            growUpOneBody()
            return true
        }
        return false
    }
}