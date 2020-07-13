package game.drawable

import game.screen.Drawable
import game.utils.expand
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Rectangle

class NoteBorder(
   baseRectangle: Rectangle,
   timeRate: Float
): Drawable {

   companion object {
      private const val MAXIMUM_EXPAND_PIXELS = 50
   }

   override val drawRange: Rectangle = baseRectangle.expand((MAXIMUM_EXPAND_PIXELS * timeRate).toInt())
   override val permanency: Boolean = false

   override fun draw(graphics: Graphics2D) {
      graphics.color = Color.WHITE
      graphics.drawRect(0, 0, drawRange.width, drawRange.height)
   }

}