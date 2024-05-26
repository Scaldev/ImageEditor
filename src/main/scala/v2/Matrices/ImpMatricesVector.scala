package PRO2.projet.v2

import fr.istic.scribble.*

object ImpMatricesVector extends Matrices {

  opaque type T[Elt] = Vector[Vector[Elt]]

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                             FONCTIONS PRIVEES                            * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  /** @param p un entier naturel.
    * @param e un élément.
    * @return le vecteur de taille p contenant uniquement l'élément e.
    */
  private def init_vector[Elt](p: Int, e: Elt): Vector[Elt] = {

    Vector.fill[Elt](p)(e)
  }

  /** @param m une matrice.
    * @param nl la nouvelle ligne.
    * @param i l'emplacement de nl.
    * @return la matrice m où la i-ème ligne vaut nl.
    * @note si m possède moins de i lignes, elle reste inchangée.
    */
  private def set_matrix_line[Elt](m: T[Elt], nl: Vector[Elt], i: Int): T[Elt] = {

    m match {

      case l :+ ls => {
        if i == 0 then Vector(nl) :+ ls
        else set_matrix_line(Vector(ls), nl, i - 1)
      }

      case _ => m
    }
  }

  /** @param v un vecteur.
    * @param e le nouvel élément.
    * @param i l'indice du nouvel élément.
    * @return le vecteur v où le i-ème élément vaut e.
    * @note par convention, le premier élément de v est à l'indice 0.
    *       si le vecteur a moins de i éléments, elle reste inchangée.
    */
  private def set_vector_element[Elt](v: Vector[Elt], e: Elt, i: Int): Vector[Elt] = {

    v match {

      case x :+ xs => {
        if i == 0 then Vector(e) :+ xs
        else set_vector_element(Vector(xs), e, i - 1)
      }

      case _     => v
    }
  }

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                            FONCTIONS PUBLIQUES                           * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  /** @param n un entier naturel.
    * @param p un entier naturel.
    * @param e un élément.
    * @return la matrice de taille n * p ne contenant que des éléments e.
    */
  def init_matrix[Elt](n: Int, p: Int, e: Elt): T[Elt] = {

    n match {
      case 0 => Vector()
      case _ => init_matrix(n - 1, p, e) :+ init_vector(p, e)
    }
    
  }

  /** @param m une matrice.
    * @return le couple (hauteur, largeur) de la matrice.
    */
  def get_dimensions[Elt](m: T[Elt]): (Int, Int) = {
    m match {
      case l :+ ls => (m.length, ls.length)
      case _       => (0, 0)
    }
  }

  /** @param m une matrice de taille n * p.
    * @param i un entier naturel.
    * @param j un entier naturel.
    * @return Some(e), où e est l'élément de m à la position (i, j)
    *         si une telle position est valide, None sinon.
    */
  def get_element[Elt](m: T[Elt], i: Int, j: Int): Option[Elt] = {

    val (n, p) = get_dimensions(m)

    if (i < n && j < p) then Some(m(i)(j))
    else None
  }

  /** @param m une matrice.
    * @param i le numéro de la ligne.
    * @param j le numéro de la colonne.
    * @param e l'élement.
    * @return la matrice m où si un élément a les coordonnées (i, j), il vaut désormais e.
    */
  def set_element[Elt](m: T[Elt], i: Int, j: Int, e: Elt): T[Elt] = {

    val (n, _) = get_dimensions(m)
    
    if i >= n then {
      m
    } else {
      val nl: Vector[Elt] = set_vector_element(m(i), e, j)
      set_matrix_line(m, nl, i)
    }
    
  }
  
  /** @param m une matrice.
    * @return la liste des éléments de m, ordonnés ligne par ligne.
    */
  def matrix_to_list[Elt](m: T[Elt]): List[Elt] = {
    m.foldRight(Nil: List[Elt])((l, acc) => l.toList ++ acc)
  }

}
