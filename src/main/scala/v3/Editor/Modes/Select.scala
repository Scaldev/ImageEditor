package PRO2.projet.v3

import fr.istic.scribble.*

import Utils.*

class ModeSelect(service_QT: Quadtrees) {

  private val UElements = UtilsElements(service_QT)
  import UElements.*

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                             FONCTIONS PRIVEES                            * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  /** @param qte1 un élément quadtree.
    * @param qte2 un élément quadtree.
    * @return la superposition de qt2 sur qt1.
    */
  private def overlay_qte(qte1: QTE, qte2: QTE): QTE = {

    val s = qte1.size_order
    val qt1 = service_QT.decompress(qte1.quadtree, s)
    val qt2 = service_QT.decompress(qte2.copy(size_order = s).quadtree, s)
    val qt = service_QT.compress(service_QT.overlay(qt1, qt2))

    QTE(qt, qte1.position, qte1.grid, s, qte1.size_first)
  }

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                            FONCTIONS PUBLIQUES                           * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  /** @param s l'état de l'application en mode Select.
    * @param e un événement.
    * @return le nouvel état de l'application en mode Select selon e.
    */
  def update_state_select(s: State, e: Event): State = {

    e match {

      case MouseClick(x, y) => {

        val c = (x.toInt, y.toInt)
        val new_mode = update_mode_on_click(s.mode, c)
        val new_selected = get_from_click(s.elements, (x.toInt, y.toInt), 0)

        (s.selected, new_selected) match {

          case (Some(i), Some(j)) if i != j => {
            
            val new_i = overlay_qte(s.elements(i), s.elements(j))
            s.copy(
              mode = new_mode,
              selected = None,
              elements = delete_element(s.elements.updated(i, new_i), j)
            )
          }
          case _ => s.copy(mode = new_mode)
        }

      }

      case KeyPressed(KeyAscii(c)) if key_to_mode.keys.toList.contains(KeyAscii(c)) => {
        s.copy(mode = key_to_mode(KeyAscii(c)))
      }

      case _ => s
    }
  }

}
