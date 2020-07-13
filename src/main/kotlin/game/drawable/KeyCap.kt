package game.drawable

import java.awt.Color
import java.awt.Graphics2D
import java.awt.Rectangle

class KeyCap(
   val caption: Char,
   val rectangle: Rectangle
) {

   var highlighting = false

   fun draw(
      graphics: Graphics2D
   ) {
      graphics.color = if(highlighting) Color(36, 74, 168) else Color(0, 0, 0, 0)
      graphics.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height)

      graphics.color = Color.GRAY
      graphics.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height)

      graphics.drawString(
         caption.toString(),
         rectangle.x + rectangle.width / 2 - 7, rectangle.y + rectangle.height / 2 + 9
      )
   }

}