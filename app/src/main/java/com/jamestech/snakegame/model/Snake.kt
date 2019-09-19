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
        val newPoint = Point(points.last().x - snakeBodyWidth, points.last().y)
        points.poll()
        Log.d("Left size ", points.size.toString())
        if (newPoint.x < initX) {
            points.add(Point(endX, newPoint.y))
        } else {
            points.add(newPoint)
        }
        return hitBody(points.last())
    }

    fun moveRight(): Boolean {
        snakeWay = GameView.SnakeWay.RIGHT
        val newPoint = Point(points.last().x + snakeBodyWidth, points.last().y)
        points.poll()
        if (newPoint.x > endX) {
            points.add(Point(initX, newPoint.y))
        } else {
            points.add(newPoint)
        }
        return hitBody(points.last())
    }

    fun moveUp(): Boolean {
        snakeWay = GameView.SnakeWay.UP
        val newPoint = Point(points.last().x, points.last().y - snakeBodyWidth)
        points.poll()
        if (newPoint.y < initY) {
            points.add(Point(newPoint.x, endY - snakeBodyWidth))
        } else {
            points.add(newPoint)
        }
        return hitBody(points.last())
    }

    fun moveDown(): Boolean {
        snakeWay = GameView.SnakeWay.DOWN
        val newPoint = Point(points.last().x, points.last().y + snakeBodyWidth)
        points.poll()
        if (newPoint.y > endY) {
            points.add(Point(newPoint.x, initY))
        } else {
            points.add(newPoint)
        }
        return hitBody(points.last())
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

    fun hitBody(point: Point): Boolean {
        points.forEachIndexed { index, element ->
            if (index + 3 < points.size) {
                if (element.x <= point.x && point.x < element.x + snakeBodyWidth
                    && element.y <= point.y && point.y < element.y + snakeBodyWidth
                ) {
                    return true
                }
            } else {
                return@forEachIndexed
            }
        }
        return false
    }
}