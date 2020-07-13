package game.score

import game.drawable.Keyboard
import game.drawable.NoteBorder
import game.screen.Drawable
import game.system.JudgeResult
import game.utils.expand
import game.utils.toGlobalLocation
import java.awt.Graphics2D
import java.awt.Rectangle
import kotlin.math.abs

class Note(
   char: Char,
   val timestamp: Long
) {

   companion object {
      private const val MAXIMUM_VISIBLE_TIME = 800.0f
   }

   val baseRange: Rectangle = Keyboard.getKeyCap(char)!!.rectangle.toGlobalLocation(Keyboard.drawRange)

   fun judge(currentTimestamp: Long): JudgeResult {
      return JudgeResult.Perfection
   }

   fun createJudgeBorder(currentTimestamp: Long): Drawable? {
      if(abs(timestamp - currentTimestamp) > MAXIMUM_VISIBLE_TIME) return null
      return NoteBorder(
         baseRange,
         (timestamp - currentTimestamp) / MAXIMUM_VISIBLE_TIME
      )
   }

}