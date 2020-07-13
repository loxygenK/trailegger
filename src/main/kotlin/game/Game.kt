package game

import game.drawable.Keyboard
import game.score.Score
import game.sheet.SheetMusicParser
import game.screen.Drawable
import game.screen.Screen
import java.awt.*
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

class Game : KeyAdapter() {

   private val score = SheetMusicParser.parse()
   private val point = Score()

   private var currentTime = 0L

   private var previousJudgeResult: JudgeResult? = null

   private val idealFPSWaitTime = (999 shl 16) / 60
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

   private val score = SheetMusicParser.parse()
   private val point = Score()

   private var currentTime = 0L

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
         currentTime = System.currentTimeMillis() - start
         withFPScare {

            score.notes.getLostNotes(currentTime).map {
               point.updatePoint(it.judge(currentTime))
            }

            Screen.registerTask(Keyboard)

            score.notes.getNotesByTimeRange(currentTime.relativeRange(-1000, 3000)).forEach {
               it.createJudgeBorder(currentTime)?.let { task -> Screen.registerTask(task) }
            }

            Screen.registerTask(point.createPointDrawer())

            Screen.resolveTask()

         }
      }

   }

   override fun keyPressed(e: KeyEvent?) {

      Keyboard.getKeyCap(e!!.keyChar.toLowerCase())?.highlighting = true
      point.updatePoint(score.notes.getNearestNote(currentTime).judge(currentTime))

   }

   override fun keyReleased(e: KeyEvent?) {
      Keyboard.getKeyCap(e!!.keyChar.toLowerCase())?.highlighting = false
   }
}

fun Long.relativeRange(start: Long, end: Long): LongRange{
   return (this + start) until (this + end)
}