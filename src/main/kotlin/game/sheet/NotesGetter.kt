package game.sheet

import game.judge.JudgeSettings
import kotlin.math.abs

class NotesGetter(
   private val notes: List<Note>
) {

   fun getNotesByTimeRange(range: LongRange): List<Note> {
      return notes.filter { !it.judged && range.contains(it.timing) }
   }

   fun getNearestNote(songTimestamp: Long, keyName: Char): Note? {
      return notes
         .filter {!it.judged && it.char == keyName}
         .minBy { abs(it.timing - songTimestamp) }
   }

   fun getNearNotes(songTimestamp: Long, count: Int): List<Note> {
      val filteredNotes = notes
         .filter { !it.judged && it.timing > songTimestamp }
         .sortedBy { it.timing }

      return if(filteredNotes.size > count) filteredNotes.subList(0, count) else filteredNotes

   }

   fun getLostNotes(songTimestamp: Long): List<Note> {
      return notes
         .filter { !it.judged && (songTimestamp - it.timing) > JudgeSettings.JUDGE_TARGET_TIME}
   }

}