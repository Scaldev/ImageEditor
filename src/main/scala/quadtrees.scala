package PRO2.projet

import fr.istic.scribble.*

object Quadtrees {

  // **************************************************************************** \\
  // *                          VARIABLES GLOBALES                              * \\
  // **************************************************************************** \\

  sealed trait QT
  case class C(c: Color) extends QT
  case class N(no: QT, ne: QT, se: QT, so: QT) extends QT

  val GRID_COLOR: Color = RED

  // **************************************************************************** \\
  // *                          FONCTIONS PUBLIQUES                             * \\
  // **************************************************************************** \\

  /**
   * @param length la taille de la zone.
   * @param fc la couleur dans l'aire de la zone.
   * @param lc la couleur du contour de la zone.
   * @return la zone de taille length, de couleur fc et contourée par la couleur lc. 
   */
  def make_area(color: Color, show_grid: Boolean, length: Float): Image = {

    val rectangle: Image = Rectangle(length, length)
    val colored_rectangle: Image = FillColor(rectangle, color)
    val line_color: Color = if show_grid then GRID_COLOR else color

    LineColor(colored_rectangle, line_color)

  }

  /**
    * @param qt le quadtree représentant une image.
    * @param show_grid indique si on affiche la grille des subdivisions ou non.
    * @param length le nombre de pixels de longueur de l'image.
    * @return l'image de longueur length basée du quadtree qt.
    */
  def quadtree_to_image(qt: QT, show_grid: Boolean, length: Float): Image = {

    qt match {

      // Cas de base : toute la zone n'est qu'une seule couleur.
      case C(c) => make_area(c, show_grid, length)

      // Cas récursif : diviser la zone en 4 sous-zones, puis régner (les réunir).
      case N(no, ne, se, so) => {

        // Déterminer les images des 4 sous-zones.
        val img_no: Image = quadtree_to_image(no, show_grid, length / 2)
        val img_ne: Image = quadtree_to_image(ne, show_grid, length / 2)
        val img_se: Image = quadtree_to_image(se, show_grid, length / 2)
        val img_so: Image = quadtree_to_image(so, show_grid, length / 2)

        // Assembler les images des sous-zones.
        val img_top: Image = Beside(img_no, img_ne)
        val img_bot: Image = Beside(img_so, img_se)     
        Below(img_top, img_bot)

      }

    }

  }

}