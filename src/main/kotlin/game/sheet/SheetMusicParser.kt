package game.sheet

import java.io.File
import kotlin.properties.Delegates

object SheetMusicParser {

   class InvalidSheetMusicException(
      line: Int,
      content: String,
      errorMessage: String
   ) : Exception("[LINE $line] $content\n  $errorMessage")

   enum class Parsing {
      NOTHING,
      SONGINFO,
      NOTES
   }

   private val validChars = "1234567890-^\\qwertyuiop@[asdfghjkl;:]zxcvbnm,./_ "

   // TODO: 実際にファイルから読み込ませる
   fun parse(sheetMusicFile: File): SheetMusic {

      val lines = sheetMusicFile.readLines()

      var name: String? = null
      var author: String? = null
      var bpm: Float? = null
      var offset = 0f
      var soundFileName: String? = null

      var timePerBar = 0f

      val notes = mutableListOf<Note>()

      var barCount = -1
      var parsing = Parsing.NOTHING
      for (rawLine in lines.withIndex()) {

         val index = rawLine.index
         val line = rawLine.value.trim()
         if(line.isEmpty()) continue
         if(line.startsWith("#")) continue

         if(line.startsWith("---")) {
            if(parsing != Parsing.NOTHING) {
               parsing = Parsing.NOTHING
               continue
            }
            parsing = when(line.substring(3).toLowerCase()) {
               "songinfo" -> Parsing.SONGINFO
               "music" -> Parsing.NOTES
               else -> throw InvalidSheetMusicException(index, line, "Invalid Block Name!")
            }
            continue
         }

         if(parsing == Parsing.SONGINFO) {

            val matchResult = "(.*?)\\s*:\\s*(.*?)".toRegex().matchEntire(line) ?: continue
            val key = matchResult.groups[1]!!.value
            val value = matchResult.groups[2]!!.value

            when(key) {
               "name" -> name = value
               "author" -> author = value
               "music" -> soundFileName = value
               "offset" -> {
                  val parsedOffset = value.toFloatOrNull() ?: throw InvalidSheetMusicException(index, line, "Offset must be number!")
                  offset = parsedOffset * 1000
               }
               "bpm" -> {
                  bpm = value.toFloatOrNull() ?: throw InvalidSheetMusicException(index, line, "BPM must be number!")
                  timePerBar = 60 / bpm * 1000 * 4
               }
            }
            continue
         }

         if(parsing == Parsing.NOTES) {

            if(name == null || author == null || soundFileName == null || bpm == null) {
               throw InvalidSheetMusicException(index, line, "There is/are not initialized property(ies)!")
            }

            val bars = line.split("|").filter { it != "" }
            val parsedNotes = bars.map { barContent ->
               barCount++
               val timePerNote = 60 / bpm / (barContent.length / 4.0) * 1000
               barContent.mapIndexed { idx, c ->
                  if(!validChars.contains(c.toLowerCase())) throw InvalidSheetMusicException(index, line, "Invalid character found! ($c)")
                  if(c != ' ') Note(c.toLowerCase(), (offset + barCount * timePerBar + idx * timePerNote).toLong()) else null
               }.filterNotNull()
            }.flatten()

            notes.addAll(parsedNotes)
            continue

         }

         throw InvalidSheetMusicException(index, line, "Unexpected Statement!")

      }

      if(notes.isEmpty()) throw InvalidSheetMusicException(0, "(General Error)", "No notes found!")

      val musicFilePath = File(sheetMusicFile.parentFile, soundFileName!!)
      if(!musicFilePath.isFile) {
         throw InvalidSheetMusicException(0, "(Runtime Error)", "Music file not found!")
      }

      return SheetMusic(
         NotesGetter(notes),
         musicFilePath.toPath(),
         SongInfo(name!!, author!!, bpm!!)
      )
   }

}