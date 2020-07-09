package game.drawable

import game.screen.Drawable
import game.screen.Screen.expandWidthOfScreen
import game.screen.Screen.toBottomOfScreen
import java.awt.*

class Keyboard : Drawable {

   private val keyboardCaptions = listOf(
      "1234567890-^\\", "qwertyuiop@[", "asdfghjkl;:]", "zxcvbnm,./\\"
   )
   private val keyboardLocation: List<List<Point>>
   private val keySize = 50
   private val keyMargin = 15

   override val drawRange = Rectangle(0, (keySize + keyMargin) * 4).toBottomOfScreen().expandWidthOfScreen()
   override val permanency: Boolean = false

   init {
      val location = mutableListOf<MutableList<Point>>()
      val keyDimension = keySize + keyMargin

      for(y in keyboardCaptions.indices) {
         val keyCount = keyboardCaptions[y].length
         val startX = (drawRange.width - keyDimension * keyCount) / 2

         val partialLocation = mutableListOf<Point>()
         for(x in 0 until keyCount) {
            partialLocation.add(Point(
               startX + x * keyDimension, y * keyDimension
            ))
         }

         location.add(partialLocation)
      }
      keyboardLocation = location
   }

   override fun draw(graphics: Graphics2D) {
      graphics.color = Color.GRAY
      graphics.font = Font("Fira Code Retina", 0, 24)
      keyboardLocation.forEachIndexed { y, list ->
         list.forEachIndexed { x, p ->
            graphics.drawRect(p.x, p.y, keySize, keySize)
            graphics.drawString(keyboardCaptions[y][x].toString(), p.x + keySize / 2 - 7, p.y + keySize / 2 + 9)
         }
      }
   }

}