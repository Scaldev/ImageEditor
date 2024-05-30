package PRO2.projet.v3

import fr.istic.scribble.*
import Utils.*

import MatrixConversions.*

class UtilsState(service_QT: Quadtrees) {

  val UElements = UtilsElements(service_QT)
  import UElements.*

  private val WIDTH: Int = 800
  private val HEIGHT: Int = 600

  private val trans_to_function: Map[Char, Transformation] =
    Map(
      'r' -> service_QT.rotation_right,
      'R' -> service_QT.rotation_left,
      'f' -> service_QT.flip_vertical,
      'F' -> service_QT.flip_horizontal,
      'd' -> service_QT.darken,
      'D' -> service_QT.lighten,
      'g' -> service_QT.gray_shades
    )

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                             FONCTIONS PRIVEES                            * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  // **************************************************************************** \\
  // *                             Changer de mode                              * \\
  // **************************************************************************** \\

  /** @param m le mode actuel de l'application.
    * @param c la position du curseur après un click.
    * @return le nouveau mode selon m et c.
    */
  private def update_mode(m: Mode, c: Position): Mode = {

    val click_in_edit = is_in_rect((0, 0), (WIDTH, HEIGHT - 32), c)

    if click_in_edit && m == Load then {
      Edit
    } else if !click_in_edit && m == Edit then {
      Load
    } else {
        m
    }

  }

  // **************************************************************************** \\
  // *                            Charger une image                             * \\
  // **************************************************************************** \\

  /** @param s l'état de l'application.
    * @return l'état de jeu comportant un nouvel élément correspondant
    *         à l'image du fichier de chemin s.input._2, et dont
    *         s.input = (false, "") (input remis à zéro).
    */
  private def load_image(s: State): State = {

    val image_path = s.input
    val (new_qt, d) = service_QT.file_to_quadtree_and_dimensions(image_path)

    val n = get_size_order(d._1, d._2)
    
    val new_qte = QTE(new_qt, (WIDTH / 2, HEIGHT / 2), false, d, n)
    val new_elements = new_qte +: s.elements
    val new_selected = Some(new_elements.length - 1)

    s.copy(
      elements = new_elements,
      selected = new_selected,
      input = ""
    )

  }

  // **************************************************************************** \\
  // *                           Élément sélectionné                            * \\
  // **************************************************************************** \\

  /** @param s l'état de l'application en cours.
    * @param c la position de la souris.
    * @return l'état de l'application où l'élément sélectionné
    *         est le premier élément en avant-plan qui est sur
    *         le pixel cliqué par la souris (éventuellement, aucun).
    */
  private def update_move_on_click(s: State, c: Position): State = {
    s.selected match {
      case None    => s.copy(selected = get_from_click(s.elements, c, 0))
      case Some(i) => s.copy(selected = None)
    }
  }

  /**
    * @param s l'état de l'application, en mode Edit ou Load.
    * @param c la position de la souris.
    * @return le nouvel état, en ayant changé de sélection si besoin.
    */
  private def update_select_on_click(s: State, c: Position): State = {
    val new_s = s.copy(mode = update_mode(s.mode, c))
    new_s.copy(selected = get_from_click(new_s.elements, c, 0))
  }

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                            FONCTIONS PUBLIQUES                           * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  // **************************************************************************** \\
  // *                  Mettre à jour [mode chargement fichier]                 * \\
  // **************************************************************************** \\

  /** @param s l'état de l'application en mode Load.
    * @param e un événement.
    * @return le nouvel état de l'application en mode Load selon e.
    */
  def update_state_load(s: State, e: Event): State = {

    e match {

      // CLICK -> changer de mode si besoin.
      case MouseClick(x, y) => update_select_on_click(s, (x.toInt, y.toInt))

      // ENTRÉE -> charger l'image.
      case KeyPressed(KeyReturn) => load_image(s)

      // TOUCHE CLAVIER -> ajouter caractère.
      case KeyPressed(k) => s.copy(input = Utils.add_key_to_string(s.input, k))
      case _             => s

    }
  }

  // **************************************************************************** \\
  // *                       Mettre à jour [mode édition]                       * \\
  // **************************************************************************** \\

  /** @param s l'état de l'application en mode Edit.
    * @param e un événement.
    * @return le nouvel état de l'application en mode Edit selon e.
    */
  def update_state_edit(s: State, e: Event): State = {

    def trans_key(c: Char) = trans_to_function.keys.toList.contains(c)

    e match {

      case MouseClick(x, y) => update_select_on_click(s, (x.toInt, y.toInt))

      // Application la transformation voulue à l'élément.
      case KeyPressed(KeyAscii(c)) if trans_key(c) =>
        update_selected(s, update_quadtree(trans_to_function(c)))

      // Diviser par 2 la taille de l'élément.
      case KeyPressed(KeyAscii('-')) => update_selected(s, update_size(-1))

      // Multiplier par 2 la taille de l'élément.
      case KeyPressed(KeyAscii('+')) => update_selected(s, update_size(1))

      // Afficher / cacher la grille de l'élément.
      case KeyPressed(KeyAscii('G')) => update_selected(s, update_grid)

      // Passer en mode Move.
      case KeyPressed(KeyAscii('m')) => s.copy(mode = Move, selected = None)

      case KeyPressed(KeyAscii('p')) => s.copy(mode = Draw, selected = None)

      case _ => s

    }

  }

  // **************************************************************************** \\
  // *                 Mettre à jour [mode déplacement d'élément]               * \\
  // **************************************************************************** \\

  /** @param s l'état de l'application en mode Move.
    * @param e un événement.
    * @return le nouvel état de l'application en mode Move selon e.
    */
  def update_state_move(s: State, e: Event): State = {

    e match {

      // CLICK -> sélectionner/poser la sélection.
      case MouseClick(x, y) => update_move_on_click(s, (x.toInt, y.toInt))

      // DÉPLACEMENT -> déplacer la sélection s'il y en a une.
      case MouseMove(x, y) => {
        
        update_selected(s, update_position((x.toInt, y.toInt)))
      }

      // "m" -> sortir du mode Move.
      case KeyPressed(KeyAscii('m')) => s.copy(mode = Edit, selected = None)

      case _ => s

    }

  }

  def update_state_draw(s: State, e: Event): State = {

    e match {

      case MouseClick(x, y) => {
        get_from_click(s.elements, (x.toInt, y.toInt), 0) match {

          case None => s
          case Some(i) => {

            val qte = s.elements(i)
            val size_order_qt = next_pow_of_2(math.max(qte.dimensions._1, qte.dimensions._2), 0)
            val length_image = math.pow(2, size_order_qt).toInt
            println(s"length = $size_order_qt, length_image = $length_image")

            val coef = math.pow(2, qte.size_order - size_order_qt).toInt

            val new_qt = update_pixel(uncompress(qte.quadtree, size_order_qt), size_order_qt, qte.position, (y.toInt, x.toInt), RED, coef)
            val new_qte = qte.copy(quadtree = new_qt)

            s.copy(elements = s.elements.updated(i, new_qte))
          }
        }
      }

      case KeyPressed(KeyAscii('p')) => s.copy(mode = Edit)

      case _ => s
    }
  }

}
