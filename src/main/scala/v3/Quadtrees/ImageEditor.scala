package PRO2.projet.v3

import fr.istic.scribble.*

import java.util.Scanner;

// Quadtree Edited
private case class QTE(
    quadtree: QT,
    position: (Int, Int),
    grid: Boolean,
    size_order: Int
)

private case class State(
    elements: Vector[QTE],
    history: List[Transformation],
    selected: Option[Int],
    input: (Boolean, String)
)

class ImageEditor(service_QT: Quadtrees) extends Universe[State] {

  val size_order = 9
  val WIDTH: Int = 800
  val HEIGHT: Int = 600
  val name = "Image editor"

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                             FONCTIONS PRIVEES                            * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  /**
    * @param elements un tableau des images chargées.
    * @return l'affichage de ces images en fonction de leurs paramètres.
    */
  private def elements_to_image(elements: Vector[QTE]): Image = {

    elements match {

      case e +: es => {
        val top: Image =
          service_QT.quadtree_to_image(e.quadtree, e.grid, size_order)
        val bot: Image = elements_to_image(es)
        val (i, j) = e.position
        val length = math.pow(2, size_order).toInt
        OnFrontAt(
          top,
          bot,
          (i - length / 2).toFloat,
          (j - length / 2).toFloat,
        )
      }

      case _ => Empty
    }
  }

  /**
    * @param s l'état de l'application.
    * @param f la transformation sélectionnée.
    * @return s où l'élément sélectionné a reçu la transformation f.
    */
  private def transform_event(s: State, f: Transformation): State = {

    s.selected match {

      case None => s

      case Some(i) => {

        // Modifier le quadtree sélectionné selon la transformation voulue.
        val qte = s.elements(i)
        val new_qt = service_QT.transform(qte.quadtree, f :: Nil)
        val new_qte = QTE(new_qt, qte.position, qte.grid, qte.size_order)

        // Mettre à jour l'état de l'application.
        val new_elements = s.elements.updated(i, new_qte)
        val new_history = f :: s.history
        State(new_elements, new_history, s.selected, s.input)
      }
    }
  }

  /**
    * @param s l'état de l'application.
    * @return s où tous ses éléments ont eu leur état grid opposé.
    */
  private def flip_grid_display(s: State): State = {
    val new_elements = s.elements.map(qte => qte.copy(grid = !qte.grid))
    s.copy(elements = new_elements)
  }

  /**
    * @param p la position du coin en haut à gauche de la zone.
    * @param size_order un entier s tel que la zone est de longueur 2^s.
    * @param c la position du curseur.
    * @return true ssi c est dans la zone définie par p et size_order.
    */
  private def is_in_square(
      p: (Int, Int),
      size_order: Int,
      c: (Int, Int)
  ): Boolean = {

    val (i, j) = p
    val (x, y) = c
    val length = math.pow(2, size_order).toInt
    (i <= x && x <= i + length) && (j <= y && y <= j + length)
  }

  /**
    * @param elements le tableau des éléments chargés.
    * @param c la position du curseur.
    * @param i l'index d'un élément chargé.
    * @return le premier élément chargé dont un pixel est c (éventuellement aucun).
    */
  private def find_first_on_click(
      elements: Vector[QTE],
      c: (Int, Int),
      i: Int
  ): Option[Int] = {

    elements match {

      case e +: es => {
        if is_in_square(e.position, e.size_order, c) then return Some(i)
        else find_first_on_click(es, c, i + 1)
      }
      case _ => None

    }
  }

  /**
    * @param s l'état de l'application en cours.
    * @param x la première coordonnée de la souris.
    * @param y la seconde coordonnée de la souris.
    * @return l'état de l'application où l'élément sélectionné
    *         est le premier élément en avant-plan qui est sur
    *         le pixel cliqué par la souris (éventuellement, aucun).
    */
  private def select_clicked_element(s: State, x: Int, y: Int): State = {
     s.copy(selected = find_first_on_click(s.elements, (x, y), 0))
  }

  /**
    * @param s l'état de l'application.
    * @return l'état de jeu comportant un nouvel élément correspondant
    *         à l'image du fichier de chemin s.input._2, et dont
    *         s.input = (false, "") (input remis à zéro).
    */
  private def load_image(s: State): State = {

    val image_path = s.input._2
    val new_qt = service_QT.file_to_quadtree(image_path)

    val new_qte = QTE(new_qt, (WIDTH / 2, HEIGHT / 2), false, size_order)
    val new_elements = new_qte +: s.elements
    val new_selected = Some(new_elements.length - 1)

    s.copy(elements = new_elements, selected = new_selected, input = (false, ""))

  }

  /**
    * @param filename une chaîne de caractère représentant le
    *             chemin du fichier image voulant être chargé.
    * @param k la touche appuyée.
    * @return le couple (e, filename'), où :
    *   - e vaut true ssi la touche ENTRÉE a été pressée, signifiant
    *       la fin de l'input (et donc le chargement de l'image).
    *   - filename' est le filename modifiée selon la touche appuyée.
    */
  private def update_input(filename: String, k: Key): (Boolean, String) = {
    k match {
      case KeyAscii(c) => (true, filename + c)
      case KeyDelete   => (true, filename.dropRight(1))
      case KeyReturn   => (false, filename)
      case _           => (true, filename)
    }
  }

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                            FONCTIONS PUBLIQUES                           * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  def init: State = {
    
    State(Vector(), Nil, None, (false, ""))
  }

  def toImage(s: State): Image = {

    val elements_space = Rectangle(1600, 800)
    val elements_images: Image = elements_to_image(s.elements)
    val ui: Image = FillColor(Text(s.input._2, 16), BLACK)

    val a = OnFrontAt(elements_images, elements_space, 0, 0)
    OnFrontAt(ui, a, 0, 0)
  }

  def stopWhen(s: State): Boolean = false

  def react(s: State, e: Event): State = {

    if s.input._1 then {
      e match {
        case KeyPressed(k) => {
          val new_input = update_input(s.input._2, k)
          if new_input._1 == false then load_image(s)
          else s.copy(input = new_input)
        }
        case _ => s
      }

    } else

      e match {

        case KeyPressed(KeyAscii('r')) => {
          transform_event(s, service_QT.rotation_right)
        }

        case KeyPressed(KeyAscii('g')) => flip_grid_display(s)

        case KeyPressed(KeyAscii('l')) => {
          s.copy(input = (true, ""))
        }

        case MouseClick(x, y) => select_clicked_element(s, x.toInt, y.toInt)

        case _ => s

      }
  }

}
