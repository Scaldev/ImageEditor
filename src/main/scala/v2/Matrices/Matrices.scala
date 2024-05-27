package PRO2.projet.v2
import fr.istic.scribble.*

trait Matrices {

  type T[Elt]

  /** @param n un entier naturel.
    * @param p un entier naturel.
    * @param e un élément.
    * @return la matrice de taille n * p ne contenant que des éléments e.
    */
  def init_matrix[Elt](n: Int, p: Int, e: Elt): T[Elt]

  /** @param m une matrice.
    * @return le couple (hauteur, largeur) de la matrice.
    */
  def get_dimensions[Elt](m: T[Elt]): (Int, Int)

  /** @param m une matrice de taille n * p.
    * @param i un numéro de ligne.
    * @param j un numéro de colonne.
    * @return Some(e), où e est l'élément de m à la position (i, j)
    *         si une telle position est valide, None sinon.
    */
  def get_element[Elt](m: T[Elt], i: Int, j: Int): Option[Elt]

  /** @param m une matrice.
    * @param i le numéro de la ligne.
    * @param j le numéro de la colonne.
    * @param e l'élement.
    * @return la matrice m où si un élément a les coordonnées (i, j), il vaut désormais e.
    */
  def set_element[Elt](m: T[Elt], i: Int, j: Int, e: Elt): T[Elt]

  /** @param lines une liste de listes de même taille.
    * @return la matrice formée de la liste de listes, si possible.
    */
  def list_to_matrix[Elt](lines: List[List[Elt]]): Option[T[Elt]]

  /** @param m une matrice.
    * @return la liste des éléments de m, ordonnés dans le sens de lecture.
    */
  def matrix_to_list[Elt](m: T[Elt]): List[Elt]

  /** @param m une matrice de taille n * p.
    * @param e l'élément comblant les nouvelles cases.
    * @return la matrice m carrée de taille max(n, p), dont les
    *         nouveaux éléments valent colorTransparent.
    */
  def matrix_to_square[Elt](m: T[Elt], e: Elt): T[Elt]
}
