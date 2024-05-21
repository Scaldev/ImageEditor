package PRO2.projet

import fr.istic.scribble.*
import Templates.*

object ProgrammeUtilisateur extends App {

  // **************************************************************************** \\
  // *                                Configuration                             * \\
  // **************************************************************************** \\

  val service_QT: Quadtrees = ImpQuadtrees

  val grid: Boolean = false
  val size_order: Int = 9
  val quadtree: QT = quadtree_plain

  // **************************************************************************** \\
  // *                                  Fonctions                               * \\
  // **************************************************************************** \\

  /** @param f une transformation.
    * @param n le nombre de fois où f doit être appliquée.
    * @return la liste contenant n fois la fonction f.
    */
  def repeat(f: Transformation, n: Int): List[Transformation] = {
    (1 to n).toList.map(_ => f)
  }

  /** @param qt un quadtree.
    * @return un quadtree dont les valeurs des couleurs ont été modifiées chaotiquement.
    */
  def color_chaos(qt: QT): QT = {
    qt match {
      case C(Color(r, g, b, a)) => C(Color(g, b, r, a))
      case _                    => qt
    }
  }

  /**
    * @param qt un quadtree.
    * @return le quadtree après une série de transformations choisies.
    */
  def my_transformation(qt: QT) = {

    val transfos = service_QT.rotation_right
                :: Nil
  
    service_QT.transform(quadtree, transfos)

  }

  // **************************************************************************** \\
  // *                                Manipulation                              * \\
  // **************************************************************************** \\

  val qt: QT = my_transformation(quadtree)

  val image = service_QT.quadtree_to_image(qt, grid, size_order)

  draw(image)

}
