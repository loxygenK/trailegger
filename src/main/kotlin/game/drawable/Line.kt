package game.drawable

import game.screen.Drawable
import game.screen.Screen
import java.awt.*

class Line(
   private val start: Point,
   private val end: Point,
   private val color: Color = Color.WHITE
) : Drawable {
   override val drawRange: Rectangle = Rectangle(0, 0, Screen.WINDOW_SIZE.width, Screen.WINDOW_SIZE.height)
   override val permanency: Boolean = false

   override fun draw(graphics: Graphics2D) {
      graphics.color = color
      graphics.stroke = BasicStroke(3f)
      graphics.drawLine(start.x, start.y, end.x, end.y)
   }


}