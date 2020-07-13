package game.drawable

import game.screen.Drawable
import game.screen.Screen.expandWidthOfScreen
import game.screen.Screen.toEdgeOfScreen
import game.screen.ScreenEdge
import java.awt.*

object Keyboard : Drawable {

   private val keyboardCaptions = listOf(
      "1234567890-^\\", "qwertyuiop@[", "asdfghjkl;:]", "zxcvbnm,./\\"
   )
   private val keyCaps: List<KeyCap>
   private const val keySize = 40
   private const val keyMargin = 25

   override val drawRange =
      Rectangle(0, (keySize + keyMargin) * 4).toEdgeOfScreen(ScreenEdge.DOWN, 15).expandWidthOfScreen(15)
   override val permanency: Boolean = false

   init {
      val pendingKeyCaps = mutableListOf<KeyCap>()
      val keyDimension = keySize + keyMargin

      for(y in keyboardCaptions.indices) {
         val keyCount = keyboardCaptions[y].length
         val startX = (drawRange.width - keyDimension * keyCount) / 2

         val partialLocation = mutableListOf<KeyCap>()
         for(x in 0 until keyCount) {
            partialLocation.add(
               KeyCap(
                  keyboardCaptions[y][x],
                  Rectangle(startX + x * keyDimension, y * keyDimension, keySize, keySize)
               )
            )
         }

         pendingKeyCaps.addAll(partialLocation)
      }
      keyCaps = pendingKeyCaps
   }

   override fun draw(graphics: Graphics2D) {
      graphics.color = Color.GRAY
      graphics.font = Font("Fira Code Retina", 0, 24)
      keyCaps.forEach {
         it.draw(graphics)
      }
   }

   fun getKeyCap(char: Char): KeyCap? {
      return keyCaps.find { it.caption == char }
   }

}