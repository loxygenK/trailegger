package game.sheet

import java.nio.file.Path

data class SheetMusic (
   val notes: NotesGetter,
   val soundFilePath: Path,
   val songInfo: SongInfo
)