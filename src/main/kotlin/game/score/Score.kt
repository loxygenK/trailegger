package game.score

import game.drawable.ScoreDrawer
import game.judge.JudgeResultLevel
import game.judge.JudgeSettings

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

   fun createPointDrawer(): ScoreDrawer {
      return ScoreDrawer(
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