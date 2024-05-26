package PRO2.projet.v0

import fr.istic.scribble.*

/* Le type des Quadtrees n'est pas privé et définit dans l'interface,
car on veut que l'utilisateur puisse en implémenter par lui-même. */

sealed trait QT
case class C(c: Color) extends QT
case class N(no: QT, ne: QT, se: QT, so: QT) extends QT

trait Quadtrees {

  /** @param qt un quadtree.
    * @param show_grid si on affiche la grille des subdivisions ou non.
    * @param size_order l'ordre de la taille de l'image, correspondant à une
    *                   puissance de 2 strictement positive.
    * @return l'image de taille 2^size_order représentée par le quadtree qt.
    */
  def quadtree_to_image(qt: QT, show_grid: Boolean, size_order: Int): Image

}
