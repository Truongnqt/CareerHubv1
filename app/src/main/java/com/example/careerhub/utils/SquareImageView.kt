import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.widget.ImageView


class SquareImageView(context: Context?, attrs: AttributeSet?) :
    ImageView(context, attrs) {
    private var borderColor = 0
    private var borderWidth = 0

    private var bitmap: Bitmap? = null

    private var removeBorder = false

    override fun onDraw(canvas: Canvas) {
        val imageBitmap = bitmapResource
        val temporaryBitmap: Bitmap
        if (imageBitmap != null) {
            temporaryBitmap = imageBitmap.copy(Bitmap.Config.ARGB_8888, true)
        } else {
            return
        }

        val w = width

        val roundBitmap = getRoundedCroppedBitmap(cropBitmap(temporaryBitmap), w)
        canvas.drawBitmap(roundBitmap, 0f, 0f, null)
    }

    private val bitmapResource: Bitmap?
        get() {
            if (bitmap == null) {
                val drawable = drawable ?: return null

                if (width == 0 || height == 0) {
                    return null
                }

                return (drawable as BitmapDrawable).bitmap
            } else {
                return bitmap
            }
        }

    private fun cropBitmap(sourceBmp: Bitmap): Bitmap {
        val outputBmp = if (sourceBmp.width > sourceBmp.height) {
            Bitmap.createBitmap(sourceBmp, 0, 0, sourceBmp.height, sourceBmp.height)
        } else if (sourceBmp.width < sourceBmp.height) {
            Bitmap.createBitmap(sourceBmp, 0, 0, sourceBmp.width, sourceBmp.width)
        } else {
            sourceBmp
        }

        return outputBmp
    }

    private fun getRoundedCroppedBitmap(bitmap: Bitmap, radius: Int): Bitmap {
        val finalBitmap = if (bitmap.width != radius || bitmap.height != radius) {
            // Set the filter to false, because we don't need very smooth one! It's cheaper!
            Bitmap.createScaledBitmap(bitmap, radius, radius, false)
        } else {
            bitmap
        }

        val output =
            Bitmap.createBitmap(finalBitmap.width, finalBitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val paint = Paint()
        val rect = Rect(0, 0, finalBitmap.width, finalBitmap.height)

        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        paint.isDither = true

        canvas.drawARGB(0, 0, 0, 0)

        // It doesn't matter which color!
        paint.color = Color.WHITE
        canvas.drawRoundRect(
            RectF(
                0f,
                0f,
                finalBitmap.width.toFloat(),
                finalBitmap.height.toFloat()
            ), 15f, 15f, paint
        )

        // The second drawing should only be visible of if overlapping with the first
        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
        canvas.drawBitmap(finalBitmap, rect, rect, paint)

        // Draw the border, if exist
        if (!removeBorder) {
            canvas.drawRoundRect(
                RectF(0f, 0f, finalBitmap.width.toFloat(), finalBitmap.height.toFloat()), 15f, 15f,
                borderPaint
            )
        }



        return output
    }

    private val borderPaint: Paint
        get() {
            val borderPaint = Paint()
            if (borderColor != 0) {
                borderPaint.color = borderColor
            } else {
                borderPaint.color = Color.WHITE
            }
            if (borderWidth != 0) {
                borderPaint.strokeWidth = borderWidth.toFloat()
            } else {
                borderPaint.strokeWidth = DEFAULT_BOARDER_STROKE
            }

            borderPaint.style = Paint.Style.STROKE
            borderPaint.isAntiAlias = true

            return borderPaint
        }

    /**
     * A method to set the bitmap for the image view
     *
     * @param bmp The target bitmap
     */
    fun setBitmap(bmp: Bitmap?) {
        this.bitmap = bmp
    }

    /**
     * A method to se the color of the border of the image view
     *
     * @param colorResource The resource id of the favourite color
     */
    fun setBorderColor(colorResource: Int) {
        this.borderColor = colorResource
    }

    /**
     * A method to set the thickness of the border of the image view
     *
     * @param width The width of the border stroke in pixels
     */
    fun setBorderWidth(width: Int) {
        this.borderWidth = width
    }

    /**
     * A method to set whether show the image view with border or not
     *
     * @param removeBorder true to remove the border of the iamge view, otherwise it will have a border
     */
    fun removeBorder(removeBorder: Boolean) {
        this.removeBorder = removeBorder
    }

    companion object {
        private const val DEFAULT_BOARDER_STROKE = 5f
    }
}