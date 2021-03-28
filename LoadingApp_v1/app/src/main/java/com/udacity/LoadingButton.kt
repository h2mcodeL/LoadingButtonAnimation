package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import com.udacity.main.ButtonState
import kotlin.properties.Delegates

//these are constants for the button state during its click
private var buttonColor = 0
private var downloadButtonColor = 0
private var clickedButtonColor = 0
private var completeButtonColor = 0
private var loadingCircleColor = 0
var button_state = 0

private var loadingWidth = 0f


//JvmOverloads provides for substitute overloads from the default values.
/*
This constructor allows a Button subclass to use its own class-specific base style from
either a theme attribute or style resource when inflating. To see how the final value of
a particular attribute is resolved based on your inputs to this constructor, see
 View.View(Context, AttributeSet, int, int).
 */

class LoadingButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var widthSize = 0
    private var heightSize = 0
    private var buttonWidth = 0


    private var buttonBackgroundColor = 0
    private var buttonTextColor = 0

    //new code
    private var loadingWidth = 0f
    private var loadingAngle = 0f

    //for the circle
    private var radius = 0f
    private var buttonText = ""

    private var circleAnimator = ValueAnimator()
    private var buttonAnimator = ValueAnimator()


    private var buttonPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = resources.getDimension(R.dimen.default_text_size)
        typeface = Typeface.create("", Typeface.BOLD)
    }

    //this is used for the animation
    private val paintCircle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = context.getColor(R.color.circleColor)        //(R.color.colorAccent)
    }

    //this is the text in the button
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textSize = resources.getDimension(R.dimen.default_text_size)
        color = context.getColor(R.color.white)
        color = Color.WHITE
    }


    var sweepAngle = 0.0f

    //work out the state of the button
    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed)
    { p, old, new ->

        // when the new state is true, what happens with property and old??
        when (new) {
            ButtonState.Loading -> {
                //change the button text
                buttonText = "Clicked.."

                //get the button animation set up
                buttonAnimator = ValueAnimator.ofInt(0, widthSize)
                        .apply {
                            duration = 2500 //3500
                            repeatMode = ValueAnimator.RESTART
                            repeatCount = 1
                            addUpdateListener {
                                buttonWidth = animatedValue as Int
                                invalidate()
                            }
                        }

                //move infront of the button, and set up the download circle animation
                circleAnimator = ValueAnimator.ofFloat(0f, 360f)
                        .apply {
                            duration = 2500          //3500
                            repeatMode = ValueAnimator.REVERSE
                            repeatCount = ValueAnimator.INFINITE
                            interpolator = AccelerateInterpolator(1f)
                            addUpdateListener { //animation ->
                                loadingAngle = animatedValue as Float
                                invalidate()
                            }
                        }

                //get animations going
                circleAnimator.start()
                buttonAnimator.start()
            }
            //set up for button complete state
            ButtonState.Completed -> {
                buttonText = resources.getString(R.string.download_ready)
                buttonWidth = 0
                circleAnimator.end()
                buttonAnimator.end()
            }
            else -> {
                ButtonState.Clicked
                buttonText = "Download.."
                //buttonWidth = 0

            }
        }
    }


    override fun performClick(): Boolean {
        if (super.performClick()) return true

        when (buttonState) {
            buttonState -> ButtonState.Clicked
            buttonState -> ButtonState.Loading
            else -> ButtonState.Completed
            }
        invalidate()
        return true

    }

    init {
        isClickable = true
        buttonText = "Download..."      //this text is shown on the starting state..
        buttonState = ButtonState.Clicked

        //set up the colors for the button and the text
        context.theme.obtainStyledAttributes(attrs, R.styleable.LoadingButton, 0, 0).apply {
            buttonBackgroundColor = getColor(R.styleable.LoadingButton_loadingButtonColor2, 0)
            buttonTextColor = getColor(R.styleable.LoadingButton_completeButtonColor3, 0)
        }}


//    override fun onSizeChanged(width: Int, height: Int, oldwidth: Int, oldheight: Int) {
//        super.onSizeChanged(width, height, oldwidth, oldheight)
//        //radius = (min(width, height) / 2.0 * 0.8).toFloat()
//            }

        override fun onDraw(canvas: Canvas?) {
            //button start colour
            buttonPaint.color = context.getColor(R.color.buttonColor)
            canvas?.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), buttonPaint)

            //button next colour
            buttonPaint.color = context.getColor(R.color.loadingColor)
            canvas!!.drawRect(0f, 0f, widthSize.toFloat() * buttonWidth / 100, heightSize.toFloat(), buttonPaint)
            //set the button background color for the start condition
            // paint.color = if(buttonState == ButtonState.Clicked) buttonColor else Color.GREEN (REMOVE)

            textPaint.textAlign = Paint.Align.CENTER

            canvas.drawText(
                    buttonText, widthSize.toFloat() / 2, heightSize / 1.7f, textPaint)

            canvas.drawArc(
                    widthSize - 160F, heightSize / 2 - 40f,
                    widthSize - 75f, heightSize / 2 + 40f,
                    0f, loadingAngle, true, paintCircle)
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
                View.MeasureSpec.getSize(w),
                heightMeasureSpec,
                0)
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }
}
