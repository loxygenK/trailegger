package game.drawable

import java.awt.Color
import java.awt.Graphics2D
import java.awt.Rectangle

class KeyCap(
   private val caption: Char,
   private val rectangle: Rectangle
) {

   fun draw(
      graphics: Graphics2D,
      background: Color = Color(0, 0, 0, 0),
      borderColor: Color = Color.GRAY,
      textColor: Color = Color.GRAY
   ) {
      graphics.color = background
      graphics.fillRect(0, 0, rectangle.width, rectangle.height)

      graphics.color = borderColor
      graphics.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height)

      graphics.color = textColor
      graphics.drawString(
         caption.toString(),
         rectangle.x + rectangle.width / 2 - 7, rectangle.y + rectangle.height / 2 + 9
      )
   }

}