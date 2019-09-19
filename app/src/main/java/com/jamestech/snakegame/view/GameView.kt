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
import android.widget.Toast
import com.jamestech.snakegame.model.Point
import com.jamestech.snakegame.model.Snake
import java.util.*

class GameView : View, GestureDetector.OnGestureListener {
    private var isFailed = false
    private var paint: Paint = Paint()
    private var priSnakeWay: SnakeWay = SnakeWay.LEFT
    private val taskQueue: Queue<SnakeWay> by lazy {
        var list = LinkedList<SnakeWay>()
        list.add(SnakeWay.LEFT)
        list
    }
    private val gestureDetector: GestureDetector by lazy {
        GestureDetector(this.context, this)
    }

    val snake: Snake by lazy {
        Snake().apply {
            snakeWay = priSnakeWay
            points.add(
                Point(
                    this@GameView.measuredWidth / 2,
                    this@GameView.measuredHeight / 2
                )
            )

            points.add(
                Point(
                    this@GameView.measuredWidth / 2 + snakeBodyWidth,
                    this@GameView.measuredHeight / 2
                )
            )

            points.add(
                Point(
                    this@GameView.measuredWidth / 2 + snakeBodyWidth * 2,
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
        this.setOnClickListener {
            snake.growUpOneBody()
            Log.d("Hello", " click")
            false
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        snake.endX = this.measuredWidth
        snake.endY = this.measuredHeight
    }

    override fun onDraw(canvas: Canvas) {
        postDelayed({
            invalidate()
        }, 500)
        paint.color = Color.RED
        drawSnake(canvas)
        paint.color = Color.GREEN
    }

    private fun drawSnake(canvas: Canvas) {
        if (taskQueue.isEmpty().not()) {
            priSnakeWay = taskQueue.poll()
        }
        var execute = true
        when (priSnakeWay) {
            SnakeWay.UP -> {
                execute = snake.moveUp()
            }
            SnakeWay.DOWN -> {
                execute = snake.moveDown()
            }
            SnakeWay.LEFT -> {
                execute = snake.moveLeft()
            }
            SnakeWay.RIGHT -> {
                execute = snake.moveRight()
            }
        }
        snake.points.forEach {
            canvas.drawRect(
                it.x.toFloat(),
                it.y.toFloat(),
                (it.x + snake.snakeBodyWidth).toFloat(),
                (it.y + snake.snakeBodyWidth).toFloat(),
                paint
            )
        }
        if (execute) {
            Toast.makeText(this.context, "Game over", Toast.LENGTH_LONG).show()
            this.handler.removeCallbacksAndMessages(null)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d("James snake", " touch event")
        gestureDetector.onTouchEvent(event)
        return true
    }

    enum class SnakeWay {
        LEFT, RIGHT, UP, DOWN
    }

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
                    if (taskQueue.isEmpty().not() && taskQueue.last() != SnakeWay.LEFT) {
                        taskQueue.add(SnakeWay.RIGHT)
                    } else if (priSnakeWay != SnakeWay.LEFT) {
                        taskQueue.add(SnakeWay.RIGHT)
                    }
                    Log.d("James way", " Right")
                }
                else -> {
                    if (taskQueue.isEmpty().not() && taskQueue.last() != SnakeWay.RIGHT) {
                        taskQueue.add(SnakeWay.LEFT)
                    } else if (priSnakeWay != SnakeWay.RIGHT) {
                        taskQueue.add(SnakeWay.LEFT)
                    }
                    Log.d("James way", " Left")
                }
            }
            return true
        }
        when {
            diffY > 0 -> {
                if (taskQueue.isEmpty().not() && taskQueue.last() != SnakeWay.UP) {
                    taskQueue.add(SnakeWay.DOWN)
                } else if (priSnakeWay != SnakeWay.UP) {
                    taskQueue.add(SnakeWay.DOWN)
                }
                Log.d("James way", " Down")
            }
            else -> {
                if (taskQueue.isEmpty().not() && taskQueue.last() != SnakeWay.DOWN) {
                    taskQueue.add(SnakeWay.UP)
                } else if (priSnakeWay != SnakeWay.DOWN) {
                    taskQueue.add(SnakeWay.UP)
                }
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

}