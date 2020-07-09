package game.drawable

import game.screen.Drawable
import game.screen.Screen.expandWidthOfScreen
import game.screen.Screen.toBottomOfScreen
import java.awt.*

class Keyboard : Drawable {

   private val keyboardCaptions = listOf(
      "1234567890-^\\", "qwertyuiop@[", "asdfghjkl;:]", "zxcvbnm,./\\"
   )
   private val keyCaps: List<List<KeyCap>>
   private val keySize = 50
   private val keyMargin = 15

   override val drawRange = Rectangle(0, (keySize + keyMargin) * 4).toBottomOfScreen().expandWidthOfScreen()
   override val permanency: Boolean = false

   init {
      val location = mutableListOf<MutableList<KeyCap>>()
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

         location.add(partialLocation)
      }
      keyCaps = location
   }

   override fun draw(graphics: Graphics2D) {
      graphics.color = Color.GRAY
      graphics.font = Font("Fira Code Retina", 0, 24)
      keyCaps.forEach {
         it.forEach {  key ->
            key.draw(graphics)
         }
      }
   }

   fun getKeyCap(char: Char): KeyCap? {

      val y = keyboardCaptions.indexOfFirst { it.contains(char) }
      if(y == -1) return null

      val x = keyboardCaptions[y].indexOfFirst { it == char }

      return keyCaps[y][x]

   }

}