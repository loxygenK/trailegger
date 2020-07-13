package game

import game.drawable.JudgeResultDrawer
import game.drawable.Keyboard
import game.drawable.Line
import game.judge.JudgeResult
import game.judge.JudgeResultLevel
import game.score.Score
import game.screen.Drawable
import game.screen.Screen
import game.sheet.SheetMusic
import game.utils.getCenter
import java.awt.*
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.nio.file.Paths

class Game(
   private val sheetMusic: SheetMusic
) : KeyAdapter() {

   private val point = Score()
   private val sound = Sound(sheetMusic.soundFilePath)
   private val successSound = Sound(Paths.get("sound/success.wav"))
   private val unobservedSound = Sound(Paths.get("sound/unobserved.wav"))

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

   fun start() {
      Screen.show()
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
      Screen.registerTask(sheetMusic.songInfo.createSongInfoDrawer())

      sound.play()
      while (true) {
         val currentTime = sound.getCurrentPosition().toLong()
         withFPScare {

            // --- フレームごとの処理

            val lostNotes = sheetMusic.notes.getLostNotes(currentTime)
            if(lostNotes.isNotEmpty()) {
               unobservedSound.play()
               lostNotes.map {
                  val judgeResult = it.judge(currentTime)
                  previousJudgeResult = judgeResult
                  point.updatePoint(judgeResult.level)
               }
            }

            if(this.previousJudgeResult != null && (currentTime - this.previousJudgeResult!!.timing) < 800f ){
               Screen.registerTask(previousJudgeResult!!.createJudgeResultDrawer(currentTime))
            }

            val neighborhoodNotes = sheetMusic.notes.getNearNotes(currentTime, 2)
            if(neighborhoodNotes.size == 2) {
               Screen.registerTask(
                  Line(
                     neighborhoodNotes[0].baseRange.getCenter(),
                     neighborhoodNotes[1].baseRange.getCenter(),
                     Color(200, 200, 255)
                  )
               )
            }

            // --- 描画処理

            Screen.registerTask(Keyboard)

            sheetMusic.notes.getNotesByTimeRange(currentTime.relativeRange(-1000, 3000)).forEach {
               it.createJudgeBorder(currentTime)?.let { task -> Screen.registerTask(task) }
            }

            Screen.registerTask(point.createPointDrawer())

            Screen.resolveTask()

         }
      }

   }

   override fun keyPressed(e: KeyEvent?) {

      Keyboard.getKeyCap(e!!.keyChar.toLowerCase())?.highlighting = true

      val currentTime = sound.getCurrentPosition().toLong()
      val nearestNotes = sheetMusic.notes.getNearestNote(currentTime, e.keyChar) ?: return

      val judgeResult = nearestNotes.judge(currentTime)

      if(judgeResult.level == JudgeResultLevel.NotJudged) return
      successSound.play()
      previousJudgeResult = judgeResult
      point.updatePoint(previousJudgeResult!!.level)

   }

   override fun keyReleased(e: KeyEvent?) {
      Keyboard.getKeyCap(e!!.keyChar.toLowerCase())?.highlighting = false
   }
}

fun Long.relativeRange(start: Long, end: Long): LongRange{
   return (this + start) until (this + end)
}