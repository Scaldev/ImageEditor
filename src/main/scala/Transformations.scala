package PRO2.projet

import fr.istic.scribble.*

object Transformations {

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                             FONCTIONS PRIVEES                            * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  // **************************************************************************** \\
  // *                              color values                                * \\
  // **************************************************************************** \\

  private def rgb_brighness(v: Int, coef: Double): Int = {
    math.min((v * coef).toInt, 255)
  }

  // **************************************************************************** \\
  // *                                  colors                                  * \\
  // **************************************************************************** \\

  private def color_lighten(c: Color): Color = {

    val new_red: Int   = rgb_brighness(c.red, 1.2)
    val new_green: Int = rgb_brighness(c.green, 1.2)
    val new_blue: Int  = rgb_brighness(c.blue, 1.2)

    Color(new_red, new_green, new_blue, 255)
  }

  private def color_darken(c: Color): Color = {

    val new_red: Int   = rgb_brighness(c.red, 0.8)
    val new_green: Int = rgb_brighness(c.green, 0.8)
    val new_blue: Int  = rgb_brighness(c.blue, 0.8)

    Color(new_red, new_green, new_blue, 255)
  }

  private def color_gray(c: Color): Color = {

    val gray: Int = (c.red + c.green + c.blue) / 3
    Color(gray, gray, gray, 255)

  }
  
  /**
    * @param qt un quadtree dont les subdivisions ont été miroitées par l'axe vertical.
    * @return le quadtree qt après un miroitage d'axe vertical.
    */
  private def colorChange(qt: QT, f: Color => Color): QT = {
    qt match {
      case C(c) => C(f(c))
      case _ => qt
    }
  }

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                            FONCTIONS PUBLIQUES                           * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  // **************************************************************************** \\
  // *                              rotation                                    * \\
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
  // *                              miroiter                                    * \\
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
  // *                              miroiter                                    * \\
  // **************************************************************************** \\

  /** @param qt un quadtree dont les subdivisions ont été miroitées par l'axe vertical.
    * @return le quadtree qt après un miroitage d'axe vertical.
    */
  def colorGrayScale(qt: QT): QT = {
    colorChange(qt, color_gray)
  }

  def colorLighten(qt: QT): QT = {
    colorChange(qt, color_lighten)
  }

  def colorDarken(qt: QT): QT = {
    colorChange(qt, color_darken)
  }

}
