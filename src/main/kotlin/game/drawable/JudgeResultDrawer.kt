package game.drawable

import game.judge.JudgeResultLevel
import game.screen.Drawable
import game.screen.Screen.expandWidthOfScreen
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.Rectangle

class JudgeResultDrawer(
   private val resultLevel: JudgeResultLevel,
   diffTime: Long
) : Drawable {
   val MAXIMUM_VISIBLE_TIME = 500

   private val drawProgressRate: Float = diffTime.toFloat() / MAXIMUM_VISIBLE_TIME

   override val drawRange: Rectangle = Rectangle(0, 250, 0, 200).expandWidthOfScreen()
   override val permanency: Boolean = false

   override fun draw(graphics: Graphics2D) {
      graphics.color =
         Color(160, 160, 200, (255 * (1 - drawProgressRate)).toInt())
      graphics.font = Font("Fira Code Retina", 0, 72)
      graphics.drawString(
         resultLevel.toString(),
         (drawRange.width - graphics.fontMetrics.stringWidth(resultLevel.toString())) / 2,
         ((72 + 50) - 25 * drawProgressRate).toInt()
      )
   }

}