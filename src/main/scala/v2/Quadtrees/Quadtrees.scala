package PRO2.projet.v2

import fr.istic.scribble.*

/* Le type des Quadtrees n'est pas privé et définit dans l'interface,
car on veut que l'utilisateur puisse en implémenter par lui-même. */
sealed trait QT
case class C(c: Color) extends QT
case class N(no: QT, ne: QT, se: QT, so: QT) extends QT

/* Type des transformations de quadtrees (rotation, mirroitage, etc). */
type Transformation = QT => QT

trait Quadtrees {

  // **************************************************************************** \\
  // *                                quadtrees                                 * \\
  // **************************************************************************** \\

  /** @param qt un quadtree.
    * @param grid si on affiche la grille des subdivisions ou non.
    * @param size_order l'ordre de la taille de l'image, correspondant à une
    *                   puissance de 2 strictement positive.
    * @return l'image de taille 2^size_order représentée par le quadtree qt.
    */
  def quadtree_to_image(qt: QT, grid: Boolean, size_order: Int): Image

  /** @param qt un quadtree.
    * @return la version compressée du quadtree qt, i.e. sans node interne
    *         ayant 4 enfants feuilles de même couleur.
    */
  def compress(qt: QT): QT

  // **************************************************************************** \\
  // *                        transformations prédéfinies                       * \\
  // **************************************************************************** \\

  /** @param qt un quadtree dont les subdivisions sont supposées transformées.
    * @return le quadtree qt après une rotation de 90° dans le sens trigonométrique.
    */
  def rotation_left(qt: QT): QT

  /** @param qt un quadtree dont les subdivisions sont supposées transformées.
    * @return le quadtree qt après une rotation de 90° dans le sens de l'horloge.
    */
  def rotation_right(qt: QT): QT

  /** @param qt un quadtree dont les subdivisions sont supposées transformées.
    * @return le quadtree qt après une volte-face par rapport à l'axe vertical.
    */
  def flip_vertical(qt: QT): QT

  /** @param qt un quadtree dont les subdivisions sont supposées transformées.
    * @return le quadtree qt après une volte-face par rapport  à l'axe vertical.
    */
  def flip_horizontal(qt: QT): QT

  /** @param qt un quadtree.
    * @return le quadtree qt en nuances de gris.
    */
  def gray_shades(qt: QT): QT

  /** @param qt un quadtree.
    * @return le quadtree qt éclairci.
    */
  def lighten(qt: QT): QT

  /** @param qt un quadtree.
    * @return le quadtree qt noirci.
    */
  def darken(qt: QT): QT

  // **************************************************************************** \\
  // *                                transformations                           * \\
  // **************************************************************************** \\

  /** @param qt un quadtree.
    * @param fs une liste de transformations.
    * @return le quadtree qt après les transformations.
    */
  def transform(qt: QT, fs: List[Transformation]): QT

  // **************************************************************************** \\
  // *                               file_to_quadtree                           * \\
  // **************************************************************************** \\

  /**
    * @param filename le nom du fichier d'image (chemin relatif au projet sbt)
    *                 au format jpg ou png.
    * @return le quadtree associé à l'image.
    */
  def file_to_quadtree(filename: String): QT

}
