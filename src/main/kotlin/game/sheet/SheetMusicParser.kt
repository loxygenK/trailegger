package game.sheet

import java.io.File

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

   // TODO: 実際にファイルから読み込ませる
   fun parse(sheetMusicFile: File): SheetMusic {

      val lines = sheetMusicFile.readLines()

      var name = ""
      var author = ""
      var bpm = 0.0f
      var offset = 0.0f
      var soundFileName = ""

      var timePerNote = 0f
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
                  timePerNote = 60 / bpm * 1000
                  timePerBar = timePerNote * 4
               }
            }
            continue
         }

         if(parsing == Parsing.NOTES) {

            val bars = line.split("|")
            val parsedNotes = bars.map { barContent ->
               barCount++
               barContent.mapIndexed { idx, c ->
                  Note(c, (offset + barCount * timePerBar + idx * timePerNote).toLong())
               }
            }.flatten()

            notes.addAll(parsedNotes)
            continue

         }

         throw InvalidSheetMusicException(index, line, "Unexpected Statement!")

      }

      val musicFilePath = File(sheetMusicFile.parentFile, soundFileName)
      if(!musicFilePath.isFile) {
         throw InvalidSheetMusicException(0, "(Runtime Error)", "Music file not found!")
      }

      return SheetMusic(
         NotesGetter(notes),
         musicFilePath.toPath(),
         SongInfo(name, author, bpm)
      )
   }

}