package PRO2.projet

import fr.istic.scribble.*

object Quadtrees {

  sealed trait QT
  case class C(c: Color) extends QT
  case class N(no: QT, ne: QT, se: QT, so: QT) extends QT

  /**
    * @param qt le quadtree représentant une image.
    * @param show_grid indique si on affiche la grille des subdivisions ou non.
    * @param length le nombre de pixels de longueur de l'image.
    * @return l'image de longueur length basée du quadtree qt.
    */
  def quadtree_to_image(qt: QT, show_grid: Boolean, length: Float): Image = {

    qt match {

      // Cas de base : toute la zone n'est qu'une seule couleur.
      case C(c) => {

        val img_unicolor: Image = FillColor(Rectangle(length, length), c)
        val line_color: Color = if show_grid then RED else c
        LineColor(img_unicolor, line_color)

      }

      // Cas récursif : diviser la zone en 4 sous-zones, puis régner (les réunir).
      case N(no, ne, se, so) => {

        val img_no: Image = quadtree_to_image(no, show_grid, length / 2)
        val img_ne: Image = quadtree_to_image(ne, show_grid, length / 2)
        val img_se: Image = quadtree_to_image(se, show_grid, length / 2)
        val img_so: Image = quadtree_to_image(so, show_grid, length / 2)

        val img_top: Image = Beside(img_no, img_ne)
        val img_bot: Image = Beside(img_so, img_se)
        val img_all: Image = Below(img_top, img_bot)

        img_all
      }

    }

  }

}