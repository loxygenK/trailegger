package game.score

object ScoreParser {

   // TODO: 実際にファイルから読み込ませる
   fun parse(): Score {
      return Score(
         List(10) { Note('w', it * 1000L + 5000L) },
         SongInfo(
            "とても良いテスト用の譜面",
            "An author of the very nice score for test",
            120.0f
         )
      )
   }

}