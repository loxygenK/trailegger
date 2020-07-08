package game.screen

import java.awt.Graphics2D
import java.awt.Rectangle

interface Drawable {

   val drawRange: Rectangle
   val permanency: Boolean

   fun draw(graphics: Graphics2D)

}