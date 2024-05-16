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
    * La puissance de 2 telle que la fenêtre sera de longueur
    * et de largeur 2^size_order pixels.
    * @example size_order = 9 => fenêtre de 512x512 pixels.
    */
  val size_order: Int = 9

  bigbang(Application(quadtree_input, size_order))

}