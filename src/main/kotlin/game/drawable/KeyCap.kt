package game.drawable

import java.awt.Graphics2D
import java.awt.Rectangle

class KeyCap(
   private val caption: Char,
   private val rectangle: Rectangle
) {

   fun draw(graphics: Graphics2D) {
      graphics.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height)
      graphics.drawString(
         caption.toString(),
         rectangle.x + rectangle.width / 2 - 7, rectangle.y + rectangle.height / 2 + 9
      )
   }

}