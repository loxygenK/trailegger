package game.sheet

import game.drawable.Keyboard
import game.drawable.NoteBorder
import game.screen.Drawable
import game.system.JudgeResultLevel
import game.system.JudgeSettings
import game.utils.toGlobalLocation
import java.awt.Rectangle
import kotlin.math.abs

class Note(
   char: Char,
   val timing: Long
) {

   private val MAXIMUM_VISIBLE_TIME = 800.0f

   var judged = false
      private set

   val baseRange: Rectangle = Keyboard.getKeyCap(char)!!.rectangle.toGlobalLocation(Keyboard.drawRange)

   fun judge(songTimestamp: Long): JudgeResultLevel {

      if(judged) throw IllegalStateException("This note is already judged! Check class Score.")

      val diffTime = timing - songTimestamp

      // ジャッジ対象時間内にジャッジ処理が呼ばれているか
      if(abs(diffTime) > JudgeSettings.JUDGE_TARGET_TIME) {
         // まだタイミングに達していればNotJudged、もう過ぎてたらそれは終わりです
         return if(diffTime > 0) {
            JudgeResultLevel.NotJudged
         } else {
            judged = true
            JudgeResultLevel.Unobserved
         }
      }

      judged = true
      return JudgeSettings.judgeBorder.entries.find { it.value.contains(diffTime) }?.key
         ?: error("Invalid difftime($diffTime ms)! Maybe JUDGE_TARGET_TIME(${JudgeSettings.JUDGE_TARGET_TIME} ms) is invalid.")

   }

   fun createJudgeBorder(songTimestamp: Long): Drawable? {
      if(abs(timing - songTimestamp) > MAXIMUM_VISIBLE_TIME) return null
      return NoteBorder(
         baseRange,
         (timing - songTimestamp) / MAXIMUM_VISIBLE_TIME
      )
   }

}