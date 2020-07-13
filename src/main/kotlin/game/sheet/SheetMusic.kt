package game.sheet

import game.Sound
import java.nio.file.Path

data class SheetMusic (
   val notes: NotesGetter,
   val soundFilePath: Path,
   val songInfo: SongInfo
)