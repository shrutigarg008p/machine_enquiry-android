package com.machine.machine.util

import android.graphics.*
import android.graphics.drawable.Drawable


internal class ProgressDrawable(private val mForeground: Int, private val mBackground: Int) :
    Drawable() {
    private val mPaint: Paint = Paint()
    private val mSegment = RectF()
    override fun onLevelChange(level: Int): Boolean {
        invalidateSelf()
        return true
    }

    override fun draw(canvas: Canvas) {
        val cornerRadius = 5f
        val level = level / 10000f
        val b: Rect = bounds
        val gapWidth: Float = b.height() / 2f
        val segmentWidth: Float = (b.width() - (NUM_SEGMENTS - 0) * gapWidth) / NUM_SEGMENTS
        mSegment[0f, 0f, segmentWidth + 10] = b.height().toFloat()
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mForeground)

        for (i in 0 until NUM_SEGMENTS) {
            val loLevel = i / NUM_SEGMENTS.toFloat()
            val hiLevel = (i + 1) / NUM_SEGMENTS.toFloat()
            if (loLevel <= level && level <= hiLevel) {
                val middle = mSegment.left + NUM_SEGMENTS * segmentWidth * (level - loLevel)
                canvas.drawRect(mSegment.left, mSegment.top, middle, mSegment.bottom, mPaint)

                canvas.drawRoundRect(
                    RectF(mSegment.left, mSegment.top, middle, mSegment.bottom),
                    cornerRadius,
                    cornerRadius,
                    mPaint
                );
                mPaint.setColor(mBackground)
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(Color.WHITE);
                mPaint.setStrokeWidth(1F);

                canvas.drawRoundRect(
                    RectF(middle, mSegment.top, mSegment.right, mSegment.bottom),
                    cornerRadius,
                    cornerRadius,
                    mPaint
                );
                //  canvas.drawRect(middle, mSegment.top, mSegment.right, mSegment.bottom, mPaint)
            } else {
// stroke
                // Second rectangle

                canvas.drawRoundRect(mSegment, cornerRadius, cornerRadius, mPaint);
                // canvas.drawRect(mSegment, mPaint)
            }
            mSegment.offset(mSegment.width() + gapWidth, 0f)
        }
    }

    override fun setAlpha(alpha: Int) {}
    override fun setColorFilter(cf: ColorFilter?) {}
    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    companion object {
        private const val NUM_SEGMENTS = 10
    }
}