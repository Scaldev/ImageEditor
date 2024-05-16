package PRO2.projet

import fr.istic.scribble.*
import Quadtrees.*

object ProgrammeUtilisateur extends App {

  /**
    * Le quadtree que l'utilisateur veut visualiser.
    */
  val quadtree_input: QT = N(
    C(BLACK),
    N(C(BLACK), C(BLACK), C(WHITE), C(WHITE)),
    C(WHITE),
    N(C(BLACK), C(WHITE), C(WHITE), C(WHITE))
  )

  /**
    * La puissance de 2 telle que l'image sera de longueur
    * et de largeur 2^size_order pixels.
    * @example size_order = 9 => image de 512x512 pixels.
    * @note si size_order est "raisonnable", la fenêtre et
    *       l'image seront de même taille.
    */
  val size_order: Int = 9

  bigbang(Application(quadtree_input, size_order))

}