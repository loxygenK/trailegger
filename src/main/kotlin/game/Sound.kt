package game

import java.io.IOException
import java.nio.file.Path
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.DataLine
import kotlin.properties.Delegates

class Sound(
   soundFilePath: Path
) {

   private lateinit var clip: Clip
   private var frameRate by Delegates.notNull<Float>()

   init {
      AudioSystem.getAudioInputStream(soundFilePath.toFile()).use { ais ->
         clip = AudioSystem.getLine(
            DataLine.Info(Clip::class.java, ais.format)
         ) as Clip
         clip.open(ais)
         frameRate = ais.format.frameRate
      }
   }

   fun play() {
      clip.start()
   }

   fun pause() {
      clip.stop()
   }

   fun stop() {
      clip.stop()
      clip.flush()
      clip.framePosition = 0
   }

   fun getCurrentPosition(): Float {
      return clip.longFramePosition / frameRate * 1000
   }

}