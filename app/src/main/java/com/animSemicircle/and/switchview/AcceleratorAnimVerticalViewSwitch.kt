package com.animSemicircle.and.switchview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Paint.FILTER_BITMAP_FLAG
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.util.*

class AcceleratorAnimVerticalViewSwitch : View {
    private var accState: AccState = AccState.AccWaiting//状态
    private var accLastState: AccState = accState//上一次状态

    sealed class AccState{
        object AccWaiting : AccState()
        object AccBooting : AccState()
        object Accelerating : AccState()
        object AccStoping : AccState()
    }

    private var thumbSize: Int = 0//滑块大小（直径)
    private var trackBgRadius: Int = 0//背景圆角半径
    private var isEnableTrackBorder: Boolean = true//是否开启背景边框

    private var trackOnPaint: Paint = Paint()//背景开启状态画笔
    private var trackOffPaint: Paint = Paint()//背景关闭状态画笔
    private var trackBorderPaint = Paint()//背景边框画笔
    private var trackArrowPaint: Paint = Paint()//箭头画笔
    private var thumbOneBgPaint: Paint = Paint()//滑块一层状态颜色画笔
    private var thumbTwoBgPaint: Paint = Paint()//滑块二层状态颜色画笔
    private var thumbOnTxtPaint: Paint = Paint()//滑块开启状态文本画笔
    private var thumbOffTxtPaint: Paint = Paint()//滑块关闭状态文本画笔
    private var trackPath: Path = Path()//背景绘制路径
    private var thumbPath: Path = Path()//滑块绘制路径
    private var trackBorderPath: Path = Path()//背景边框路径
    private var trackRectF = RectF()//背景绘制范围
    private var thumbOffCenterX: Float = 0f//滑块关闭时，绘制中心X轴
    private var thumbOnCenterX: Float = 0f//滑块开启时，绘制中心X轴
    private var thumbCenterX: Float = 0f//滑块所在X轴
    private var thumbTotalOffset: Float = 0f//滑块可以移动的总距离
    private var thumbOffsetParent: Float = 0f//滑动移动距离的百分比，用于处理滑动过渡动画
    private var thumbAnimatorDuration: Int = 300//过渡动画时长
    private var bgAlpha: Int = 0//块和背景透明度，用于过渡动画计算，使动画过渡更自然
    private val thumbAnimator: ValueAnimator by lazy {
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.duration = thumbAnimatorDuration.toLong()
        valueAnimator.addUpdateListener {
            //it.animatedFraction//动画当前播放进度
            thumbOffsetParent = it.animatedValue as Float
            bgAlpha = ((it.animatedValue as Float) * 255).toInt()
            postInvalidate()
        }
        valueAnimator
    }//滑块动画处理对象
    private var switchBitmap: Bitmap? = null
    //旋转动画相关
    private var rotateBitmap: Bitmap? = null
    private var rotateDegrees = 0f
    private var rotateTimer: Timer? = null
    //箭头动画相关
    private val arrowColorAry = arrayOf(255, 77)
    private var arrowLightFlag = 0
    private var arrowTimer: Timer? = null

    @JvmOverloads
    constructor(context: Context?) : this(context, null)

    @JvmOverloads
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    @JvmOverloads
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        //初始化背景画笔
        trackOnPaint.style = Paint.Style.FILL
        trackOnPaint.strokeJoin = Paint.Join.ROUND
        trackOnPaint.strokeCap = Paint.Cap.ROUND
        //trackOnPaint.color = Color.parseColor("#0D0E0E0E")//开启状态背景颜色
        trackOnPaint.isAntiAlias = true
        trackOnPaint.isDither = true

        trackOffPaint.style = Paint.Style.FILL
        trackOffPaint.strokeJoin = Paint.Join.ROUND
        trackOffPaint.strokeCap = Paint.Cap.ROUND
        //trackOffPaint.color = Color.parseColor("#0D0E0E0E")//关闭状态背景颜色
        trackOffPaint.isAntiAlias = true
        trackOffPaint.isDither = true

        trackArrowPaint.style = Paint.Style.STROKE
        trackArrowPaint.strokeWidth = dip2px(context, 4.5f).toFloat()
        trackArrowPaint.strokeJoin = Paint.Join.ROUND
        trackArrowPaint.strokeCap = Paint.Cap.ROUND
        trackArrowPaint.color = Color.WHITE//关闭状态背景颜色
        trackArrowPaint.isAntiAlias = true
        trackArrowPaint.isDither = true

        thumbOneBgPaint.isAntiAlias = true
        thumbOneBgPaint.isDither = true
        thumbOneBgPaint.style = Paint.Style.FILL
        thumbOneBgPaint.strokeJoin = Paint.Join.ROUND
        thumbOneBgPaint.strokeCap = Paint.Cap.ROUND

