package PRO2.projet.v3

import fr.istic.scribble.*
import java.util.Scanner;

import MatrixConversions.*

object ProgrammeUtilisateur extends App {

  val service_QT: Quadtrees = ImpQuadtrees

  // **************************************************************************** \\
  // *                                  Fonctions                               * \\
  // **************************************************************************** \\

  /** @param f une transformation.
    * @param n le nombre de fois où f doit être appliquée.
    * @return la liste contenant n fois la fonction f.
    */
  def repeat(f: Transformation, n: Int): List[Transformation] = {
    n match {
      case 0 => Nil
      case _ => f :: repeat(f, n - 1)
    }
    (1 to n).toList.map(_ => f)
  }

  /** @param qt un quadtree.
    * @return un quadtree dont les valeurs des couleurs ont été modifiées chaotiquement.
    */
  def color_chaos(qt: QT): QT = {
    qt match {
      case C(Color(r, g, b, a)) => C(Color(g, b, r, a))
      case _                    => qt
    }
  }

  /** @param qt un quadtree.
    * @return le quadtree après une série de transformations choisies.
    */
  def my_transformation(qt: QT) = {

    val transfos = 

      service_QT.rotation_right
      // :: service_QT.flip_horizontal
      // :: service_QT.flip_vertical
      // :: color_chaos
      // :: service_QT.darken
      // :: service_QT.darken
      :: Nil

    service_QT.transform(qt, transfos)

  }

  // **************************************************************************** \\
  // *                                Manipulation                              * \\
  // **************************************************************************** \\

  // Paramètres
  val grid: Boolean = false
  val size_order: Int = 9

  // Chaîne de traitement
  val quadtree = service_QT.file_to_quadtree("images/templates/meadow.png")

  val quadtree_transformed = my_transformation(quadtree)

  val image = service_QT.quadtree_to_image(quadtree_transformed, grid, size_order)

  // draw(image)

  // **************************************************************************** \\
  // *                          Application réactive                            * \\
  // **************************************************************************** \\

  bigbang(ImageEditor(service_QT))


  /*
  val scanner = new Scanner(System.in)
  println("Enter your name : ")
  val a = scanner.nextLine()
  println("My name is : "+a)
   */
}
