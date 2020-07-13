package game.score

import game.drawable.ScoreDrawer
import game.judge.JudgeResultLevel
import game.judge.JudgeSettings

class Score {

   private var point = 0
   private var chain = 0

   fun createPointDrawer(): ScoreDrawer {
      return ScoreDrawer(
         point, chain
      )
   }

   fun updatePoint(judgeResultLevel: JudgeResultLevel) {

      if(judgeResultLevel == JudgeResultLevel.NotJudged) return
      point += JudgeSettings.points[judgeResultLevel]!!

      if(judgeResultLevel == JudgeResultLevel.Unobserved) {
         chain = 0
      } else {
         chain++
      }

   }

}