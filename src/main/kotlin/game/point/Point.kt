package game.point

import game.drawable.PointDrawer
import game.system.JudgeResult

class Point {

   var point = 0
   var chain = 0
   val judgeResults = hashMapOf(
      JudgeResult.Unobserved to 0,
      JudgeResult.Good to 0,
      JudgeResult.Perfection to 0
   )

   fun createPointDrawer(): PointDrawer {
      return PointDrawer(
         point, chain
      )
   }

}