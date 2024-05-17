package PRO2.projet

import fr.istic.scribble.*

object Templates {

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                             COULEURS GLOBALES                            * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  private val brown: Color = Color(123, 84, 60, 255)
  private val blue: Color = Color(80, 180, 210, 255)
  private val lime: Color = Color(100, 170, 90, 255)
  private val green: Color = Color(90, 100, 50, 255)
  private val yellow: Color = Color(240, 220, 90, 255)
  private val red: Color = Color(190, 50, 50, 255)

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                          EXEMPLES DE QUADTREES                           * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  // **************************************************************************** \\
  // *                             quadtree_plain                               * \\
  // **************************************************************************** \\

  val quadtree_plain: QT = N(
    
    N(C(green), C(green), N(C(red), C(green), C(green), C(green)), C(green)),
    N(C(blue), C(yellow), C(blue), C(blue)),
    N(C(blue), C(blue), C(lime), C(lime)),
    N(C(brown), C(blue), C(lime), C(lime))
  )

}
