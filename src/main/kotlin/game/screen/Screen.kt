package game.screen

import java.awt.*
import java.awt.event.KeyListener
import java.awt.image.BufferedImage
import javax.swing.JFrame

object Screen {

   /* 定数たち */
   val WINDOW_SIZE = Dimension(960, 720)

   private val frame: JFrame = JFrame("Trailegger")
   private lateinit var graphics: Graphics

   private val taskQueue = mutableListOf<Drawable>()

   private var DEBUG = false

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

   fun addKeyListener(listener: KeyListener) {
      frame.addKeyListener(listener)
   }

   fun registerTask(task: Drawable) {
      taskQueue.add(task)
   }

   fun resolveTask() {

      // TODO: 描画速度が遅い
      // なんかもっといいやり方を使いたい…
      val windowImage = BufferedImage(WINDOW_SIZE.width + 1, WINDOW_SIZE.height + 1, BufferedImage.TYPE_INT_ARGB)

      taskQueue.map {
         val range = it.drawRange
         val image = windowImage.getSubimage(
            range.x, range.y, range.width + 1, range.height + 1)

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

   fun Rectangle.toEdgeOfScreen(edge: ScreenEdge, margin: Int = 0): Rectangle {
      val newLocation = when(edge) {
         ScreenEdge.TOP -> Point(this.x, 0 + margin)
         ScreenEdge.DOWN -> Point(this.x, WINDOW_SIZE.height - this.height - margin)
         ScreenEdge.LEFT -> Point(0 + margin, this.y)
         ScreenEdge.RIGHT -> Point(WINDOW_SIZE.width - this.width - margin, this.y)
      }
      return Rectangle(newLocation.x, newLocation.y, this.width, this.height)
   }

}