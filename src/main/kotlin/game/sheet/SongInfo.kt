package game.sheet

import game.drawable.SongInfoDrawer

data class SongInfo (
   val name: String,
   val author: String,
   val bpm: Float
) {
   
   fun createSongInfoDrawer(): SongInfoDrawer {
      return SongInfoDrawer(name, author)
   }
   
}