package com.isx3i.nitrokill.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import androidx.core.graphics.drawable.IconCompat
import java.util.Locale

object IconTextGenerator {

    // تكبير مساحة المؤشر قليلًا
    private const val SIZE = 115

    fun forSpeed(bytesPerSecond: Long): IconCompat {
        val (value, unit) = shortLabel(bytesPerSecond)
        return render(value, unit)
    }

    private fun shortLabel(bytesPerSecond: Long): Pair<String, String> {
        val kbps = bytesPerSecond / 1024.0

        return when {
            kbps < 1.0 -> "0" to "KB/s"

            kbps < 1000.0 ->
                String.format(Locale.US, "%.0f", kbps) to "KB/s"

            else ->
                String.format(Locale.US, "%.1f", kbps / 1024.0) to "MB/s"
        }
    }

    private fun render(value: String, unit: String): IconCompat {
        val bitmap = Bitmap.createBitmap(
            SIZE,
            SIZE,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)

        // ==========================================
        // الرقم الرئيسي
        // ==========================================
        val numberPaint = Paint(
            Paint.ANTI_ALIAS_FLAG or Paint.SUBPIXEL_TEXT_FLAG
        ).apply {
            color = Color.WHITE

            // خط ضيق حتى نستطيع تكبير الرقم عموديًا
            // بدون أن يصبح عريضًا جدًا
            typeface = Typeface.create(
                "sans-serif-condensed",
                Typeface.BOLD
            )

            textAlign = Paint.Align.CENTER

            // تكبير الرقم بشكل واضح
            textSize = when {
                value.length >= 4 -> SIZE * 0.50f
                value.length == 3 -> SIZE * 0.58f
                value.length == 2 -> SIZE * 0.70f
                else -> SIZE * 0.76f
            }
        }

        // ==========================================
        // الوحدة الموجودة تحت الرقم
        // ==========================================
        val unitPaint = Paint(
            Paint.ANTI_ALIAS_FLAG or Paint.SUBPIXEL_TEXT_FLAG
        ).apply {
            color = Color.WHITE

            typeface = Typeface.create(
                "sans-serif",
                Typeface.BOLD
            )

            textAlign = Paint.Align.CENTER

            // حجم الوحدة — تصغير قليل
            textSize = SIZE * 0.42f
        }

        // ==========================================
        // موضع الرقم
        // ==========================================
        // رفع الرقم قليلًا حتى لا يتداخل مع الوحدة
        val numberY = SIZE * 0.48f

        // ==========================================
        // موضع الوحدة
        // ==========================================
        val unitY = SIZE * 0.92f

        // رسم الرقم
        canvas.drawText(
            value,
            SIZE / 2f,
            numberY,
            numberPaint
        )

        // رسم الوحدة
        canvas.drawText(
            unit,
            SIZE / 2f,
            unitY,
            unitPaint
        )

        return IconCompat.createWithBitmap(bitmap)
    }
}
