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

class AcceleratorAnimViewSwitch : View {
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
    private var thumbOnBgPaint: Paint = Paint()//滑块开启状态颜色画笔
    private var thumbOffBgPaint: Paint = Paint()//滑块关闭状态颜色画笔
    private var thumbOnTxtPaint: Paint = Paint()//滑块开启状态文本画笔
    private var thumbOffTxtPaint: Paint = Paint()//滑块关闭状态文本画笔
    private var trackPath: Path = Path()//背景绘制路径
    private var thumbPath: Path = Path()//滑块绘制路径
    private var trackBorderPath: Path = Path()//背景边框路径
    private var trackRectF = RectF()//背景绘制范围
    private var thumbOffCenterX: Float = 0f//滑块关闭时，绘制中心X轴
    private var thumbOnCenterX: Float = 0f//滑块开启时，绘制中心X轴
    private var thumbCenterY: Float = 0f//滑块所在Y轴
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
    //旋转动画相关
    private var rotateBitmap: Bitmap? = null
    private var rotateDegrees = 0f
    private var rotateTimer: Timer? = null
    //箭头动画相关
    private val arrowColorAry = arrayOf(255, 153, 77)
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
        trackOnPaint.color = Color.parseColor("#0D0E0E0E")//开启状态背景颜色
        trackOnPaint.isAntiAlias = true
        trackOnPaint.isDither = true

        trackOffPaint.style = Paint.Style.FILL
        trackOffPaint.strokeJoin = Paint.Join.ROUND
        trackOffPaint.strokeCap = Paint.Cap.ROUND
        trackOffPaint.color = Color.parseColor("#0D0E0E0E")//关闭状态背景颜色
        trackOffPaint.isAntiAlias = true
        trackOffPaint.isDither = true

        trackArrowPaint.style = Paint.Style.STROKE
        trackArrowPaint.strokeWidth = dip2px(context, 2.5f).toFloat()
        trackArrowPaint.strokeJoin = Paint.Join.ROUND
        trackArrowPaint.strokeCap = Paint.Cap.ROUND
        trackArrowPaint.color = Color.WHITE//关闭状态背景颜色
        trackArrowPaint.isAntiAlias = true
        trackArrowPaint.isDither = true

        //初始化按钮画笔
        thumbOnBgPaint.isAntiAlias = true
        thumbOnBgPaint.isDither = true
        thumbOnBgPaint.style = Paint.Style.FILL
        thumbOnBgPaint.strokeJoin = Paint.Join.ROUND
        thumbOnBgPaint.strokeCap = Paint.Cap.ROUND
        thumbOnBgPaint.color = Color.WHITE//on状态滑块背景颜色

        thumbOffBgPaint.isAntiAlias = true
        thumbOffBgPaint.isDither = true
        thumbOffBgPaint.style = Paint.Style.FILL
        thumbOffBgPaint.strokeJoin = Paint.Join.ROUND
        thumbOffBgPaint.strokeCap = Paint.Cap.ROUND
        thumbOffBgPaint.color = Color.WHITE//off状态滑块背景颜色

        trackBorderPaint.isAntiAlias = true
        trackBorderPaint.isDither = true
        trackBorderPaint.style = Paint.Style.STROKE
        trackBorderPaint.strokeWidth = dip2px(context, 1f).toFloat()
        trackBorderPaint.strokeJoin = Paint.Join.ROUND
        trackBorderPaint.strokeCap = Paint.Cap.ROUND
        trackBorderPaint.color = Color.parseColor("#33FFFFFF")//边框颜色

        thumbOnTxtPaint.isAntiAlias = true
        thumbOnTxtPaint.isDither = true
        thumbOnTxtPaint.textSize = dip2px(context, 14f).toFloat()
        thumbOnTxtPaint.color = Color.parseColor("#FE7E7F")//on状态滑块文本颜色

        thumbOffTxtPaint.isAntiAlias = true
        thumbOffTxtPaint.isDither = true
        thumbOffTxtPaint.textSize = dip2px(context, 14f).toFloat()
        thumbOffTxtPaint.color = Color.parseColor("#CCCCCC")//off状态滑块文本颜色

