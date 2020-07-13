package game.score

import game.drawable.PointDrawer
import game.system.JudgeResultLevel
import game.system.JudgeSettings

class Score {

   var point = 0
      private set
   var chain = 0
      private set

   val judgeResults = hashMapOf(
      JudgeResultLevel.Unobserved to 0,
      JudgeResultLevel.FastBad to 0,
      JudgeResultLevel.FastGood to 0,
      JudgeResultLevel.Perfection to 0
   )

   fun createPointDrawer(): PointDrawer {
      return PointDrawer(
         point, chain
      )
   }

   fun updatePoint(judgeResultLevel: JudgeResultLevel) {

      if(judgeResultLevel == JudgeResultLevel.NotJudged) {
         println("Event was dispatched, but wasn't judged. Maybe too far to judge.")
         return
      }
      println("Judged! result was $judgeResultLevel.")

      point += JudgeSettings.points[judgeResultLevel]!!

      if(judgeResultLevel == JudgeResultLevel.Unobserved) {
         chain = 0
      } else {
         chain++
      }

   }

}