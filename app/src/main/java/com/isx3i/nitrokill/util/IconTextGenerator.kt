package com.isx3i.nitrokill.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import androidx.core.graphics.drawable.IconCompat
import java.util.Locale

object IconTextGenerator {

    private const val SIZE = 96

    fun forSpeed(bytesPerSecond: Long): IconCompat {
        val (value, unit) = shortLabel(bytesPerSecond)
        return render(value, unit)
    }

    private fun shortLabel(bytesPerSecond: Long): Pair<String, String> {
        val kbps = bytesPerSecond / 1024.0

        return when {
            kbps < 1.0 -> "0" to "K"

            kbps < 1000.0 ->
                String.format(Locale.US, "%.0f", kbps) to "K"

            else ->
                String.format(Locale.US, "%.1f", kbps / 1024.0) to "M"
        }
    }

    private fun render(value: String, unit: String): IconCompat {
        val bitmap = Bitmap.createBitmap(
            SIZE,
            SIZE,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)

        // الرقم الرئيسي
        val numberPaint = Paint(
            Paint.ANTI_ALIAS_FLAG or Paint.SUBPIXEL_TEXT_FLAG
        ).apply {
            color = Color.WHITE
            typeface = Typeface.create(
                "sans-serif-condensed",
                Typeface.BOLD
            )
            textAlign = Paint.Align.CENTER

            // تكبير الرقم عموديًا مع الحفاظ على عرضه الضيق
            textSize = when {
                value.length >= 4 -> SIZE * 0.46f
                value.length == 3 -> SIZE * 0.52f
                else -> SIZE * 0.64f
            }
        }

        // الوحدة تبقى كما هي
        val unitPaint = Paint(
            Paint.ANTI_ALIAS_FLAG or Paint.SUBPIXEL_TEXT_FLAG
        ).apply {
            color = Color.WHITE
            typeface = Typeface.create(
                "sans-serif",
                Typeface.BOLD
            )
            textAlign = Paint.Align.CENTER
            textSize = SIZE * 0.24f
        }

        // رفع الرقم قليلًا حتى نستطيع تكبيره رأسيًا
        val numberY = SIZE * 0.55f

        // الوحدة تبقى في الأسفل
        val unitY = SIZE * 0.88f

        canvas.drawText(
            value,
            SIZE / 2f,
            numberY,
            numberPaint
        )

        canvas.drawText(
            unit,
            SIZE / 2f,
            unitY,
            unitPaint
        )

        return IconCompat.createWithBitmap(bitmap)
    }
}
