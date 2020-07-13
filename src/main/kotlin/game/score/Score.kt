package game.score

data class Score (
   val notes: List<Note>,
   val songInfo: SongInfo
) {

   fun getNotesByTimeRange(range: LongRange): List<Note> {
      return notes.filter { range.contains(it.timestamp) }
   }

}