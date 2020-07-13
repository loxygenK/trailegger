package game.drawable

import game.screen.Drawable
import game.screen.Screen.expandWidthOfScreen
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.Rectangle

class PointDrawer(
   val point: Int,
   val chain: Int
) : Drawable {

   override val drawRange: Rectangle =
      Rectangle(0, 130, 0, 150).expandWidthOfScreen(30)
   override val permanency: Boolean = false

   override fun draw(graphics: Graphics2D) {

      val pointString = String.format("%08d", point)
      val chainString = String.format("%04d chain", chain)

      graphics.color = Color(75, 75, 125)
      graphics.font = Font("Fira Code Retina", 0, 96)
      graphics.drawString(
         pointString,
         (drawRange.width - graphics.fontMetrics.stringWidth(pointString)) / 2,
         90
      )

      graphics.color = Color(75, 75, 125)
      graphics.font = Font("Fira Code Retina", 0, 36)
      graphics.drawString(
         chainString,
         (drawRange.width - graphics.fontMetrics.stringWidth(chainString)) / 2,
         126
      )


   }

}