package PRO2.projet.v3

import fr.istic.scribble.*
import Utils.*

/* Fichier contenant toutes les fonctions de l'éditeur photo
   en lien avec la modification des éléments (QTE). */

private sealed trait QTArea
private case object NO extends QTArea
private case object NE extends QTArea
private case object SE extends QTArea
private case object SO extends QTArea
private case object Leaf extends QTArea

class UtilsElements(service_QT: Quadtrees) {

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                             FONCTIONS PRIVEES                            * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  /** @param area un coin d'un noeud d'un quadtree, ou une feuille.
    * @param node le noeud d'un quadtree.
    * @return l'enfant de node en fonction d'area.
    */
  private def get_accessed_area(area: QTArea, node: N): QT = {
    area match {
      case NO   => node.no
      case NE   => node.ne
      case SE   => node.se
      case SO   => node.so
      case Leaf => node
    }
  }

  /** @param qt un quadtree.
    * @param area le coin du quadtree par lequel access_pixel est passé.
    * @return le coin du quadtree accédé par access_pixel, composé d'une unique feuille.
    * @note l'idée ici est de remonter le pixel jusqu'à la racine pour ensuite accéder à sa couleur.
    */
  private def get_pixel(qt: QT, area: QTArea): QT = {
    qt match {
      case C(c)              => C(c)
      case N(no, ne, se, so) => get_accessed_area(area, N(no, ne, se, so))
    }
  }

  /** @param color une couleur.
    * @param qt un quadtree.
    * @param area le coin du quadtree par lequel access_pixel est passé.
    * @return le quadtree où le pixel voulu est colorié (en fonction la taille du crayon).
    */
  private def set_pixel(color: Color)(qt: QT, area: QTArea): QT = {
    if area == Leaf then C(color)
    else
      qt match {
        case C(c)              => C(color)
        case N(no, ne, se, so) => qt
      }
  }

  /** @param pixel la position du pixel dans la matrice associée au quadtree.
    * @param f la fonction à appliquer (get ou set).
    * @param length un entier tel que le quadtree est de taille 2^length.
    * @param n le nombre d'étapes restantes.
    * @param p la position du curseur dans la matrice associée au quadtree.
    * @param qt le quadtree.
    * @return l'image par f de la paire (p, qt) appliquée récursivement de la
    *         racine de qt jusqu'à la feuille associée au pixel cliqué.
    * @note cette fonction permet de combiner get_pixel et set_pixel.
    */
  def access_pixel(pixel: Position, f: (QT, QTArea) => QT, length: Int, n: Int)(
      p: Position,
      qt: QT
  ): QT = {

    (qt, n) match {

      // Résolution maximale => c'est CE pixel que l'on modifie.
      case (_, 0) => f(qt, Leaf)

      case (C(c), _) => {
        val node = N(C(c), C(c), C(c), C(c))
        access_pixel(pixel, f, length, n)(p, node)
      }

      // Récursion sur les noeuds du quadtree.
      case (N(no, ne, se, so), _) => {

        val (i, j) = pixel
        val (x, y) = p
        val d = math.pow(2, length - 2).toInt
        val a = access_pixel(pixel, f, length - 1, n - 1)

        (i < x, j < y) match {
          case (true, true)   => f(N(a((x - d, y - d), no), ne, se, so), NO)
          case (false, true)  => f(N(no, a((x + d, y - d), ne), se, so), NE)
          case (false, false) => f(N(no, ne, a((x + d, y + d), se), so), SE)
          case (true, false)  => f(N(no, ne, se, a((x - d, y + d), so)), SO)
        }

      }
    }
  }

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                            FONCTIONS PUBLIQUES                           * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  // **************************************************************************** \\
  // *                             Éléments -> image                            * \\
  // **************************************************************************** \\

  /** @param elements un tableau des images chargées.
    * @return l'affichage de ces images en fonction de leurs paramètres.
    */
  def convert_to_image(elements: Vector[QTE], n: Int): Image = {

    elements match {

      case e +: es => {

        // Contour rouge en cas de sélection.
        val length = math.pow(2, e.size_order).toFloat
        val edges_color = if n == 0 then RED else TRANSPARENT
        val selected_edges = LineColor(Rectangle(length, length), edges_color)

        // Superposition des images.
        val top = service_QT.quadtree_to_image(e.quadtree, e.grid, e.size_order)
        val top_focus = OnFrontAt(selected_edges, top, 0, 0)
        val bot = convert_to_image(es, n - 1)

        // Déterminer les coordonnées de placement.
        val i = (e.position._1 - length / 2).toFloat
        val j = (e.position._2 - length / 2).toFloat

        OnFrontAt(top_focus, bot, i, j)

      }

      case _ => Empty
    }
  }

  // **************************************************************************** \\
  // *                    Sélectionner un élément selon un click                * \\
  // **************************************************************************** \\

  /** @param elements le tableau des éléments chargés.
    * @param c la position du curseur.
    * @param i l'index d'un élément chargé.
    * @return l'élément affiché sur le pixel c (éventuellement aucun).
    */
  def get_from_click(
      elements: Vector[QTE],
      c: Position,
      i: Int
  ): Option[Int] = {

    elements match {

      case e +: es => {

        val l = math.pow(2, e.size_order).toInt
        val top_left = (e.position._1 - l / 2, e.position._2 - l / 2)

        if is_in_rect(top_left, (l, l), c) then return Some(i)
        else get_from_click(es, c, i + 1)

      }

      case _ => None

    }
  }

