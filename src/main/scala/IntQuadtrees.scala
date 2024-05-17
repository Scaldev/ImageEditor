package PRO2.projet

import fr.istic.scribble.*

/* Le type des Quadtrees n'est pas privé et définit dans l'interface,
car on veut que l'utilisateur puisse en implémenter par lui-même. */

sealed trait QT
case class C(c: Color) extends QT
case class N(no: QT, ne: QT, se: QT, so: QT) extends QT

sealed trait Transformation
case object RotationLeft   extends Transformation
case object RotationRight  extends Transformation
case object FlipVertical   extends Transformation
case object FlipHorizontal extends Transformation
case object ColorGrayScale extends Transformation
case object ColorLighten   extends Transformation
case object ColorDarken    extends Transformation

trait IntQuadtrees {

  /** @param qt un quadtree.
    * @param show_grid si on affiche la grille des subdivisions ou non.
    * @param size_order l'ordre de la taille de l'image, correspondant à une
    *                   puissance de 2 strictement positive.
    * @return l'image de taille 2^size_order représentée par le quadtree qt.
    */
  def quadtree_to_image(qt: QT, show_grid: Boolean, size_order: Int): Image

  /** @param qt un quadtree.
    * @return la version compressée du quadtree qt, i.e. sans node interne
    *         ayant 4 enfants feuilles de même couleur.
    */
  def compress(qt: QT): QT

  /** @param qt un quadtree.
    * @param transfo une transformation de quadtree.
    * @return le quadtree qt après la transformation.
    */
  def transform(qt: QT, transfo: Transformation): QT

  /** @param qt un quadtree.
    * @param transfos une liste de transformations à appliquer sur qt.
    * @return le quadtree qt une fois la liste appliquée.
    */
  def transforms(qt: QT, transfos: List[Transformation]): QT

}
