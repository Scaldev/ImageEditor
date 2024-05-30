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

        val length = math.pow(2, e.size_order).toFloat

        // Superposition des images.
        val top = service_QT.quadtree_to_image(e.quadtree, e.grid, e.size_order)
        val bot = convert_to_image(es, n - 1)

        // Déterminer les coordonnées de placement.

        val i = (e.position._1 - length / 2).toFloat
        val j = (e.position._2 - length / 2).toFloat

        OnFrontAt(
          OnFrontAt(
            LineColor(Rectangle(length, length), edges_color),
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

  /**
    * @param qt un quadtree carré.
    * @param color la couleur appliquée.
    * @param pixel les coordonnées du pixel cliqué.
    * @param p la position dans la matrice associée à la racine de qt.
    * @param n le nombre de récursions avant de toucher les feuilles de qt.
    * @return qt où la feuille associée au pixel cliqué est désormais de couleur color.
    */
  def update_pixel(
      qt: QT,
      color: Color,
      pixel: Position,
      p: Position,
      n: Int,
  ): QT = {

    val (i, j) = pixel
    val (x, y) = p

    (qt, n) match {

      // Résolution maximale => c'est CE pixel que l'on modifie.
      case (_, 0) => C(color)

      // On est pas encore à la résolution maximale => refaire en décompressant la feuille.
      case (C(c), _) => {
        val node = N(C(c), C(c), C(c), C(c))
        update_pixel(node, color, pixel, p, n)
      }

      // Récursion sur les noeuds du quadtree.
      case (N(no, ne, se, so), _) => {

        val d = math.pow(2, n - 2).toInt

        // HAUT-GAUCHE
        if i < x && j < y then {
          val new_no = update_pixel(no, color, pixel, (x - d, y - d), n - 1)
          N(new_no, ne, se, so)

        // HAUT-DROITE
        } else if i < x && j >= y then {
          val new_ne = update_pixel(ne, color, pixel, (x - d, y + d), n - 1)
          N(no, new_ne, se, so)

        // BAS-GAUCHE
        } else if i >= x && j >= y then {
          val new_no = update_pixel(se, color, pixel, (x + d, y + d), n - 1)
          N(no, ne, new_no, so)

        // BAS-DROITE
        } else {
          val new_so = update_pixel(so, color, pixel, (x + d, y - d), n - 1)
          N(no, ne, se, new_so)
        }
      }

    }
  }

}
