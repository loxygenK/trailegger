import game.Game
import game.sheet.SheetMusicParser
import java.io.File

fun main(args: Array<String>) {

   if(args.isEmpty()) {
      println("Specify a music sheet!")
      return
   }

   val musicSheet = SheetMusicParser.parse(
      File(args[0])
   )
   Game(musicSheet).start()

}