        thumbTwoBgPaint.isAntiAlias = true
        thumbTwoBgPaint.isDither = true
        thumbTwoBgPaint.style = Paint.Style.FILL
        thumbTwoBgPaint.strokeJoin = Paint.Join.ROUND
        thumbTwoBgPaint.strokeCap = Paint.Cap.ROUND

        trackBorderPaint.isAntiAlias = true
        trackBorderPaint.isDither = true
        trackBorderPaint.style = Paint.Style.STROKE
        trackBorderPaint.strokeWidth = dip2px(context, 3f).toFloat()
        trackBorderPaint.strokeJoin = Paint.Join.ROUND
        trackBorderPaint.strokeCap = Paint.Cap.ROUND
        trackBorderPaint.color = Color.parseColor("#FF41D158")//边框颜色

        thumbOnTxtPaint.isAntiAlias = true
        thumbOnTxtPaint.isDither = true
        thumbOnTxtPaint.textSize = dip2px(context, 18f).toFloat()
        thumbOnTxtPaint.color = Color.parseColor("#FFFFFFFF")//on状态滑块文本颜色

        thumbOffTxtPaint.isAntiAlias = true
        thumbOffTxtPaint.isDither = true
        thumbOffTxtPaint.textSize = dip2px(context, 18f).toFloat()
        thumbOffTxtPaint.color = Color.parseColor("#FFFFFFFF")//off状态滑块文本颜色

