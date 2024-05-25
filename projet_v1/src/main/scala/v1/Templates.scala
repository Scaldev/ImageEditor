package PRO2.projet.v1

import fr.istic.scribble.*

object Templates {

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                             COULEURS GLOBALES                            * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  private val brown: Color = Color(123, 84, 60, 255)
  private val blue: Color = Color(80, 180, 210, 255)
  private val light_blue: Color = Color(200, 255, 255, 255)
  private val lime: Color = Color(100, 170, 90, 255)
  private val green: Color = Color(90, 100, 50, 255)
  private val yellow: Color = Color(240, 220, 90, 255)
  private val red: Color = Color(190, 50, 50, 255)
  private val pink: Color = Color(255, 200, 230, 255)

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                          EXEMPLES DE QUADTREES                           * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  // **************************************************************************** \\
  // *                             quadtree_apple                               * \\
  // **************************************************************************** \\

  val quadtree_apple: QT = N(
    N(C(green), C(red), C(red), C(red)),
    N(C(lime), C(lime), C(lime), C(red)),
    N(C(red), C(red), C(green), C(red)),
    N(C(red), C(red), C(red), C(green))
  )

  // **************************************************************************** \\
  // *                               quadtree_sun                               * \\
  // **************************************************************************** \\

  val quadtree_sun: QT = N(
    N(C(blue), C(yellow), C(yellow), C(yellow)),
    N(C(yellow), C(blue), C(yellow), C(yellow)),
    N(C(yellow), C(yellow), C(blue), C(yellow)),
    N(C(yellow), C(yellow), C(yellow), C(blue))
  )

  // **************************************************************************** \\
  // *                             quadtree_cloud                               * \\
  // **************************************************************************** \\

  val quadtree_cloud: QT = N(
    N(C(blue), C(blue), C(blue), C(light_blue)),
    N(C(blue), C(blue), C(light_blue), C(light_blue)),
    N(C(light_blue), C(light_blue), C(blue), C(blue)),
    N(C(light_blue), C(light_blue), C(blue), C(blue))
  )

  // **************************************************************************** \\
  // *                             quadtree_flower                              * \\
  // **************************************************************************** \\

  val quadtree_flower: QT = N(
    N(C(blue), C(blue), C(pink), C(blue)),
    N(C(pink), C(blue), C(pink), C(yellow)),
    N(C(pink), C(blue), C(blue), C(blue)),
    N(C(blue), C(lime), C(lime), C(blue))
  )
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
