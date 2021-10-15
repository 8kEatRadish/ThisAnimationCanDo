package com.shawn.thisanimationcando

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager

class CanvasView : View {
    // 网格画笔
    private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    // 屏幕尺寸
    private var winSize: Point = Point(1000, 1000)

    // 坐标系原点
    private var originOfCoordinateSystem: Point = Point(500, 500)

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    init {
        loadWinSize(context, winSize)
        originOfCoordinateSystem.x = winSize.x / 2 - ((winSize.x / 2) % 50)
        originOfCoordinateSystem.y = winSize.y / 2 - ((winSize.y / 2) % 50)
    }

    private fun loadWinSize(context: Context, winSize: Point) {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        windowManager.apply {
            defaultDisplay.getMetrics(outMetrics)
            winSize.x = outMetrics.widthPixels
            winSize.y = outMetrics.heightPixels
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // TODO 绘制网格
        drawGrid(canvas, winSize, paint)
        // TODO 绘制坐标系
        drawOriginOfCoordinateSystem(canvas, originOfCoordinateSystem, winSize, paint)
    }

    private fun drawOriginOfCoordinateSystem(
        canvas: Canvas?,
        originOfCoordinateSystem: Point,
        winSize: Point,
        paint: Paint
    ) {
        // 初始化网格画笔
        paint.apply {
            strokeWidth = 4f
            color = Color.BLACK
            style = Paint.Style.STROKE
            // 设置虚线效果
            pathEffect = null
        }
        canvas?.apply {
            // 绘制直线
            drawPath(originOfCoordinateSystemPath(originOfCoordinateSystem, winSize), paint)
            // 左箭头
            drawLine(
                winSize.x.toFloat(),
                originOfCoordinateSystem.y.toFloat(),
                (winSize.x - 40).toFloat(),
                (originOfCoordinateSystem.y - 20).toFloat(),
                paint
            )
            drawLine(
                winSize.x.toFloat(),
                originOfCoordinateSystem.y.toFloat(),
                (winSize.x - 40).toFloat(),
                (originOfCoordinateSystem.y + 20).toFloat(),
                paint
            )
            // 下箭头
            drawLine(
                originOfCoordinateSystem.x.toFloat(),
                winSize.y.toFloat(),
                (originOfCoordinateSystem.x - 20).toFloat(),
                (winSize.y - 40).toFloat(),
                paint
            )
            drawLine(
                originOfCoordinateSystem.x.toFloat(),
                winSize.y.toFloat(),
                (originOfCoordinateSystem.x + 20).toFloat(),
                (winSize.y - 40).toFloat(),
                paint
            )
            // 为坐标系绘制文字
            drawTextOriginOfCoordinateSystem(canvas, originOfCoordinateSystem, winSize, paint)
        }
    }

    private fun drawTextOriginOfCoordinateSystem(
        canvas: Canvas,
        originOfCoordinateSystem: Point,
        winSize: Point,
        paint: Paint
    ) {
        paint.textSize = 50f
        canvas.drawText(
            "x",
            (winSize.x - 60).toFloat(),
            (originOfCoordinateSystem.y - 40).toFloat(),
            paint
        )
        canvas.drawText(
            "y",
            (originOfCoordinateSystem.x - 40).toFloat(),
            (winSize.y - 60).toFloat(),
            paint
        )
    }

    private fun originOfCoordinateSystemPath(
        originOfCoordinateSystem: Point,
        winSize: Point
    ): Path {
        val path = Path()
        // x正半轴
        path.moveTo(originOfCoordinateSystem.x.toFloat(), originOfCoordinateSystem.y.toFloat())
        path.lineTo(winSize.x.toFloat(), originOfCoordinateSystem.y.toFloat())
        // x负半轴
        path.moveTo(originOfCoordinateSystem.x.toFloat(), originOfCoordinateSystem.y.toFloat())
        path.lineTo(
            (originOfCoordinateSystem.x - winSize.x).toFloat(),
            originOfCoordinateSystem.y.toFloat()
        )
        // y正半轴
        path.moveTo(originOfCoordinateSystem.x.toFloat(), originOfCoordinateSystem.y.toFloat())
        path.lineTo(originOfCoordinateSystem.x.toFloat(), winSize.y.toFloat())
        // y负半轴
        path.moveTo(originOfCoordinateSystem.x.toFloat(), originOfCoordinateSystem.y.toFloat())
        path.lineTo(
            originOfCoordinateSystem.x.toFloat(),
            (originOfCoordinateSystem.y - winSize.y).toFloat()
        )
        return path
    }

    private fun drawGrid(canvas: Canvas?, winSize: Point, gridPaint: Paint) {
        gridPaint.apply {
            strokeWidth = 2f
            color = Color.GRAY
            style = Paint.Style.STROKE
            pathEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
        }
        canvas?.drawPath(50.gridPath(winSize), gridPaint)
    }

    private fun Int.gridPath(winSize: Point): Path {
        val path = Path()
        for (index in 0..winSize.y step this) {
            path.moveTo(0f, index.toFloat())
            path.lineTo(winSize.x.toFloat(), index.toFloat())
        }
        for (index in 0..winSize.x step this) {
            path.moveTo(index.toFloat(), 0f)
            path.lineTo(index.toFloat(), winSize.y.toFloat())
        }
        return path
    }
}