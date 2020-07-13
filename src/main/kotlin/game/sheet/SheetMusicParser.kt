package game.sheet

object SheetMusicParser {

   // TODO: 実際にファイルから読み込ませる
   fun parse(): SheetMusic {
      return SheetMusic(
         NotesGetter(List(10) { Note('w', it * 1000L + 5000L) }),
         SongInfo(
            "とても良いテスト用の譜面",
            "An author of the very nice score for test",
            120.0f
         )
      )
   }

}