  // **************************************************************************** \\
  // *                        Mettre à jour un paramètre                        * \\
  // **************************************************************************** \\

  /** @param f une fonction de transformation de quadtree.
    * @param qte un élément quadtree.
    * @return le qte dont le quadtree a reçu la transformation f.
    */
  def update_quadtree(f: Transformation)(qte: QTE): QTE = {

    qte.copy(quadtree = service_QT.transform(qte.quadtree, f :: Nil))
  }

  /** @param c une position.
    * @param qte un élément quadtree.
    * @return le qte de position c.
    */
  def update_position(c: Position)(qte: QTE): QTE = {

    qte.copy(position = c)
  }

  /** @param c une position.
    * @param qte un élément quadtree.
    * @return le qte de taille qte.size_order + x, où x = 1 ou -1.
    * @note size est compris entre 1 et 9.
    */
  def update_size(x: Int)(qte: QTE): QTE = {

    qte.copy(size_order = math.min(math.max(qte.size_order + x, 1), 9))
  }

  /**
    * @param qte un élément quadtree.
    * @return le qte dont l'affichage grid est opposée.
    */
  def update_grid(qte: QTE): QTE = {

    qte.copy(grid = !qte.grid)
  }

  /**
    * @param s l'état de l'application.
    * @param f une fonction à appliquer sur un élément quadtree.
    * @return s après que f soit appliquée sur l'élément sélectionné
    *         s.selected, s'il y en a un.
    */
  def update_selected(s: State, f: QTE => QTE): State = {
    s.selected match {
      case None    => s
      case Some(i) => s.copy(elements = s.elements.updated(i, f(s.elements(i))))
    }
  }

  /**
    * @param elements le tableau des éléments quadtrees.
    * @param i l'indice d'un élément, ou -1.
    * @return elements sans l'élément d'indice i, s'il y en a un.
    */
  def delete_element(elements: Vector[QTE], i: Int): Vector[QTE] = {
    elements match {
      case e +: es if i == 0 => es
      case e +: es           => e +: delete_element(es, i - 1)
      case _                 => elements
    }
  }

  // **************************************************************************** \\
  // *                           Actions sur un pixel                           * \\
  // **************************************************************************** \\

  /** @param qte un élément quadtree de l'éditeur.
    * @param pixel les coordonnées du pixel cliqué sur l'écran.
    * @param scale l'échelle utilisée (get => résolution qt, set => taille crayon).
    * @return l'élément qte où la feuille correspondante à pixel est accédée et où
    *         à chaque appel récursif la fonction f est appelée sur le noeud accédé.
    */
  def access_pixel_of_element(
      qte: QTE,
      pixel: Position,
      scale: Int,
      f: (QT, QTArea) => QT
  ): QT = {

    // Coordonnées des paramètres.

    val (qte_x, qte_y) = qte.position
    val (pixel_x, pixel_y) = pixel

    // Caractéristiques du quadtree sélectionné.
    
    val qt = service_QT.decompress(qte.quadtree, qte.size_first)
    val qt_h = hauteur(qt)
    val qt_l = qt_h + 1
    val qt_length = math.pow(2, qt_l).toInt

    // Coordonnées dans la fenêtre.

    val pixel_length = math.pow(2, qte.size_order).toInt
    val coef_zoom = pixel_length / qt_length
    val qte_length = qt_length * coef_zoom

    val top_left_x = qte_x - qte_length / 2
    val top_left_y = qte_y - qte_length / 2

    // Coordonnées dans le quadtree.

    val qt_pixel_x = (pixel_x.toInt - top_left_x) / coef_zoom
    val qt_pixel_y = (pixel_y.toInt - top_left_y) / coef_zoom
    val qt_pixel = (qt_pixel_x, qt_pixel_y)

    val qt_center = (qt_length / 2, qt_length / 2)

    // Nombre de récursions avant de considérer le pixel atteint.
    val n = math.max(qt_l - scale, 0) 

    service_QT.compress(access_pixel(qt_pixel, f, qt_l, n)(qt_center, qt))

  }

  /**
    * @param qte un élément quadtree.
    * @param pixel les coordonnées du pixel cliqué.
    * @param color la couleur à appliquer.
    * @param n la taille du pixel à appliquer.
    * @return l'élément quadtree où la feuille associée à pixel est
    *         de couleur pencil.color et a une profondeur de pencil.size.
    * @note 2^pencil.size est alors la taille du pixel dessiné.
    */
  def set_pixel_of_element(
      qte: QTE,
      pixel: Position,
      color: Color,
      n: Int
  ): QTE = {
    val f = set_pixel(color)
    val qt = access_pixel_of_element(qte, pixel, n, f)
    qte.copy(quadtree = qt)
  }

  /**
    * @param qte un élément quadtree.
    * @param pixel les coordonnées du pixel cliqué.
    * @param pencil l'état du crayon.
    * @return la couleur de la feuille associée au pixel.
    */
  def get_pixel_of_element(qte: QTE, pixel: Position, pencil: Pencil): Color = {
    val qt = access_pixel_of_element(qte, pixel, 0, get_pixel)
    qt match {
      case C(c) => c
      case _    => throw Exception("Accessed pixel was not a leaf but a node.")
    }
  }

}
