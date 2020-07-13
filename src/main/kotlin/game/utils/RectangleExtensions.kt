package game.utils

import java.awt.Rectangle

fun Rectangle.expand(delta: Int): Rectangle {

   val newX = this.x - delta / 2
   val newY = this.y - delta / 2

   return Rectangle(newX, newY, this.width + delta, this.height + delta)

}

fun Rectangle.shrink(delta: Int): Rectangle {
   return this.expand(-delta)
}

fun Rectangle.toGlobalLocation(base: Rectangle) : Rectangle {
   return Rectangle(
      this.x + base.x,
      this.y + base.y,
      this.width,
      this.height
   )
}