        rotateBitmap = getBitmapFromResId(context, R.drawable.accelerate_animation)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = measureSize(widthMeasureSpec, dip2px(context, 150f))
        val height = measureSize(heightMeasureSpec, dip2px(context, 50f))
        thumbSize = height - dip2px(context, 6f)
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
        //初始化滑块默认路径
        var padding = (height - thumbSize) * 0.5f
        thumbOffCenterX = padding + thumbSize * 0.5f
        thumbOnCenterX = width - padding - thumbSize * 0.5f
        thumbCenterY = height * 0.5f
        thumbTotalOffset = width - padding - thumbOffCenterX - thumbSize * 0.5f
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
        trackBorderPath.reset()
        trackBorderPath.addRoundRect(trackRectF.left + dip2px(context, 1f), trackRectF.top + dip2px(context, 1f),
            trackRectF.right - dip2px(context, 1f), trackRectF.bottom - dip2px(context, 1f), trackBgRadius.toFloat(), trackBgRadius.toFloat(), Path.Direction.CW)
        canvas.save()
        canvas.drawPath(trackBorderPath, trackBorderPaint)
        canvas.restore()
    }

    //绘制按钮
    private fun onDrawToggleThumb(canvas: Canvas) {
        var centerX = thumbOffCenterX + thumbOffsetParent * thumbTotalOffset
        thumbPath.reset()
        val thumbRadius = thumbSize * 0.5f
        thumbPath.addCircle(centerX, thumbCenterY, thumbRadius, Path.Direction.CW)

        canvas.save()
        canvas.drawPath(thumbPath, thumbOffBgPaint)
        thumbOnBgPaint.alpha = bgAlpha
        canvas.drawPath(thumbPath, thumbOnBgPaint)
        canvas.restore()

        val startX = (thumbRadius * 2f) + dip2px(context, 15f)
        when (accState) {
            AccState.AccWaiting -> {
                val showTxtStr = "ON"
                canvas.drawText(showTxtStr, centerX - (thumbOffTxtPaint.measureText(showTxtStr) / 2f), thumbCenterY + (getStringPixelHeight(thumbOffTxtPaint, showTxtStr).toFloat() / 2f), thumbOffTxtPaint)
                var curArrowLightFlag = arrowLightFlag
                if(0 != arrowLightFlag % arrowColorAry.size)curArrowLightFlag = arrowColorAry.size - (arrowLightFlag % arrowColorAry.size)//调换哈顺序
                for(i in arrowColorAry.indices){//向右箭头
                    val startRealX = startX + dip2px(context, 28f * i)
                    val halfHeight = dip2px(context, 8f + i.toFloat())
                    val path = Path()
                    path.moveTo(startRealX, thumbCenterY - halfHeight)
                    path.lineTo(startRealX + dip2px(context, 10f), thumbCenterY)
                    path.lineTo(startRealX, thumbCenterY + halfHeight)
                    trackArrowPaint.color = Color.argb(arrowColorAry[((curArrowLightFlag + i) % arrowColorAry.size)], 255, 255, 255)
                    canvas.drawPath(path, trackArrowPaint)
                }//end of for
                execArrowAnimTimer()
            }
            AccState.AccBooting, AccState.AccStoping -> {
                rotateBitmap?.let {
                    val rotateLen = thumbRadius - dip2px(context, 5f)//图片显示长度的1/2
                    val startX = centerX - rotateLen
                    val startY = thumbCenterY - rotateLen
                    val matrix = Matrix()
                    matrix.setScale((rotateLen * 2f) / it.width.toFloat(), (rotateLen * 2f) / it.height.toFloat())
                    matrix.postTranslate(startX, startY)
                    matrix.postRotate(rotateDegrees, centerX, thumbCenterY)
                    canvas.drawBitmap(it, matrix, null)
                }
                execRotateAnimTimer()
            }
            AccState.Accelerating -> {
                val showTxtStr = "OFF"
                canvas.drawText(showTxtStr, centerX - (thumbOnTxtPaint.measureText(showTxtStr) / 2f), thumbCenterY + (getStringPixelHeight(thumbOnTxtPaint, showTxtStr).toFloat() / 2f), thumbOnTxtPaint)
                for(i in arrowColorAry.indices){//向左箭头
                    val startRealX = (startX / 3f) + dip2px(context, 28f * i)
                    val halfHeight = dip2px(context, 8f + i.toFloat())
                    val path = Path()
                    path.moveTo(startRealX + dip2px(context, 10f), thumbCenterY - halfHeight)
                    path.lineTo(startRealX, thumbCenterY)
                    path.lineTo(startRealX + dip2px(context, 10f), thumbCenterY + halfHeight)
                    trackArrowPaint.color = Color.argb(arrowColorAry[arrowColorAry.size - 1 - ((arrowLightFlag + i) % arrowColorAry.size)], 255, 255, 255)
                    canvas.drawPath(path, trackArrowPaint)
                }//end of for
                execArrowAnimTimer()
            }
        }
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