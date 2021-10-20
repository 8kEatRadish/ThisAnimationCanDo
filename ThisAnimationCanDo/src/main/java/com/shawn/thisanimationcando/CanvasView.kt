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

    // 画图笔
    private var redPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    // 屏幕尺寸
    private var winSize: Point = Point(1000, 1000)

    // 坐标系原点
    private var originOfCoordinateSystem: Point = Point(0, 0)

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    init {
        loadWinSize(context, winSize)
        redPaint.color = Color.parseColor("#ff0000")
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
        canvas?.drawColor(Color.parseColor("#E0f6f5"))
        // TODO 绘制网格
        drawGrid(canvas, winSize, paint)
        // TODO 绘制坐标系
        drawOriginOfCoordinateSystem(canvas, originOfCoordinateSystem, winSize, paint)

        // 画圆弧
//        drawLikeCircle(canvas)
        // 画图片
        drawBitmap(canvas)
    }

    private fun drawBitmap(canvas: Canvas?) {
        val bitmap = BitmapFactory.decodeResource(resources,R.mipmap.xiaohei_img1)
//        val matrix = Matrix()
//        matrix.setValues(floatArrayOf(
//            1f,0.5f,100 * 3f,
//            0f,1f,100 * 3f,
//            0f,0f,3f
//        ))
//        canvas?.drawBitmap(bitmap,matrix,redPaint)
        val rectF = RectF(100 + 900f, 100f, 600 + 900f,400f)
        canvas?.drawBitmap(bitmap,null,rectF,redPaint)
    }

    private fun drawLikeCircle(canvas: Canvas?) {
        canvas?.apply {
            // 圆
            drawCircle(750f, 300f, 200f, redPaint)
            // 椭圆
            val rectF = RectF(100f, 100f, 500f, 300f)
            drawOval(rectF, redPaint)

            // 绘制圆弧(矩形边界,开始角度,扫过角度,使用中心?边缘两点与中心连线区域：边缘两点连线区域)
            val rectArc = RectF(100f + 950, 100f, 500f + 950, 300f)
            drawArc(rectArc, 0f, 90f, true, redPaint)

            // 绘制圆弧(矩形边界,开始角度,扫过角度,使用中心?边缘两点与中心连线区域：边缘两点连线区域)
            val rectArc2 = RectF(100f + 950 + 300, 100f, 500f + 950 + 300, 300f)
            drawArc(rectArc2, 0f, 90f, false, redPaint)
        }
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
            style = Paint.Style.FILL_AND_STROKE
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
        // X正轴文字
        for (i in 1 until (winSize.x - originOfCoordinateSystem.x) / 50) {
            paint.strokeWidth = 2f
            paint.textSize = 20f
            canvas.drawText(
                "${100 * i}",
                (originOfCoordinateSystem.x - 20 + 100 * i).toFloat(),
                (originOfCoordinateSystem.y + 40).toFloat(),
                paint
            )
            paint.strokeWidth = 5f
            canvas.drawLine(
                (originOfCoordinateSystem.x + 100 * i).toFloat(),
                originOfCoordinateSystem.y.toFloat(),
                (originOfCoordinateSystem.x + 100 * i).toFloat(),
                (originOfCoordinateSystem.y - 10).toFloat(),
                paint
            )
        }
        // x负轴文字
        for (i in 1 until originOfCoordinateSystem.x / 50) {
            paint.strokeWidth = 2f
            canvas.drawText(
                "${-100 * i}",
                (originOfCoordinateSystem.x - 20 - 100 * i).toFloat(),
                (originOfCoordinateSystem.y + 40).toFloat(),
                paint
            )
            paint.strokeWidth = 5f
            canvas.drawLine(
                (originOfCoordinateSystem.x - 100 * i).toFloat(),
                originOfCoordinateSystem.y.toFloat(),
                (originOfCoordinateSystem.x - 100 * i).toFloat(),
                (originOfCoordinateSystem.y - 10).toFloat(),
                paint
            )
        }
        // y正轴文字
        for (i in 1 until (winSize.y - originOfCoordinateSystem.y) / 50) {
            paint.strokeWidth = 2f
            canvas.drawText(
                "${100 * i}",
                (originOfCoordinateSystem.x + 20).toFloat(),
                (originOfCoordinateSystem.y + 10 + 100 * i).toFloat(),
                paint
            )
            paint.strokeWidth = 5f
            canvas.drawLine(
                originOfCoordinateSystem.x.toFloat(),
                (originOfCoordinateSystem.y + 100 * i).toFloat(),
                (originOfCoordinateSystem.x + 10).toFloat(),
                (originOfCoordinateSystem.y + 100 * i).toFloat(),
                paint
            )
        }
        // y负轴文字
        for (i in 1 until originOfCoordinateSystem.y / 50) {
            paint.strokeWidth = 2f
            canvas.drawText(
                "${-100 * i}",
                (originOfCoordinateSystem.x + 20).toFloat(),
                (originOfCoordinateSystem.y + 10 - 100 * i).toFloat(),
                paint
            )
            paint.strokeWidth = 5f
            canvas.drawLine(
                originOfCoordinateSystem.x.toFloat(),
                (originOfCoordinateSystem.y - 100 * i).toFloat(),
                (originOfCoordinateSystem.x + 10).toFloat(),
                (originOfCoordinateSystem.y - 100 * i).toFloat(),
                paint
            )
        }
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