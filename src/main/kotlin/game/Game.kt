package game

import game.drawable.Keyboard
import game.screen.Drawable
import game.screen.Screen
import java.awt.*

class Game {

   private val idealFPSWaitTime = (1000 shl 16) / 60

   private fun withFPScare(frameOperation: () -> Unit) {

      val startTime = System.currentTimeMillis() shl 16
      frameOperation()
      val endTime = System.currentTimeMillis() shl 16

      val sleepTime = (idealFPSWaitTime - (endTime - startTime)) shr 16

      if(sleepTime < 0) {
         println("[!] ${-sleepTime} ms over!")
         return
      }

      Thread.sleep(sleepTime)

   }

   fun start() {
      Screen.show()
      Screen.DEBUG = true

      val screenEraser =
         object : Drawable {
            override val drawRange = Rectangle(0, 0, Screen.WINDOW_SIZE.width, Screen.WINDOW_SIZE.height)
            override val permanency = true

            override fun draw(graphics: Graphics2D) {
               graphics.color = Color(17, 25, 45)
               graphics.fillRect(0, 0, drawRange.width, drawRange.height)
            }

         }

      Screen.registerTask(screenEraser)

      val keyBoard = Keyboard()

      while (true) {
         withFPScare {
            Screen.registerTask(keyBoard)
            Screen.resolveTask()
         }
      }

   }

}