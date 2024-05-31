package PRO2.projet.v3

import fr.istic.scribble.*

import Utils.*

class ModeDraw(service_QT: Quadtrees) {

  private val UElements = UtilsElements(service_QT)
  import UElements.*

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                             FONCTIONS PRIVEES                            * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  /** @param s l'état de l'application en mode Draw.
    * @param pixel le pixel cliqué.
    * @return l'état s où le pixel cliqué est modifié selon s.pencil.
    */
  private def draw_on_pixel(s: State, pixel: Position): State = {

    val drawn_element = get_from_click(s.elements, pixel, 0)

    drawn_element match {
      case None => s
      case Some(i) => {
        val qt = set_pixel_of_element(s.elements(i), pixel, s.pencil.color, s.pencil.size)
        s.copy(elements = s.elements.updated(i, qt))
      }

    }

  }

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                            FONCTIONS PUBLIQUES                           * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  /** @param s l'état de l'application en mode Draw.
    * @param e un événement.
    * @return le nouvel état de l'application en mode Draw selon e.
    */
  def update_state_draw(s: State, e: Event): State = {

    val (applied, toolMode) = s.pencil.mode

    e match {

      /* Appliquer l'outil sur le pixel */

      case MouseClick(x, y) => {

        val new_mode = update_mode_on_click(s.mode, (x.toInt, y.toInt))
        val new_pencil = s.pencil.copy(mode = (!applied, toolMode))
        val new_s = s.copy(mode = new_mode, pencil = new_pencil)
        draw_on_pixel(new_s, (x.toInt, y.toInt))

      }

      case MouseMove(x, y) if s.pencil.mode == (true, Tracing) => {
        draw_on_pixel(s, (x.toInt, y.toInt))
      }

      /* Alterner le mode d'application */

      case KeyPressed(KeyAscii('*')) => {

        val new_toolMode = (toolMode match {
          case Placing => Tracing
          case Tracing => Placing
        })

        val new_pencil = s.pencil.copy(mode = (applied, new_toolMode))
        s.copy(pencil = new_pencil)

      }

      /* Modification de la taille de l'outil */

      case KeyPressed(KeyAscii('-')) =>
        s.copy(pencil = s.pencil.copy(size = math.max(s.pencil.size - 1, 0)))

      case KeyPressed(KeyAscii('+')) =>
        s.copy(pencil = s.pencil.copy(size = math.min(s.pencil.size + 1, 10)))

      /* Autres */

      case KeyPressed(KeyAscii(c)) if key_to_mode.keys.toList.contains(KeyAscii(c)) => {
        s.copy(mode = key_to_mode(KeyAscii(c)))
      }

      case _ => s
    }
  }

}