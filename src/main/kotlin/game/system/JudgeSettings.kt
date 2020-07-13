package game.system

object JudgeSettings {

   const val JUDGE_TARGET_TIME = 300

   val judgeBorder = hashMapOf(
      JudgeResultLevel.FastBad to 200..300,
      JudgeResultLevel.FastGood to 100..200,
      JudgeResultLevel.Perfection to -100..100,
      JudgeResultLevel.LateGood to -200..-100,
      JudgeResultLevel.LateBad to -300..-200
   )

   val points = hashMapOf(
      JudgeResultLevel.NotJudged to 0,
      JudgeResultLevel.Unobserved to 0,
      JudgeResultLevel.FastBad to 50,
      JudgeResultLevel.LateBad to 50,
      JudgeResultLevel.FastGood to 100,
      JudgeResultLevel.LateGood to 100,
      JudgeResultLevel.Perfection to 500
   )

}