        switchBitmap = getBitmapFromResId(context, R.drawable.icon_switch_btn)
        rotateBitmap = getBitmapFromResId(context, R.drawable.icon_rotate_btn)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = measureSize(widthMeasureSpec, dip2px(context, 150f))
        val height = measureSize(heightMeasureSpec, dip2px(context, 50f))
        thumbSize = width - dip2px(context, 12f * 2)
        trackBgRadius = height
        setMeasuredDimension(width, height)
    }

    private fun measureSize(measureSpec: Int, defaultSize: Int): Int {
        var result: Int
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)
        if (mode == MeasureSpec.EXACTLY) {
            result = size
        } else {
            result = defaultSize
            if (mode == MeasureSpec.AT_MOST) result = result.coerceAtMost(size)
        }
        return result
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //初始化背景绘制路径
        trackPath.reset()
        trackRectF.left = 0f
        trackRectF.right = width.toFloat()
        trackRectF.top = 0f
        trackRectF.bottom = height.toFloat()
        trackPath.addRoundRect(trackRectF, trackBgRadius.toFloat(), trackBgRadius.toFloat(), Path.Direction.CW)
        trackOnPaint.shader = LinearGradient(0f, 0f, 0f, height.toFloat(), Color.parseColor("#FF141519"), Color.parseColor("#FF272B36"), Shader.TileMode.CLAMP)
        trackOffPaint.shader = LinearGradient(0f, 0f, 0f, height.toFloat(), Color.parseColor("#FF141519"), Color.parseColor("#FF272B36"), Shader.TileMode.CLAMP)
        //初始化滑块默认路径
        val padding = (width - thumbSize) * 0.5f
        thumbOffCenterX = padding + thumbSize * 0.5f
        thumbOnCenterX = height - padding - thumbSize * 0.5f
        thumbCenterX = width * 0.5f
        thumbTotalOffset = height - padding - thumbOffCenterX - thumbSize * 0.5f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawFilter = PaintFlagsDrawFilter(0, ANTI_ALIAS_FLAG or FILTER_BITMAP_FLAG)
        onDrawToggleTrack(canvas)
        onDrawToggleTrackBorder(canvas)
        onDrawToggleThumb(canvas)
    }

    //绘制按钮背景
    private fun onDrawToggleTrack(canvas: Canvas) {
        canvas.save()
        canvas.drawPath(trackPath, trackOffPaint)
        canvas.drawPath(trackPath, trackOnPaint)
        canvas.restore()
    }

    //绘制按钮边框
    private fun onDrawToggleTrackBorder(canvas: Canvas) {
        if(!isEnableTrackBorder)return
        when (accState) {
            AccState.Accelerating -> {
                trackBorderPath.reset()
                trackBorderPath.addRoundRect(trackRectF.left + dip2px(context, 1f), trackRectF.top + dip2px(context, 1f), trackRectF.right - dip2px(context, 1f),
                    trackRectF.bottom - dip2px(context, 1f), trackBgRadius.toFloat(), trackBgRadius.toFloat(), Path.Direction.CW)
                canvas.save()
                canvas.drawPath(trackBorderPath, trackBorderPaint)
                canvas.restore()
            }
        }
    }

    //绘制按钮
    private fun onDrawToggleThumb(canvas: Canvas) {
        val centerY = thumbOffCenterX + thumbOffsetParent * thumbTotalOffset
        thumbPath.reset()
        val thumbRadius = thumbSize * 0.5f
        thumbPath.addCircle(thumbCenterX, centerY, thumbRadius, Path.Direction.CW)

        val thumbTwoRadius = thumbRadius * 0.6f
        when (accState) {
            AccState.AccWaiting, AccState.Accelerating -> {
                thumbOneBgPaint.alpha = 1
                thumbOneBgPaint.color = Color.parseColor(if(AccState.AccWaiting == accState) "#FF575764" else "#FF41D158")//滑块背景颜色
                canvas.drawPath(thumbPath, thumbOneBgPaint)
                canvas.save()
                thumbPath.reset()
                thumbPath.addCircle(thumbCenterX, centerY, thumbTwoRadius, Path.Direction.CW)
                thumbTwoBgPaint.shader = LinearGradient(thumbCenterX - thumbTwoRadius, centerY - thumbTwoRadius, thumbCenterX - thumbTwoRadius, centerY + thumbTwoRadius,
                    Color.parseColor("#FF26272C"), Color.parseColor("#FF585C64"), Shader.TileMode.CLAMP)
                canvas.drawPath(thumbPath, thumbTwoBgPaint)
                canvas.restore()
                switchBitmap?.let {
                    val rotateLen = thumbTwoRadius - dip2px(context, 14f)//图片显示长度的1/2
                    val startX = thumbCenterX - rotateLen
                    val startY = centerY - rotateLen
                    val matrix = Matrix()
                    matrix.setScale((rotateLen * 2f) / it.width.toFloat(), (rotateLen * 2f) / it.height.toFloat())
                    matrix.postTranslate(startX, startY)
                    canvas.drawBitmap(it, matrix, null)
                }
            }
            AccState.AccBooting, AccState.AccStoping -> {
                thumbOneBgPaint.alpha = bgAlpha
                thumbOneBgPaint.color = Color.parseColor("#FF575764")//滑块背景颜色
                canvas.drawPath(thumbPath, thumbOneBgPaint)
                canvas.save()
                thumbPath.reset()
                thumbPath.addCircle(thumbCenterX, centerY, thumbTwoRadius, Path.Direction.CW)
                thumbTwoBgPaint.shader = LinearGradient(thumbCenterX - thumbTwoRadius, centerY - thumbTwoRadius, thumbCenterX - thumbTwoRadius, centerY + thumbTwoRadius,
                    Color.parseColor("#FF26272C"), Color.parseColor("#FF585C64"), Shader.TileMode.CLAMP)
                canvas.drawPath(thumbPath, thumbTwoBgPaint)
                canvas.restore()
                rotateBitmap?.let {
                    val rotateLen = thumbTwoRadius - dip2px(context, 14f)//图片显示长度的1/2
                    val startX = thumbCenterX - rotateLen
                    val startY = centerY - rotateLen
                    val matrix = Matrix()
                    matrix.setScale((rotateLen * 2f) / it.width.toFloat(), (rotateLen * 2f) / it.height.toFloat())
                    matrix.postTranslate(startX, startY)
                    matrix.postRotate(rotateDegrees, thumbCenterX, centerY)
                    canvas.drawBitmap(it, matrix, null)
                }
                execRotateAnimTimer()
            }
        }//end of when

        when (accState) {
            AccState.AccWaiting -> {
                val startY = (centerY * 2f) + dip2px(context, 30f)
                var curArrowLightFlag = arrowLightFlag
                if(0 != arrowLightFlag % arrowColorAry.size)curArrowLightFlag = arrowColorAry.size - (arrowLightFlag % arrowColorAry.size)//调换哈顺序
                var lastArrowY = 0f
                for(i in arrowColorAry.indices){//向下箭头
                    val startRealY = startY + dip2px(context, 28f * i)
                    val halfWidth = dip2px(context, 16f + i.toFloat())
                    val path = Path()
                    path.moveTo( thumbCenterX - halfWidth, startRealY)
                    lastArrowY = startRealY + dip2px(context, 16f)
                    path.lineTo(thumbCenterX, lastArrowY)
                    path.lineTo( thumbCenterX + halfWidth, startRealY)
                    trackArrowPaint.color = Color.argb(arrowColorAry[((curArrowLightFlag + i) % arrowColorAry.size)], 255, 255, 255)
                    canvas.drawPath(path, trackArrowPaint)
                }//end of for
                val showTxtStr = "ON"
                canvas.drawText(showTxtStr, thumbCenterX - (thumbOnTxtPaint.measureText(showTxtStr) / 2f), lastArrowY + dip2px(context, 30f)
                        + (getStringPixelHeight(thumbOnTxtPaint, showTxtStr).toFloat() / 2f), thumbOnTxtPaint)
                execArrowAnimTimer()
            }
            AccState.Accelerating -> {
                val startY = centerY - thumbRadius - dip2px(context, 48f)
                var startRealY = 0f
                for(i in arrowColorAry.indices){//向上箭头
                    startRealY = startY - dip2px(context, 28f * i)
                    val halfWidth = dip2px(context, 16f + i.toFloat())
                    val path = Path()
                    path.moveTo(thumbCenterX - halfWidth, startRealY + dip2px(context, 16f))
                    path.lineTo(thumbCenterX, startRealY)
                    path.lineTo(thumbCenterX + halfWidth, startRealY + dip2px(context, 16f))
                    trackArrowPaint.color = Color.argb(arrowColorAry[arrowColorAry.size - 1 - ((arrowLightFlag + i) % arrowColorAry.size)], 255, 255, 255)
                    canvas.drawPath(path, trackArrowPaint)
                }//end of for
                val showTxtStr = "OFF"
                canvas.drawText(showTxtStr, thumbCenterX - (thumbOffTxtPaint.measureText(showTxtStr) / 2f), startRealY - dip2px(context, 18f)
                        - (getStringPixelHeight(thumbOffTxtPaint, showTxtStr).toFloat() / 2f), thumbOffTxtPaint)
                execArrowAnimTimer()
            }
        }//end of when
    }

    //箭头动画执行
    private fun execArrowAnimTimer(){
        if(null != arrowTimer)return
        arrowTimer = Timer()
        arrowTimer?.schedule(object : TimerTask(){
            override fun run() {
                arrowLightFlag += 1
                postInvalidate()
            }
        }, 1, 300)
    }

    //箭头动画取消
    private fun checkStopArrowTimer(){
        arrowTimer?.cancel()
        arrowTimer = null
        arrowLightFlag = 0
    }

    //旋转动画执行
    private fun execRotateAnimTimer(){
        if(null != rotateTimer)return
        rotateTimer = Timer()
        rotateTimer?.schedule(object : TimerTask(){
            override fun run() {
                if(rotateDegrees > 360)rotateDegrees = 0f
                postInvalidate()
                rotateDegrees += 1f
            }
        }, 1, 5)
    }

    //旋转动画取消
    private fun checkStopRotateTimer(){
        rotateTimer?.cancel()
        rotateTimer = null
    }

    //切换状态
    fun changeAccState(accState: AccState): Boolean {
        if(this.accState == accState)return false
        checkStopArrowTimer()
        checkStopRotateTimer()
        accLastState = this.accState
        this.accState = accState
        invalidate()
        if (thumbOffsetParent != 0f && thumbOffsetParent != 1f) {//如果还在做动画，则不允许点击
            return true
        }//end of if
        when(accState){//执行滑动动画
            AccState.AccWaiting -> {
                if(AccState.AccStoping != accLastState){
                    thumbAnimator.setFloatValues(1f, 0f)
                    thumbAnimator.start()
                }//end of if
            }
            AccState.AccBooting -> {
                if(AccState.Accelerating != accLastState){
                    thumbAnimator.setFloatValues(0f, 1f)
                    thumbAnimator.start()
                }//end of if
            }
            AccState.Accelerating -> {
                if(AccState.AccBooting != accLastState){
                    thumbAnimator.setFloatValues(0f, 1f)
                    thumbAnimator.start()
                }//end of if
            }
            AccState.AccStoping -> {
                if(AccState.AccWaiting != accLastState){
                    thumbAnimator.setFloatValues(1f, 0f)
                    thumbAnimator.start()
                }//end of if
            }
        }
        return true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        //如果还在做动画，则不允许点击
        if (thumbOffsetParent != 0f && thumbOffsetParent != 1f) return true
        when (event.action) {
            MotionEvent.ACTION_DOWN -> return true
            MotionEvent.ACTION_UP -> {
                if(AccState.AccBooting == accState || AccState.AccStoping == accState){
                    return false
                }else{
                    callOnClick()
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun dip2px(context: Context?, dpValue: Float): Int {
        if(null == context)return dpValue.toInt()
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    private fun getStringPixelHeight(strPaint: Paint, str: String): Int {
        val rect = Rect()
        strPaint.getTextBounds(str, 0, str.length, rect)
        return rect.height()
    }

    private fun getBitmapFromResId(context: Context?, resId: Int): Bitmap? {
        if(null == context)return null
        val opt = BitmapFactory.Options()
        opt.inPreferredConfig = Bitmap.Config.ARGB_4444
        opt.inPurgeable = true
        opt.inInputShareable = true
        val `is` = context.resources.openRawResource(resId)
        return BitmapFactory.decodeStream(`is`, null, opt)
    }
}