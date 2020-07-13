import game.Game
import game.sheet.SheetMusicParser
import java.io.File

fun main() {

   val musicSheet = SheetMusicParser.parse(
      File("test/kkiminochikara.tlm")
   )
   Game(musicSheet).start()

}