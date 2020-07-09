package game.screen

import java.awt.*
import java.awt.image.BufferedImage
import javax.swing.JFrame

object Screen {

   /* 定数たち */
   val WINDOW_SIZE = Dimension(960, 720)

   private val frame: JFrame = JFrame("Trailegger")
   private lateinit var graphics: Graphics

   private val taskQueue = mutableListOf<Drawable>()

   var DEBUG = false

   init {

      frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
      frame.size = WINDOW_SIZE
      frame.isUndecorated = true
      frame.setLocationRelativeTo(null)

      frame.isVisible = false

   }

   fun show() {
      frame.isVisible = true
      graphics = frame.graphics
   }

   fun registerTask(task: Drawable) {
      taskQueue.add(task)
   }

   fun resolveTask() {

      // TODO: 描画速度が遅い
      // なんかもっといいやり方を使いたい…
      val windowImage = BufferedImage(WINDOW_SIZE.width, WINDOW_SIZE.height, BufferedImage.TYPE_INT_ARGB)

      taskQueue.map {
         val range = it.drawRange
         val image = windowImage.getSubimage(range.x, range.y, range.width, range.height)

         val partialGraphics: Graphics2D = image.createGraphics()
         partialGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
         partialGraphics.color = Color.RED
         if (DEBUG) partialGraphics.drawRect(0, 0, range.width - 1, range.height - 1)
         it.draw(partialGraphics)

      }
      graphics.drawImage(windowImage, 0, 0, null)

      taskQueue.removeIf { !it.permanency }

   }

   fun Rectangle.expandWidthOfScreen(margin: Int = 0): Rectangle {
      return Rectangle(margin, this.y, WINDOW_SIZE.width - margin * 2, this.height)
   }

   fun Rectangle.expandHeightOfScreen(margin: Int = 0): Rectangle {
      return Rectangle(this.x, 0, this.width, WINDOW_SIZE.height - margin)
   }

   fun Rectangle.toBottomOfScreen(margin: Int = 0): Rectangle {
      return Rectangle(this.x, WINDOW_SIZE.height - this.height - margin, this.width, this.height)
   }

}