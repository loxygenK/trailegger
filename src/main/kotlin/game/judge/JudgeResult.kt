package game.judge

import game.drawable.JudgeResultDrawer

data class JudgeResult (
   val level: JudgeResultLevel,
   val timing: Long
) {

   fun createJudgeResultDrawer(songTimestamp: Long): JudgeResultDrawer {
      return JudgeResultDrawer(
         this.level,
         songTimestamp - timing
      )
   }

}
