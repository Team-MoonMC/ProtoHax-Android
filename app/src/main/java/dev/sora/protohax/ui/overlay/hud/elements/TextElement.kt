package dev.sora.protohax.ui.overlay.hud.elements

import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.text.TextPaint
import dev.sora.protohax.MyApplication
import dev.sora.protohax.ui.overlay.hud.HudElement
import dev.sora.protohax.ui.overlay.hud.HudFont
import dev.sora.protohax.ui.overlay.hud.HudManager
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

class TextElement : HudElement(HudManager.TEXT_ELEMENT_IDENTIFIER) {

	private val textElementDefault = "Hello world!"

	private var textValue by stringValue("Text", textElementDefault).listen {
		this.width = paint.measureText(it)
		it
	}
	private var colorRedValue by intValue("ColorRed", 255, 0..255)
	private var colorGreenValue by intValue("ColorGreen", 255, 0..255)
	private var colorBlueValue by intValue("ColorBlue", 255, 0..255)
	private var textSizeValue by intValue("TextSize", 20, 10..50).listen {
		paint.textSize = it * MyApplication.density
		height = paint.fontMetrics.let { m -> m.descent - m.ascent }
		it
	}

	private val paint = TextPaint().also {
		it.color = Color.WHITE
		it.isAntiAlias = true
		it.textSize = 20 * MyApplication.density
	}

	override var height = paint.fontMetrics.let { it.descent - it.ascent }
		private set
	override var width = paint.measureText(textElementDefault)
		private set

	override fun onRender(canvas: Canvas, editMode: Boolean, needRefresh: AtomicBoolean, context: Context) {
		if(blurValue) {
			val blurMaskFilter = BlurMaskFilter(blurRadiusValue, blurModeValue.getBlurMode())
			paint.maskFilter = blurMaskFilter
		} else{
			paint.maskFilter = null
		}
		paint.typeface = fontValue.getFont(context)
		paint.setShadowLayer(shadowRadiusValue, 0f, 0f, Color.argb(shadowAlphaValue, 0, 0, 0))
		paint.color = Color.rgb(colorRedValue, colorGreenValue, colorBlueValue)

		canvas.drawText(textValue, 0f, -paint.fontMetrics.ascent, paint)
	}
}
