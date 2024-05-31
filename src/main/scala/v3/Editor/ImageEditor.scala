package PRO2.projet.v3

import fr.istic.scribble.*

import Utils.*

// **************************************************************************** \\
// *                                                                          * \\
// *                                 VALEURS                                  * \\
// *                                                                          * \\
// **************************************************************************** \\

/* Paramètres globaux à l'application. */
val IMAGE_EDITOR_WIDTH: Int = 1400
val IMAGE_EDITOR_HEIGHT: Int = 700
val CONSOLE_HEIGHT: Int = 64

type Position = (Int, Int)

/* Les modes dans lequel l'utilisateur peut être.
   Réagir à un événement dépend du mode actuel. */

sealed trait Mode

case object Command extends Mode

case object Edit extends Mode
case object Move extends Mode
case object Select extends Mode

case object Draw extends Mode
case object Erase extends Mode
case object Eyedropper extends Mode

sealed trait ToolMode
case object Placing extends ToolMode
case object Tracing extends ToolMode

// **************************************************************************** \\
// *                                   Inputs                                 * \\
// **************************************************************************** \\

val key_to_mode: Map[KeyAscii, Mode] = Map(
  KeyAscii('0') -> Command,
  KeyAscii('1') -> Edit,
  KeyAscii('2') -> Move,
  KeyAscii('3') -> Select,
  KeyAscii('4') -> Draw,
  KeyAscii('5') -> Erase,
  KeyAscii('6') -> Eyedropper
)
def key_to_trans(service_QT: Quadtrees): Map[KeyAscii, Transformation] =
  Map(
    KeyAscii('r') -> service_QT.rotation_right,
    KeyAscii('R') -> service_QT.rotation_left,
    KeyAscii('f') -> service_QT.flip_vertical,
    KeyAscii('F') -> service_QT.flip_horizontal,
    KeyAscii('d') -> service_QT.darken,
    KeyAscii('D') -> service_QT.lighten,
    KeyAscii('g') -> service_QT.gray_shades
  )

// **************************************************************************** \\
// *                           État de l'application                          * \\
// **************************************************************************** \\

case class Pencil(
    mode: (
        Boolean,
        ToolMode
    ), // (si c'est en train d'être appliqué ou non, la manière dont c'est appliqué)
    color: Color,
    size: Int
)

case class Eraser(
    mode: (Boolean, ToolMode),
    size: Int
)

// Quadtree (as Elements)
case class QTE(
    quadtree: QT,
    position: (Int, Int),
    grid: Boolean,
    size_order: Int,
    size_first: Int,
)

// STATE
private case class State(
    elements: Vector[QTE],
    customs: List[Transformation],
    selected: Option[Int],
    mode: Mode,
    pencil: Pencil,
    eraser: Eraser,
    input: String
)

// **************************************************************************** \\
// *                                                                          * \\
// *                             ÉDITEUR D'IMAGES                             * \\
// *                                                                          * \\
// **************************************************************************** \\

class ImageEditor(service_QT: Quadtrees, customs: List[Transformation])
    extends Universe[State] {

  val UElements = UtilsElements(service_QT)
  import UElements.*

  private val MCommand = ModeCommand(service_QT)
  private val MDraw = ModeDraw(service_QT)
  private val MEdit = ModeEdit(service_QT)
  private val MSelect = ModeSelect(service_QT)
  private val MEyedropper = ModeEyedropper(service_QT)
  private val MMove = ModeMove(service_QT)
  private val MErase = ModeErase(service_QT)

  val WIDTH: Int = IMAGE_EDITOR_WIDTH
  val HEIGHT: Int = IMAGE_EDITOR_HEIGHT
  val name = "Image editor"

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                             FONCTIONS PRIVÉES                            * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  private def mode_to_image(m: Mode): Image = {

    m match {
      case Command    => FromFile("images/icons/command.png")
      case Edit       => FromFile("images/icons/edit.png")
      case Move       => FromFile("images/icons/move.png")
      case Select     => FromFile("images/icons/select.png")
      case Draw       => FromFile("images/icons/draw.png")
      case Erase      => FromFile("images/icons/erase.png")
      case Eyedropper => FromFile("images/icons/eyedropper.png")
    }

  }

  /** @param s l'état de l'application.
    * @return l'image représentant l'état s.
    */
  private def state_to_image(s: State): Image = {

    /* Indice de l'élément sélectionné.
      -1 assure qu'aucun événement ne sera considéré comme l'étant. */
    val n = if s.selected == None then -1 else s.selected.get

    val h = CONSOLE_HEIGHT.toFloat

    val elements_space = Rectangle(WIDTH.toFloat, HEIGHT.toFloat - h)
    val elements_images = convert_to_image(s.elements, n)
    val edition_window = OnFrontAt(elements_images, elements_space, 0, 0)

    val console = FillColor(Rectangle(WIDTH.toFloat, h), BLACK)
    val text_path = FillColor(Text(s"> " + s.input, 24), WHITE)

    val ui = OnFrontAt(text_path, console, 24, 16)
    val ui2 = OnFrontAt(mode_to_image(s.mode), ui, WIDTH.toFloat - h, 0)

    Below(edition_window, ui2)

  }

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                            FONCTIONS PUBLIQUES                           * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  // **************************************************************************** \\
  // *                              Initialisation                              * \\
  // **************************************************************************** \\

  def init: State = {

    val pencil = Pencil((false, Placing), BLACK, 0)
    val eraser = Eraser((false, Placing), 0)

    State(
      Vector(),
      customs,
      None,
      Command,
      pencil,
      eraser,
      "load images/templates/meadow.png"
    )

  }

  // **************************************************************************** \\
  // *                                 Affichage                                * \\
  // **************************************************************************** \\

  def toImage(s: State): Image = {

    state_to_image(s)
  }

  // **************************************************************************** \\
  // *                                   Arrêt                                  * \\
  // **************************************************************************** \\

  /** @param s l'état de l'application.
    * @return la condition d'arrêt de l'application.
    */
  def stopWhen(s: State): Boolean = {

    false // pas de condition d'arrêt.
  }

  // **************************************************************************** \\
  // *                                 Événements                               * \\
  // **************************************************************************** \\

  /** @param s l'état de l'application.
    * @param e un événement.
    * @return l'état de l'application une fois que l'événement a été assimilé.
    */
  def react(s: State, e: Event): State = {

    s.mode match {

      case Command => MCommand.update_state_command(s, e)

      case Edit => MEdit.update_state_edit(s, e)

      case Move => MMove.update_state_move(s, e)

      case Draw => MDraw.update_state_draw(s, e)

      case Erase => MErase.update_state_draw(s, e)

      case Eyedropper => MEyedropper.update_state_eyedropper(s, e)

      case Select => MSelect.update_state_select(s, e)

    }

  }

}
