package game.sheet

import game.drawable.Keyboard
import game.drawable.NoteBorder
import game.judge.JudgeResult
import game.screen.Drawable
import game.judge.JudgeResultLevel
import game.judge.JudgeSettings
import game.utils.toGlobalLocation
import java.awt.Rectangle
import kotlin.math.abs

class Note(
   val char: Char,
   val timing: Long
) {

   private val maximumVisibleTime = 500.0f

   var judged = false
      private set

   val baseRange: Rectangle = Keyboard.getKeyCap(char)!!.rectangle.toGlobalLocation(Keyboard.drawRange)

   fun judge(songTimestamp: Long): JudgeResult {

      if(judged) throw IllegalStateException("This note is already judged! Check class Score.")

      val diffTime = timing - songTimestamp

      // ジャッジ対象時間内にジャッジ処理が呼ばれているか
      if(abs(diffTime) > JudgeSettings.JUDGE_TARGET_TIME) {
         // まだタイミングに達していればNotJudged、もう過ぎてたらそれは終わりです
         return if(diffTime > 0) {
            JudgeResult(JudgeResultLevel.NotJudged, songTimestamp)
         } else {
            judged = true
            JudgeResult(JudgeResultLevel.Unobserved, songTimestamp)
         }
      }

      judged = true

      val judgeResultLevel = JudgeSettings.judgeBorder.entries.find { it.value.contains(diffTime) }?.key
         ?: error("Invalid difftime($diffTime ms)! Maybe JUDGE_TARGET_TIME(${JudgeSettings.JUDGE_TARGET_TIME} ms) is invalid.")

      return JudgeResult(judgeResultLevel, songTimestamp)

   }

   fun createJudgeBorder(songTimestamp: Long): Drawable? {
      if(abs(timing - songTimestamp) > maximumVisibleTime) return null
      return NoteBorder(
         baseRange,
         (timing - songTimestamp) / maximumVisibleTime
      )
   }

}