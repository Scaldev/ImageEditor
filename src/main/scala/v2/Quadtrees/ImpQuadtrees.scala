package PRO2.projet.v2

import fr.istic.scribble.*
import MatrixConversions.*

/* Implémentation des matrices à utiliser. */
val serv_M: Matrices = ImpMatricesVector

object ImpQuadtrees extends Quadtrees {

  private val GRID_COLOR: Color = RED
  private type RGB = (Int, Int, Int)

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                             FONCTIONS PRIVEES                            * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  // **************************************************************************** \\
  // *                             quadtree_to_image                            * \\
  // **************************************************************************** \\

  /** @param color la couleur de l'image.
    * @param grid si la grille des subdivisions est affichée ou non.
    * @param length la longueur et largeur de l'image.
    * @return l'image de taille length, de couleur fc et contourée si grid == true.
    */
  private def make_area(color: Color, grid: Boolean, length: Float): Image = {

    val colored_rectangle: Image = FillColor(Rectangle(length, length), color)
    val color_line: Color = if (grid) then RED else color
    LineColor(colored_rectangle, color_line)
  }

  /** @param qt le quadtree représentant une image.
    * @param grid indique si on affiche la grille des subdivisions ou non.
    * @param length le nombre de pixels de longueur de l'image.
    * @return l'image de longueur length basée du quadtree qt.
    */
  private def qt_to_img_aux(qt: QT, grid: Boolean, length: Float): Image = {

    qt match {

      // Cas de base : toute la zone n'est qu'une seule couleur.
      case C(c) => make_area(c, grid, length)

      // Cas récursif : diviser la zone en 4 sous-zones, puis régner (les réunir).
      case N(no, ne, se, so) => {

        val img_no: Image = qt_to_img_aux(no, grid, length / 2)
        val img_ne: Image = qt_to_img_aux(ne, grid, length / 2)
        val img_se: Image = qt_to_img_aux(se, grid, length / 2)
        val img_so: Image = qt_to_img_aux(so, grid, length / 2)

        Below(Beside(img_no, img_ne), Beside(img_so, img_se))
      }

    }

  }

  // **************************************************************************** \\
  // *                                  compress                                * \\
  // **************************************************************************** \\

  /** @param qt un quadtree.
    * @param transfo une fonction de transformation de quadtree.
    * @return le quadtree qt après la transformation transfo.
    */
  private def compress_node(no: QT, ne: QT, se: QT, so: QT): QT = {

    (no, ne, se, so) match {
      case (C(a), C(b), C(c), C(d)) if a == b && b == c && c == d => C(a)
      case _ => N(no, ne, se, so)
    }

  }

  // **************************************************************************** \\
  // *                        transformations prédéfinies                       * \\
  // **************************************************************************** \\

  /** @param v l'une des valeurs d'une couleur (rouge, bleu ou vert).
    * @param coef le taux de lumonosité par lequel multiplier v.
    * @return la valeur v multiplée par coef, en restant entre 0 et 255.
    */
  private def brightness(r: Int, g: Int, b: Int, coef: Double): RGB = {
    def f(x: Int) = math.min((x * coef).toInt, 255)
    (f(r), f(g), f(b))
  }

  /** @param c une couleur.
    * @return la couleur c en nuance de gris.
    */
  private def color_gray(c: Color): Color = {
    val gray: Int = (c.red + c.green + c.blue) / 3
    Color(gray, gray, gray, 255)
  }

  /** @param coef le taux de lumonosité par lequel multiplier chaque couleur.
    * @param c une couleur.
    * @return la couleur c dont la luminosité a été modifiée selon coef.
    */
  private def color_map(coef: Double)(c: Color): Color = {
    val (r, g, b): RGB = brightness(c.red, c.green, c.blue, coef)
    Color(r, g, b, 255)
  }

  /** @param qt un quadtree.
    * @param color_map une fonction associant une couleur à sa nouvelle valeur.
    * @return le quadtree où toutes les couleurs sont passées par color_map.
    */
  private def transfo_color(qt: QT, color_map: Color => Color): QT = {
    qt match {
      case C(c) => C(color_map(c))
      case _    => qt
    }
  }

  /** @param qt un quadtree.
    * @param node_map une fonction associant un noeur à sa nouvelle valeur
    * @return le quadtree où tous les noeuds sont passés par node_map.
    */
  private def transfo_subdiv(qt: QT, node_map: N => N): QT = {
    qt match {
      case N(no, ne, se, so) => node_map(N(no, ne, se, so))
      case _                 => qt
    }
  }

  /** @param qt un quadtree.
    * @param f une transformation.
    * @return le quadtree après la transformation f.
    */
  private def apply_transform(qt: QT, f: Transformation): QT = {

    qt match {

      case C(c) => f(qt)

      case N(no, ne, se, so) => {

        val tno: QT = apply_transform(no, f)
        val tne: QT = apply_transform(ne, f)
        val tse: QT = apply_transform(se, f)
        val tso: QT = apply_transform(so, f)
        f(N(tno, tne, tse, tso))
      }

    }

  }

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                            FONCTIONS PUBLIQUES                           * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  // **************************************************************************** \\
  // *                                quadtrees                                 * \\
  // **************************************************************************** \\

  def quadtree_to_image(qt: QT, grid: Boolean, size_order: Int): Image = {

    // L'image doit avoir une longueur et une largeur positives.
    if size_order < 1 then {
      throw Exception("Invalid input: size_order < 1.")
    }
    val length: Float = math.pow(2.0, size_order.toDouble).toFloat
    qt_to_img_aux(qt, grid, length)

  }

  def compress(qt: QT): QT = {
    qt match {
      case C(c) => C(c)
      case N(no, ne, se, so) =>
        compress_node(compress(no), compress(ne), compress(se), compress(so))
    }
  }

  // **************************************************************************** \\
  // *                        transformations prédéfinies                       * \\
  // **************************************************************************** \\

  def rotation_left(qt: QT): QT = {
    transfo_subdiv(qt, n => N(n.ne, n.se, n.so, n.no))
  }

  def rotation_right(qt: QT): QT = {
    transfo_subdiv(qt, n => N(n.so, n.no, n.ne, n.se))
  }

  def flip_vertical(qt: QT): QT = {
    transfo_subdiv(qt, n => N(n.ne, n.no, n.so, n.se))
  }

  def flip_horizontal(qt: QT): QT = {
    transfo_subdiv(qt, n => N(n.so, n.se, n.ne, n.no))
  }

  def gray_shades(qt: QT): QT = {
    transfo_color(qt, color_gray)
  }

  def lighten(qt: QT): QT = {
    transfo_color(qt, color_map(1.2))
  }

  def darken(qt: QT): QT = {
    transfo_color(qt, color_map(0.8))
  }

  // **************************************************************************** \\
  // *                                transformations                           * \\
  // **************************************************************************** \\

  def transform(qt: QT, fs: List[Transformation]): QT = {
    fs.foldLeft(qt)((acc, f) => apply_transform(acc, f))
  }

  // **************************************************************************** \\
  // *                               file_to_quadtree                           * \\
  // **************************************************************************** \\

  /**
    * @param filename le nom du fichier d'image (chemin relatif au projet sbt)
    *                 au format jpg ou png.
    * @return le quadtree associé à l'image.
    */
  def file_to_quadtree(filename: String): QT = {

    println("Image to matrix")
    val m: serv_M.T[Color] = image_to_matrix(filename)
    
    println("\nMatrix to quadtree")
    val qt: QT = matrix_to_quadtree(m)
    
    compress(qt)
  }

}
