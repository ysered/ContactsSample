package com.ysered.contactssample.views

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.AttributeSet
import android.widget.ImageView
import com.ysered.contactssample.utils.getCircularBitmap


class ContactImageView(
        context: Context,
        attributeSet: AttributeSet?,
        defStyleAttr: Int
) : ImageView(context, attributeSet, defStyleAttr) {

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context) : this(context, null)

    private var centerX: Float = 0f
    private var centerY: Float = 0f
    private var centerRadius: Float = 0f
    private var textY: Float = 0f

    private val circlePaint = Paint().apply {
        isAntiAlias = true
        color = Color.LTGRAY // TODO: set random color
    }
    private val textPaint = Paint().apply {
        isAntiAlias = true
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        textSize = 64f // TODO: move to xml attributes
    }

    var firstLetter: String = " "
        set(value) {
            field = value
            invalidate()
        }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        if (uri != null) {
            val circularBitmap = (drawable as BitmapDrawable).bitmap.getCircularBitmap()
            setImageBitmap(circularBitmap)
        }
    }

    override fun onSizeChanged(newWidth: Int, newHeight: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight)
        centerX = (newWidth / 2).toFloat()
        centerY = (newHeight / 2).toFloat()
        centerRadius = (newWidth / 2).toFloat()

        val textBounds = Rect()
        textPaint.getTextBounds(firstLetter, 0, 1, textBounds)
        textY = height / 2f + textBounds.height() / 2f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (firstLetter.isNotBlank()) {
            canvas?.drawCircle(centerX, centerY, centerRadius, circlePaint)
            canvas?.drawText(firstLetter, centerX, textY, textPaint)
        }
    }
}
