package PRO2.projet

import fr.istic.scribble.*

import Templates.*

object ProgrammeUtilisateur extends App {

  // **************************************************************************** \\
  // *                                Configuration                             * \\
  // **************************************************************************** \\

  val serv_QT: IntQuadtrees = ImpQuadtrees

  val show_grid: Boolean = false
  val size_order: Int = 9
  val quadtree: QT = quadtree_plain

  // **************************************************************************** \\
  // *                                Manipulation                              * \\
  // **************************************************************************** \\

  val transfos: List[Transformation] = ColorGrayScale :: ColorDarken :: FlipVertical :: RotationLeft :: Nil
  val qt: QT = serv_QT.transforms(quadtree, transfos)

  val image = serv_QT.quadtree_to_image(qt, show_grid, size_order)

  draw(image)

}
