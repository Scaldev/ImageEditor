package PRO2.projet

import fr.istic.scribble.*

import Transformations.*

object ImpQuadtrees extends IntQuadtrees {

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                             VARIABLES GLOBALES                           * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  private val GRID_COLOR: Color = RED

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                             FONCTIONS PRIVEES                            * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  // **************************************************************************** \\
  // *                             quadtree_to_image                            * \\
  // **************************************************************************** \\

  /** @param length la taille de la zone.
    * @param fc la couleur dans l'aire de la zone.
    * @param lc la couleur du contour de la zone.
    * @return la zone de taille length, de couleur fc et contourée par la couleur lc.
    */
  private def make_area(
      color: Color,
      show_grid: Boolean,
      length: Float
  ): Image = {

    val rectangle: Image = Rectangle(length, length)
    val colored_rectangle: Image = FillColor(rectangle, color)
    val color_line: Color = (if (show_grid) then RED else color)

    LineColor(colored_rectangle, color_line)

  }

  /** @param qt le quadtree représentant une image.
    * @param show_grid indique si on affiche la grille des subdivisions ou non.
    * @param length le nombre de pixels de longueur de l'image.
    * @return l'image de longueur length basée du quadtree qt.
    */
  private def quadtree_to_image_aux(
      qt: QT,
      show_grid: Boolean,
      length: Float
  ): Image = {

    qt match {

      // Cas de base : toute la zone n'est qu'une seule couleur.
      case C(c) => make_area(c, show_grid, length)

      // Cas récursif : diviser la zone en 4 sous-zones, puis régner (les réunir).
      case N(no, ne, se, so) => {

        val img_no: Image = quadtree_to_image_aux(no, show_grid, length / 2)
        val img_ne: Image = quadtree_to_image_aux(ne, show_grid, length / 2)
        val img_se: Image = quadtree_to_image_aux(se, show_grid, length / 2)
        val img_so: Image = quadtree_to_image_aux(so, show_grid, length / 2)

        val img_top: Image = Beside(img_no, img_ne)
        val img_bot: Image = Beside(img_so, img_se)
        Below(img_top, img_bot)

      }

    }

  }

  // **************************************************************************** \\
  // *                               compress                                   * \\
  // **************************************************************************** \\

  /** @param qt un quadtree.
    * @param transfo une fonction de transformation de quadtree.
    * @return le quadtree qt après la transformation transfo.
    */
  private def get_compressed_node(no: QT, ne: QT, se: QT, so: QT): QT = {

    (no, ne, se, so) match {
      case (C(a), C(b), C(c), C(d)) if a == b && b == c && c == d => C(a)
      case _ => N(no, ne, se, so)
    }

  }

  // **************************************************************************** \\
  // *                            transformation                                * \\
  // **************************************************************************** \\

  /** @param qt un quadtree.
    * @param transfo une fonction de transformation de quadtree.
    * @return le quadtree qt après la transformation transfo.
    */
  private def apply_transfo(qt: QT, f: QT => QT): QT = {

    qt match {

      case C(c) => f(qt)

      case N(no, ne, se, so) => {

        val new_no: QT = apply_transfo(no, f)
        val new_ne: QT = apply_transfo(ne, f)
        val new_se: QT = apply_transfo(se, f)
        val new_so: QT = apply_transfo(so, f)

        val new_qt: QT = N(new_no, new_ne, new_se, new_so)
        f(new_qt)
      }

    }
  }

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                            FONCTIONS PUBLIQUES                           * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  def quadtree_to_image(qt: QT, show_grid: Boolean, size_order: Int): Image = {

    // L'image doit avoir une longueur et une largeur positives.
    if size_order < 1 then {
      throw Exception("Invalid input: size_order < 1.")
    }

    val length: Float = math.pow(2.0, size_order.toDouble).toFloat
    quadtree_to_image_aux(qt, show_grid, length)

  }

  def compress(qt: QT): QT = {

    qt match {

      case C(c) => C(c)

      case N(no, ne, se, so) => {

        val c_no: QT = compress(no)
        val c_ne: QT = compress(ne)
        val c_se: QT = compress(se)
        val c_so: QT = compress(so)

        get_compressed_node(c_no, c_ne, c_se, c_so)

      }
    }
  }

  def transform(qt: QT, transfo: Transformation): QT = {

    transfo match {

      case RotationLeft   => apply_transfo(qt, rotationLeft)
      case RotationRight  => apply_transfo(qt, rotationRight)
      case FlipVertical   => apply_transfo(qt, flipVertical)
      case FlipHorizontal => apply_transfo(qt, flipHorizontal)

      case ColorGray    => apply_transfo(qt, colorGrayScale)
      case ColorLighten => apply_transfo(qt, colorLighten)
      case ColorDarken  => apply_transfo(qt, colorDarken)

    }
  }

  def transforms(qt: QT, transfos: List[Transformation]): QT = {

    transfos.foldRight(qt)((trans, acc) => transform(acc, trans))
  }

}
