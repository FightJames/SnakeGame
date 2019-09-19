package com.jamestech.snakegame.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.jamestech.snakegame.model.Point
import com.jamestech.snakegame.model.Snake
import java.util.*
import kotlin.random.Random

class GameView : View, GestureDetector.OnGestureListener {
    private var paint: Paint = Paint()
    private var priSnakeWay: SnakeWay = SnakeWay.LEFT
    private val timer = Timer()
    private var randomPoint: Point? = null
    private val taskQueue: Queue<SnakeWay> by lazy {
        var list = LinkedList<SnakeWay>()
        list.add(SnakeWay.LEFT)
        list
    }
    private val gestureDetector: GestureDetector by lazy {
        GestureDetector(this.context, this)
    }

    val snake: Snake =
        Snake().apply {
            snakeWay = priSnakeWay
            points.add(
                Point(
                    snakeBodyWidth * 2,
                    0
                )
            )

            points.add(
                Point(
                    snakeBodyWidth,
                    0
                )
            )
            points.add(
                Point(
                    0,
                    0
                )
            )

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

        timer.schedule(object : TimerTask() {
            override fun run() {
                this@GameView.handler?.let {
                    it.post {
                        if (this@GameView.isVisible) {
                            invalidate()
                        }
                    }
                } ?: run {
                    timer.cancel()
                }
            }
        }, 500, 200)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        snake.endX = this.measuredWidth
        snake.endY = this.measuredHeight
    }


    override fun onDraw(canvas: Canvas) {
        if (randomPoint == null) {
            randomPoint = generateRandomPoint()
            while (snake.checkPointHitBody(randomPoint!!)) {
                randomPoint = generateRandomPoint()
            }
        }
        paint.color = Color.RED
        drawSnake(canvas)
        paint.color = Color.GREEN
        randomPoint?.apply {
            canvas.drawRect(
                x.toFloat(),
                y.toFloat(),
                (x + snake.snakeBodyWidth).toFloat(),
                (y + snake.snakeBodyWidth).toFloat(),
                paint
            )
        }
        Log.d("eat ", snake.eatPoint(randomPoint!!).toString())
        Log.d("eat ", "$randomPoint!!")
        if (snake.eatPoint(randomPoint!!)) {
            randomPoint = null
        }
    }

    fun generateRandomPoint(): Point =
        Point(
            Random.nextInt(0, measuredWidth - snake.snakeBodyWidth),
            Random.nextInt(0, measuredHeight - snake.snakeBodyWidth)
        )

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
            timer.cancel()
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