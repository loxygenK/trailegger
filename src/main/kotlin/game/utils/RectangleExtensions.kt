package game.utils

import java.awt.Point
import java.awt.Rectangle

fun Rectangle.expand(delta: Int): Rectangle {

   if(delta < 0) {
      if(this.width < -delta) return Rectangle(0, 0, 0, 0)
      if(this.height < -delta) return Rectangle(0, 0, 0, 0)
   }

   val newX = this.x - delta / 2
   val newY = this.y - delta / 2

   return Rectangle(newX, newY, this.width + delta, this.height + delta)

}

fun Rectangle.toGlobalLocation(base: Rectangle) : Rectangle {
   return Rectangle(
      this.x + base.x,
      this.y + base.y,
      this.width,
      this.height
   )
}

fun Rectangle.getCenter() : Point {
   return Point(
      this.x + this.width / 2,
      this.y + this.height / 2
   )
}