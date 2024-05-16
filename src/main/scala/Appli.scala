package PRO2.projet

import fr.istic.scribble.*
import Quadtrees.*

/**
  * L'état de l'univers, définit comme étant :
  * - quadtree : la représentation sous forme d'arbre de l'image affichée.
  * - length : la longueur (et largeur) de l'image affichée.
  * - show_grid : si la grille des subdivisions est affichée ou non.
  * - stop : si l'univers est arrêté ou non.
  */
case class State(
    quadtree: QT,
    length: Int,
    show_grid: Boolean,
    stop: Boolean
)

object Appli extends Universe[State] {

  // **************************************************************************** \\
  // *                          VARIABLES GLOBALES                              * \\
  // **************************************************************************** \\

  // Paramètres de la fenêtre de jeu.

  val HEIGHT: Int = 512
  val WIDTH: Int = 512
  val name: String = "Quadtrees"

  // Paramètres par défaut à l'initialisation.

  val DEFAULT_IMG_LENGTH: Int = 512

  val DEFAULT_QUADTREE: QT =
    N(
      C(BLACK),
      N(C(BLACK), C(BLACK), C(WHITE), C(WHITE)),
      C(WHITE),
      N(C(BLACK), C(WHITE), C(WHITE), C(WHITE))
    )


  // **************************************************************************** \\
  // *                          FONCTIONS PUBLIQUES                             * \\
  // **************************************************************************** \\

  /** @return l'état initial du jeu.
    */
  def init: State = {

    State(DEFAULT_QUADTREE, DEFAULT_IMG_LENGTH, false, false)

  }

  /** @param s l'état de l'univers.
    * @return l'affichage de l'image en fonction de l'état de l'univers s.
    */
  def toImage(s: State): Image = {

    quadtree_to_image(s.quadtree, s.show_grid, s.length)

  }

  /** @param s l'état de l'univers.
    * @return true ssi le joueur a appuyé sur "x".
    */
  def stopWhen(s: State): Boolean = {

    s.stop == true

  }

  /** @param s l'état de l'univers.
    * @param e un événement.
    * @return le nouvel état de l'univers en fonction de l'événement e.
    */
  def react(s: State, e: Event): State = {

    e match {

      // Doubler la taille de l'image.
      case KeyPressed(KeyAscii('+')) => {

        val new_length: Int = math.min(HEIGHT, s.length * 2)
        State(s.quadtree, new_length, s.show_grid, s.stop)

      }

      // Diviser par 2 la taille de l'image.
      case KeyPressed(KeyAscii('-')) => {

        val new_length: Int = math.max(1, s.length / 2)
        State(s.quadtree, new_length, s.show_grid, s.stop)

      }

      // Afficher / cacher la grille des subdivisions.
      case KeyPressed(KeyAscii('g')) => {

        val new_show_grid: Boolean = !s.show_grid
        State(s.quadtree, s.length, new_show_grid, s.stop)

      }

      // Arrêter l'univers.
      case KeyPressed(KeyAscii('x')) => {

        State(s.quadtree, s.length, s.show_grid, true)

      }

      // Pour les autres événements : ne rien faire.
      case _ => s

    }

  }
}
