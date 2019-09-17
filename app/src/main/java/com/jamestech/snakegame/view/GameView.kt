package com.jamestech.snakegame.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.jamestech.snakegame.model.Point
import com.jamestech.snakegame.model.Snake

class GameView : View, GestureDetector.OnGestureListener {
    override fun onShowPress(e: MotionEvent?) {
        Log.d("James snake", " onShowPress")
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        Log.d("James snake", " onSingleTapUp")
        return false
    }


    override fun onDown(e: MotionEvent?): Boolean {
        Log.d("James snake", " onDown")
        return true
    }


    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {

        Log.d("James snake", " onFling")
        if (e1 == null || e2 == null) {
            return false
        }
        val diffX = e2.x - e1.x
        val diffY = e2.y - e1.y
        if (Math.abs(diffX) > Math.abs(diffY)) {
            when {
                diffX > 0 -> {
                    if (snakeWay != SnakeWay.LEFT)
                        snakeWay = SnakeWay.RIGHT
                    Log.d("James way", " Right")
                }
                else -> {
                    if (snakeWay != SnakeWay.RIGHT)
                        snakeWay = SnakeWay.LEFT
                    Log.d("James way", " Left")
                }
            }
            return true
        }
        when {
            diffY > 0 -> {
                if (snakeWay != SnakeWay.UP)
                    snakeWay = SnakeWay.DOWN
                Log.d("James way", " Down")
            }
            else -> {
                if (snakeWay != SnakeWay.DOWN)
                    snakeWay = SnakeWay.UP
                Log.d("James way", " Up")
            }
        }
        return true
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        Log.d("James snake", " onShowPress")
        return false
    }

    override fun onLongPress(e: MotionEvent?) {
        Log.d("James snake", " onLongPress")
    }

    private var snakeWay: SnakeWay = SnakeWay.RIGHT
    private var isFailed = false
    private var paint: Paint = Paint()
    private val gestureDetector: GestureDetector by lazy {
        GestureDetector(this.context, this)
    }
    private val snake: Snake by lazy {
        Snake().apply {
            points.add(
                Point(
                    this@GameView.measuredWidth / 2,
                    this@GameView.measuredHeight / 2
                )
            )
        }
    }

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        snake.endX = this.measuredWidth
        snake.endY = this.measuredHeight
    }

    override fun onDraw(canvas: Canvas) {
        paint.color = Color.RED
        drawSnake(canvas)
        paint.color = Color.GREEN

        invalidate()
    }

    private fun drawSnake(canvas: Canvas) {
        when (snakeWay) {
            SnakeWay.UP -> snake.moveUp()
            SnakeWay.DOWN -> snake.moveDown()
            SnakeWay.LEFT -> snake.moveLeft()
            SnakeWay.RIGHT -> snake.moveRight()
        }
        snake.points.forEach {
            canvas.drawRect(
                it.x.toFloat(),
                it.y.toFloat(),
                (it.x + 30).toFloat(),
                (it.y + 30).toFloat(),
                paint
            )
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d("James snake", " touch event")
        return gestureDetector.onTouchEvent(event)
    }

    enum class SnakeWay {
        LEFT, RIGHT, UP, DOWN
    }
}