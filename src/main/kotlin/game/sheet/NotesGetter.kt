package game.sheet

import game.system.JudgeSettings
import kotlin.math.abs

class NotesGetter(
   private val notes: List<Note>
) {

   fun getNotesByTimeRange(range: LongRange): List<Note> {
      return notes.filter { !it.judged && range.contains(it.timing) }
   }

   fun getNearestNote(songTimestamp: Long): Note {
      return notes
         .filter {!it.judged}
         .minBy { abs(it.timing - songTimestamp) } ?: error("No note was found. Maybe no notes in score?")
   }

   fun getLostNotes(songTimestamp: Long): List<Note> {
      return notes
         .filter { !it.judged && (songTimestamp - it.timing) > JudgeSettings.JUDGE_TARGET_TIME}
   }

}