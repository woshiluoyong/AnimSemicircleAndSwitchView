package com.example.mytestapplication

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.ViewTreeObserver
import android.widget.RelativeLayout
import android.view.WindowManager
import java.util.*
import kotlin.math.hypot
import kotlin.properties.Delegates

class AcceleratorAnimViewGroup(context: Context, attr: AttributeSet? = null): RelativeLayout(context, attr) {
    private var screenWidth by Delegates.notNull<Int>()
    private var bgBitmap: Bitmap? = null
    private var maxLengthVal: Float? = 0f
    private var curProgressVal: Float? = 0f
    private var progressTimer: Timer? = null

    init {
        bgBitmap = getBitmapFromResId(context, R.drawable.background_world)
        screenWidth = getScreenSize(context)?.x ?: bgBitmap?.width ?: 0
        setWillNotDraw(false)

        curProgressVal = 0f

        this.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener{
            override fun onPreDraw(): Boolean {
                maxLengthVal = hypot(width.toDouble(), height.toDouble()).toFloat()
                this@AcceleratorAnimViewGroup.viewTreeObserver.removeOnPreDrawListener(this)
                return false
            }
        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val clipPath = Path()
        clipPath.addCircle(0f, height.toFloat(), curProgressVal!!, Path.Direction.CCW)

        canvas?.save()
        canvas?.clipPath(clipPath)

        canvas?.drawColor(Color.parseColor("#4E7AF6"))

        canvas?.restore()

        bgBitmap?.let {
            canvas?.drawBitmap(it, null, Rect(0, 0, screenWidth, (it.height.toFloat()/2f).toInt()), null)
        }

        canvas?.save()
        canvas?.clipPath(clipPath)

        val shaderRightPaint = Paint()
        val intRightAry = Array(11){it}
        val floatRightAry = Array(11){it.toFloat()}
        for(i in 0..10){
            intRightAry[i] = Color.argb(((10 - i) * (100f/10f)).toInt(), 78,122, 246)
            floatRightAry[i] = 0.1f * i
        }//end of for
        shaderRightPaint.shader = LinearGradient(screenWidth.toFloat(),0f, screenWidth.toFloat()/3f-50, 0f, intRightAry.toIntArray(), floatRightAry.toFloatArray(), Shader.TileMode.CLAMP)
        canvas?.drawRect(screenWidth.toFloat(), height.toFloat(), screenWidth.toFloat()/3f-50, 0f, shaderRightPaint)

        val shaderLeftPaint = Paint()
        val intLeftAry = Array(11){it}
        val floatLeftAry = Array(11){it.toFloat()}
        for(i in 0..10){
            intLeftAry[i] = Color.argb(((10 - i) * (255f/10f)).toInt(), 253,205, 198)
            floatLeftAry[i] = 0.1f * i
        }//end of for
        shaderLeftPaint.shader = LinearGradient(0f,0f, screenWidth.toFloat()/3f+150, 0f, intLeftAry.toIntArray(), floatLeftAry.toFloatArray(), Shader.TileMode.CLAMP)
        canvas?.drawRect(0f, 0f, screenWidth.toFloat()/3f+150, height.toFloat(), shaderLeftPaint)

        canvas?.restore()
    }

    fun setCurrentProgress(curVal: Int, stepVal: Float? = 1f){
        var curMaxLengthVal = if(curVal < 0){
            0f
        }else if(curVal > 100){
            maxLengthVal
        }else{
            maxLengthVal!! * (curVal.toFloat() / 100f)
        }
        checkStopProgressTimer()
        if(curMaxLengthVal != curProgressVal){
            val isIncrease = curMaxLengthVal!! > curProgressVal!!
            progressTimer = Timer()
            progressTimer?.schedule(object : TimerTask(){
                override fun run() {
                    curProgressVal = curProgressVal!! + (if(isIncrease) stepVal!! else -stepVal!!)
                    println("======progressTimer=============>$curMaxLengthVal====>$curProgressVal")
                    if(if(isIncrease) curProgressVal!! <= curMaxLengthVal!! else curProgressVal!! >= curMaxLengthVal!!){
                        postInvalidate()
                    }else{
                        curProgressVal = curMaxLengthVal
                        postInvalidate()
                        checkStopProgressTimer()
                    }
                }
            }, 1, 1)
        }//end of if
    }

    private fun checkStopProgressTimer(){
        progressTimer?.cancel()
        progressTimer = null
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

    private fun getScreenSize(context: Context): Point? {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return Point(wm.defaultDisplay.width, wm.defaultDisplay.height)
    }
}