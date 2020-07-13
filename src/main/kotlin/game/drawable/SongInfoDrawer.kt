package game.drawable

import game.screen.Drawable
import game.screen.Screen.toEdgeOfScreen
import game.screen.ScreenEdge
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.Rectangle

class SongInfoDrawer(
   private val title: String,
   private val author: String
) : Drawable {

   override val drawRange: Rectangle =
      Rectangle(0, 0, 400, 75).toEdgeOfScreen(ScreenEdge.TOP, 5).toEdgeOfScreen(ScreenEdge.RIGHT, 15)
   override val permanency: Boolean = true

   override fun draw(graphics: Graphics2D) {
      graphics.color = Color(130, 130, 170)
      graphics.font = Font("Noto Sans CJK JP Mono Semibolv", 0, 32)
      graphics.drawString(
         title,
         drawRange.width - graphics.fontMetrics.stringWidth(title),
         32
      )

      graphics.color = Color(100, 100, 120)
      graphics.font = Font("Noto Sans CJK JP Mono Semibolv", 0, 16)
      graphics.drawString(
         author,
         drawRange.width - graphics.fontMetrics.stringWidth(author),
         52
      )
   }

}