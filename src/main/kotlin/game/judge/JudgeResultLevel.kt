package game.judge

enum class JudgeResultLevel(
   val text: String,
   val diffText: String
) {
   Perfection("Perfection", ""),
   FastGood("Good", "<-- Fast    "),
   LateGood("Good", "    Late -->"),
   FastBad("Bad", "<-- Fast    "),
   LateBad("Bad", "    Late -->"),
   Unobserved("Unobserved", ""),
   NotJudged("", ""),
}