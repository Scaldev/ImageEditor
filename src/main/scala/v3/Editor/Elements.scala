package PRO2.projet.v3

import fr.istic.scribble.*
import Utils.*

class UtilsElements(service_QT: Quadtrees) {

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

  // **************************************************************************** \\
  // *                             Éléments -> image                            * \\
  // **************************************************************************** \\

  /** @param elements un tableau des images chargées.
    * @return l'affichage de ces images en fonction de leurs paramètres.
    */
  def convert_to_image(elements: Vector[QTE], n: Int): Image = {

    elements match {

      case e +: es => {

        val edges_color = if n == 0 then RED else TRANSPARENT

        val s = get_size_order(e.dimensions._1, e.dimensions._2)

        // Superposition des images.
        val top = service_QT.quadtree_to_image(e.quadtree, e.grid, e.size_order)
        val bot = convert_to_image(es, n - 1)

        // Déterminer les coordonnées de placement.
        val length = math.pow(2, e.size_order).toInt
        val coef_edges_length = math.pow(2, e.size_order - s)

        val i = (e.position._1 - length / 2).toFloat
        val j = (e.position._2 - length / 2).toFloat

        val edges_width = (e.dimensions._1 * coef_edges_length).toFloat
        val edges_height = (e.dimensions._2 * coef_edges_length).toFloat

        OnFrontAt(
          OnFrontAt(
            LineColor(Rectangle(edges_width, edges_height), edges_color),
            top,
            0,
            0
          ),
          bot,
          i,
          j
        )

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
    * @return le premier élément chargé dont un pixel est c (éventuellement aucun).
    */
  def get_from_click(
      elements: Vector[QTE],
      c: Position,
      i: Int
  ): Option[Int] = {

    elements match {

      case e +: es => {

        val l = math.pow(2, e.size_order).toInt
        val e_i = e.position._1 - l / 2
        val e_j = e.position._2 - l / 2

        if is_in_rect((e_i, e_j), (l, l), c) then return Some(i)
        else get_from_click(es, c, i + 1)

      }

      case _ => None

    }
  }

  // **************************************************************************** \\
  // *                        Mettre à jour un élément                          * \\
  // **************************************************************************** \\

  def update_quadtree(f: Transformation)(qte: QTE): QTE = {

    qte.copy(quadtree = service_QT.transform(qte.quadtree, f :: Nil))
  }

  def update_position(c: Position)(qte: QTE): QTE = {

    qte.copy(position = c)
  }

  def update_size(x: Int)(qte: QTE): QTE = {

    qte.copy(size_order = math.min(math.max(qte.size_order + x, 1), 10))
  }

  def update_grid(qte: QTE): QTE = {

    qte.copy(grid = !qte.grid)
  }

  def update_selected(s: State, f: QTE => QTE): State = {

    s.selected match {
      case None    => s
      case Some(i) => s.copy(elements = s.elements.updated(i, f(s.elements(i))))
    }

  }

  // ************************** \\

  def update_pixel(
      qt: QT,
      n: Int,
      c: Position,
      p: Position,
      color: Color,
      coef: Int
  ): QT = {

    (qt, n) match {

      case (N(no, ne, se, so), _) => {

        val (x, y) = c
        val (i, j) = p
        val dc = math.floor(math.pow(2, n) / 4).toInt * coef
        val df = math.floor(math.pow(2, n) / 4).toInt * coef

        println(s"n = $n, (x = $x, y = $y), (i = $i, j = $j), (dc = $dc, df = $df)")

        // HAUT-GAUCHE
        if i < x && j < y then {
          N(update_pixel(no, n - 1, (x - dc, y - dc), p, color, coef), ne, se, so)

          // HAUT-DROITE
        } else if i < x && j >= y then {
          N(no, update_pixel(ne, n - 1, (x - dc, y + dc), p, color, coef), se, so)

          // BAS-GAUCHE
        } else if i >= x && j >= y then {
          N(no, ne, update_pixel(se, n - 1, (x + dc, y + dc), p, color, coef), so)

          // BAS-DROITE
        } else {
          N(no, ne, se, update_pixel(so, n / 2, (x + dc, y - dc), p, color, coef))

        }
      }

      case _ => {
        println("rip")
        C(color)
      }

    }
  }

}
