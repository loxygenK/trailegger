package game

import game.drawable.Keyboard
import game.score.ScoreParser
import game.screen.Drawable
import game.screen.Screen
import java.awt.*
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

class Game : KeyAdapter() {

   companion object {
      private const val idealFPSWaitTime = (999 shl 16) / 60
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
   }

   private val score = ScoreParser.parse()

   fun start() {
      Screen.show()
      Screen.DEBUG = true

      Screen.addKeyListener(this)

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
      Screen.registerTask(score.songInfo.createSongInfoDrawer())

      val start = System.currentTimeMillis()
      while (true) {
         val currentTime = System.currentTimeMillis() - start
         withFPScare {

            Screen.registerTask(Keyboard)

            score.getNotesByTimeRange(currentTime.relativeRange(-1000, 3000)).forEach {
               it.createJudgeBorder(currentTime)?.let { task -> Screen.registerTask(task) }
            }

            Screen.resolveTask()

         }
      }

   }

   override fun keyPressed(e: KeyEvent?) {
      Keyboard.getKeyCap(e!!.keyChar.toLowerCase())?.highlighting = true
   }

   override fun keyReleased(e: KeyEvent?) {
      Keyboard.getKeyCap(e!!.keyChar.toLowerCase())?.highlighting = false
   }
}

fun Long.relativeRange(start: Long, end: Long): LongRange{
   return (this + start) until (this + end)
}