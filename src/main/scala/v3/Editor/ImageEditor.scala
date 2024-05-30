package PRO2.projet.v3

import fr.istic.scribble.*

import Utils.*

// **************************************************************************** \\
// *                                                                          * \\
// *                                 VALEURS                                  * \\
// *                                                                          * \\
// **************************************************************************** \\

type Position = (Int, Int)

sealed trait Mode
case object Edit extends Mode
case object Load extends Mode
case object Move extends Mode
case object Draw extends Mode

// Pencil
case class Pencil(
  drawing: Boolean,
  color: Color
)

// Quadtree (as Elements)
case class QTE(
    quadtree: QT,
    position: (Int, Int),
    grid: Boolean,
    size_order: Int,
)

// STATE
private case class State(
    elements: Vector[QTE],
    history: List[Transformation],
    selected: Option[Int],
    mode: Mode,
    pencil: Pencil,
    input: String,
)

// ****************************************************************************************** \\

class ImageEditor(service_QT: Quadtrees) extends Universe[State] {

  val UElements = UtilsElements(service_QT)
  import UElements.*

  private val UState = UtilsState(service_QT)
  import UState.*

  val WIDTH: Int = 800
  val HEIGHT: Int = 600
  val name = "Image editor"

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                             FONCTIONS PRIVEES                            * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  // ...

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                            FONCTIONS PUBLIQUES                           * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  def init: State = {

    val pencil = Pencil(false, BLACK)
    State(Vector(), Nil, None, Load, pencil, "images/templates/meadow.png")
  }

  def toImage(s: State): Image = {

    val n = if s.selected == None then -1 else s.selected.get

    val elements_space = Rectangle(WIDTH.toFloat, HEIGHT.toFloat - 32)
    val elements_images: Image = convert_to_image(s.elements, n)
    val edition_window = OnFrontAt(elements_images, elements_space, 0, 0)

    val console = FillColor(Rectangle(WIDTH.toFloat, 32), BLACK)
    val text_path: Image = FillColor(Text(s"[${s.mode}] > load " + s.input, 16), WHITE)
    val ui = OnFrontAt(text_path, console, 16, 8)

    Below(edition_window, ui)
  }

  def stopWhen(s: State): Boolean = {
    false
  }

  def react(s: State, e: Event): State = {

    s.mode match {

      case Load => update_state_load(s, e)

      case Edit => update_state_edit(s, e)

      case Move => update_state_move(s, e)

      case Draw => update_state_draw(s, e)

    }

  }

}
