package PRO2.projet.v3

import fr.istic.scribble.*

import Utils.*

class ModeEyedropper(service_QT: Quadtrees) {

  private val UElements = UtilsElements(service_QT)
  import UElements.*

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                             FONCTIONS PRIVEES                            * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                            FONCTIONS PUBLIQUES                           * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  /** @param s l'état de l'application en mode Eyedropper.
    * @param e un événement.
    * @return le nouvel état de l'application en mode Eyedropper selon e.
    */
  def update_state_eyedropper(s: State, e: Event): State = {

    e match {

      case MouseClick(x, y) => {

        val c = (x.toInt, y.toInt)
        val new_mode = update_mode_on_click(s.mode, c)

        // Obtenir la couleur du pixel cliqué.

        val drawn_element = get_from_click(s.elements, c, 0)
        val new_color = (drawn_element match {
          case None    => s.pencil.color
          case Some(i) => get_pixel_of_element(s.elements(i), c, s.pencil)
        })
        val new_pencil = s.pencil.copy(color = new_color)

        s.copy(mode = new_mode, pencil = new_pencil)

      }

      case KeyPressed(KeyAscii(c))
          if key_to_mode.keys.toList.contains(KeyAscii(c)) => {
        s.copy(mode = key_to_mode(KeyAscii(c)))
      }

      case _ => s
    }
  }

}
