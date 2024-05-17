package PRO2.projet

import fr.istic.scribble.*

object Transformations {

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                             FONCTIONS PRIVEES                            * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  // **************************************************************************** \\
  // *                                  colors                                  * \\
  // **************************************************************************** \\

  /** @param v l'une des valeurs d'une couleur (rouge, bleu ou vert).
    * @param coef le taux de lumonosité par lequel multiplier v.
    * @return la valeur v multiplée par coef, en restant entre 0 et 255.
    */
  private def rgb_brighness(v: Int, coef: Double): Int = {
    
    math.min((v * coef).toInt, 255)
  }

  /** @param qt un quadtree.
    * @param color_map une fonction associant une couleur à sa nouvelle valeur.
    * @return le quadtree où toutes les couleurs sont passées par color_map.
    */
  private def colorChange(qt: QT, color_map: Color => Color): QT = {
    qt match {
      case C(c) => C(color_map(c))
      case _    => qt
    }
  }

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                            FONCTIONS PUBLIQUES                           * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  // **************************************************************************** \\
  // *                                  rotation                                * \\
  // **************************************************************************** \\

  /** @param qt un quadtree dont les subdivisions ont été tournées vers la gauche.
    * @return le quadtree qt après une rotation vers la gauche.
    */
  def rotationLeft(qt: QT): QT = {
    qt match {
      case N(ne, se, so, no) => N(se, so, no, ne)
      case _                 => qt
    }
  }

  /** @param qt un quadtree dont les subdivisions ont été tournées vers la droite.
    * @return le quadtree qt après une rotation vers la droite.
    */
  def rotationRight(qt: QT): QT = {
    qt match {
      case N(ne, se, so, no) => N(no, ne, se, so)
      case _                 => qt
    }
  }

  // **************************************************************************** \\
  // *                                  miroiter                                * \\
  // **************************************************************************** \\

  /** @param qt un quadtree dont les subdivisions ont été miroitées par l'axe vertical.
    * @return le quadtree qt après un miroitage d'axe vertical.
    */
  def flipVertical(qt: QT): QT = {
    qt match {
      case N(ne, se, so, no) => N(ne, no, so, se)
      case _                 => qt
    }
  }

  /** @param qt un quadtree dont les subdivisions ont été miroitées par l'axe horizontal.
    * @return le quadtree qt après un miroitage d'axe horizontal.
    */
  def flipHorizontal(qt: QT): QT = {
    qt match {
      case N(ne, se, so, no) => N(so, se, ne, so)
      case _                 => qt
    }
  }

  // **************************************************************************** \\
  // *                                  couleurs                                * \\
  // **************************************************************************** \\

  /** @param qt un quadtree.
    * @return le même quadtree dont les couleurs sont en nuance de gris.
    */
  def colorGrayScale(qt: QT): QT = {

    def color_map(c: Color): Color = {

      val gray: Int = (c.red + c.green + c.blue) / 3

      Color(gray, gray, gray, 255)
    }

    colorChange(qt, color_map)
  }

  /** @param qt un quadtree.
    * @return le même quadtree éclairé à 20%.
    */
  def colorLighten(qt: QT): QT = {

    def color_map(c: Color): Color = {

      val r: Int = rgb_brighness(c.red, 1.2)
      val g: Int = rgb_brighness(c.green, 1.2)
      val b: Int = rgb_brighness(c.blue, 1.2)

      Color(r, g, b, 255)
    }

    colorChange(qt, color_map)
  }

  /** @param qt un quadtree.
    * @return le même quadtree assombri à 20%.
    */
  def colorDarken(qt: QT): QT = {

    def color_map(c: Color): Color = {

      val r: Int = rgb_brighness(c.red, 0.8)
      val g: Int = rgb_brighness(c.green, 0.8)
      val b: Int = rgb_brighness(c.blue, 0.8)

      Color(r, g, b, 255)
    }

    colorChange(qt, color_map)
  }

}
