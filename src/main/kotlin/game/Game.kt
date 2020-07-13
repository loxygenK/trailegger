package game

import game.drawable.JudgeResultDrawSetting
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

      println(sleepTime)

      if(sleepTime < 0) {
         println("[!] ${-sleepTime} ms over!")
         return
      }

      Thread.sleep(sleepTime)

   }

   fun start() {
      sound.play()
      while(!sound.isPlaying) { Thread.sleep(10) }
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

      while (sound.isPlaying) {
         val currentTime = sound.getCurrentPosition().toLong()
         withFPScare {

            // 取り逃したノーツを処理する
            val lostNotes = sheetMusic.notes.getLostNotes(currentTime)
            if(lostNotes.isNotEmpty()) {
               unobservedSound.play()
               lostNotes.map {
                  val judgeResult = it.judge(currentTime)
                  previousJudgeResult = judgeResult
                  point.updatePoint(judgeResult.level)
               }
            }

            // ジャッジリザルトを描画する
            if(this.previousJudgeResult != null && (currentTime - this.previousJudgeResult!!.timing) < JudgeResultDrawSetting.maximumVisibleTime ){
               Screen.registerTask(previousJudgeResult!!.createJudgeResultDrawer(currentTime))
            }

            // となりのノーツとの線を描画する
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

            // キーボードを描画する
            Screen.registerTask(Keyboard)

            // 描画対象になるノーツを描画する
            sheetMusic.notes.getNotesByTimeRange(currentTime.relativeRange(-1000, 3000)).forEach {
               it.createJudgeBorder(currentTime)?.let { task -> Screen.registerTask(task) }
            }

            // 得点情報を描画する
            Screen.registerTask(point.createPointDrawer())

            // 描画処理を開始する
            Screen.resolveTask()

         }
      }

      println("---------")

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