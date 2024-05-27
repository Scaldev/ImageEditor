package PRO2.projet.v2

import fr.istic.scribble.*

import MatrixConversions.*

class MySuite extends munit.FunSuite {

  // Valeurs globales.
  val dsize: Int = 9
  val dlength: Float = 512.0

  // Services.
  val service_QT: Quadtrees = ImpQuadtrees

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                                  QUADTREES                               * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  // **************************************************************************** \\
  // *                          	  quadtree_to_image                     	    * \\
  // **************************************************************************** \\

  test("Quadtrees : quadtree_to_image -> zone unicolore sans contour") {

    val obtained = service_QT.quadtree_to_image(C(WHITE), false, dsize)

    val expected =
      LineColor(FillColor(Rectangle(dlength, dlength), WHITE), WHITE)

    assertEquals(obtained, expected)

  }

  test("Quadtrees : quadtree_to_image -> zone unicolore avec contour") {

    val obtained = service_QT.quadtree_to_image(C(WHITE), true, dsize)

    val expected = LineColor(FillColor(Rectangle(dlength, dlength), WHITE), RED)

    assertEquals(obtained, expected)

  }

  test("Quadtrees : quadtree_to_image -> image non-compressee") {

    val qt: QT = N(C(WHITE), C(WHITE), C(WHITE), C(WHITE))
    val obtained = service_QT.quadtree_to_image(qt, false, dsize)

    val subrect: Image = FillColor(Rectangle(dlength / 2, dlength / 2), WHITE)
    val dsubdiv: Image = LineColor(subrect, WHITE)
    val expected = Below(Beside(dsubdiv, dsubdiv), Beside(dsubdiv, dsubdiv))

    assertEquals(obtained, expected)

  }

  test("Quadtrees : quadtree_to_image -> image non-unicolore") {

    // Obtained.
    // Remarque : c'est l'exemple du cours !

    val qt: QT = N(
      C(BLACK),
      N(C(BLACK), C(BLACK), C(WHITE), C(WHITE)),
      C(WHITE),
      N(C(BLACK), C(WHITE), C(WHITE), C(WHITE))
    )

    val obtained = service_QT.quadtree_to_image(qt, false, dsize)

    // Expected.
    // La fonction make_area ayant été testée avant, on fait l'hypothèse
    // qu'elle fonctionne déjà afin d'alléger le code.

    val sdiv_white: Image =
      LineColor(FillColor(Rectangle(dlength / 2, dlength / 2), WHITE), WHITE)
    val sdiv_black: Image =
      LineColor(FillColor(Rectangle(dlength / 2, dlength / 2), BLACK), BLACK)

    val ssdiv_white: Image =
      LineColor(FillColor(Rectangle(dlength / 4, dlength / 4), WHITE), WHITE)
    val ssdiv_black: Image =
      LineColor(FillColor(Rectangle(dlength / 4, dlength / 4), BLACK), BLACK)

    val expected = Below(
      Beside(
        sdiv_black,
        Below(
          Beside(ssdiv_black, ssdiv_black),
          Beside(ssdiv_white, ssdiv_white)
        )
      ),
      Beside(
        Below(
          Beside(ssdiv_black, ssdiv_white),
          Beside(ssdiv_white, ssdiv_white)
        ),
        sdiv_white
      )
    )
    assertEquals(obtained, expected)

  }

  // **************************************************************************** \\
  // *                          	      compress                     	          * \\
  // **************************************************************************** \\

  test("Quadtrees : compress -> noeud avec 4 feuilles de même couleur") {

    val quadtree: QT = N(C(WHITE), C(WHITE), C(WHITE), C(WHITE))

    val obtained = service_QT.compress(quadtree)

    val expected = C(WHITE)

    assertEquals(obtained, expected)

  }

  test("Quadtrees : compress -> quadtree déjà compresé") {

    val obtained = service_QT.compress(C(WHITE))

    val expected = C(WHITE)

    assertEquals(obtained, expected)

  }

  // **************************************************************************** \\
  // *                          	  transformations                     	      * \\
  // **************************************************************************** \\

  test("Quadtrees : rotation_left") {

    val quadtree: QT = N(C(RED), C(BLUE), C(BLACK), C(WHITE))
    val obtained = service_QT.rotation_left(quadtree)

    val expected = N(C(BLUE), C(BLACK), C(WHITE), C(RED))

    assertEquals(obtained, expected)

  }

  test("Quadtrees : rotation_right") {

    val quadtree: QT = N(C(RED), C(BLUE), C(BLACK), C(WHITE))
    val obtained = service_QT.rotation_right(quadtree)

    val expected = N(C(WHITE), C(RED), C(BLUE), C(BLACK))

    assertEquals(obtained, expected)

  }

  test("Quadtrees : flip_vertical") {

    val quadtree: QT = N(C(RED), C(BLUE), C(BLACK), C(WHITE))
    val obtained = service_QT.flip_vertical(quadtree)

    val expected = N(C(BLUE), C(RED), C(WHITE), C(BLACK))

    assertEquals(obtained, expected)

  }

  test("Quadtrees : flip_horizontal") {

    val quadtree: QT = N(C(RED), C(BLUE), C(BLACK), C(WHITE))
    val obtained = service_QT.flip_horizontal(quadtree)

    val expected = N(C(WHITE), C(BLACK), C(BLUE), C(RED))

    assertEquals(obtained, expected)

  }

  test("Quadtrees : transform") {

    val quadtree: QT = N(C(RED), C(BLUE), C(BLACK), C(WHITE))
    val transfos: List[Transformation] =
      service_QT.rotation_left :: service_QT.flip_vertical :: Nil

    val obtained = service_QT.transform(quadtree, transfos)

    val expected = N(C(BLACK), C(BLUE), C(RED), C(WHITE))

    assertEquals(obtained, expected)

  }

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                                  MATRICES                                * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  /* Afin de pouvoir tester facilement les fonctions de l'interface Matrices,
     on admet que la fonction matrix_to_list soit correcte.
     Dans chacune des implémentations, sa correction se montre facilement.
   */

  // **************************************************************************** \\
  // *                              get_dimensions                        	    * \\
  // **************************************************************************** \\

  test("Matrices : get_dimensions") {

    val m = service_M.init_matrix(2, 3, 0)

    val obtained = service_M.get_dimensions(m)
    val expected = (2, 3)

    assertEquals(obtained, expected)
  }

  // **************************************************************************** \\
  // *                                get_element                          	    * \\
  // **************************************************************************** \\

  test("Matrices : get_element -> élément présent") {

    val m = service_M.init_matrix(2, 3, 0)

    val obtained = service_M.get_element(m, 1, 2)
    val expected = Some(0)

    assertEquals(obtained, expected)
  }

  test("Matrices : get_element -> élément absent") {

    val m = service_M.init_matrix(2, 3, 0)

    val obtained = service_M.get_element(m, 2, 2)
    val expected = None

    assertEquals(obtained, expected)
  }

  // **************************************************************************** \\
  // *                                set_element                          	    * \\
  // **************************************************************************** \\

  test("Matrices : set_element -> élément présent") {

    val m = service_M.init_matrix(2, 3, 0)

    val obtained = service_M.matrix_to_list(service_M.set_element(m, 1, 2, 42))
    val expected = 0 :: 0 :: 0 :: 0 :: 0 :: 42 :: Nil

    assertEquals(obtained, expected)
  }

  test("Matrices : set_element -> élément absent") {

    val m = service_M.init_matrix(2, 3, 0)

    val obtained = service_M.matrix_to_list(service_M.set_element(m, 2, 2, 42))
    val expected = 0 :: 0 :: 0 :: 0 :: 0 :: 0 :: Nil

    assertEquals(obtained, expected)
  }

  // **************************************************************************** \\
  // *                              matrix_to_list                          	  * \\
  // **************************************************************************** \\

  test("Matrices : matrix_to_list") {

    val l = (1 :: 0 :: Nil) :: (0 :: 2 :: Nil) :: Nil

    val m0 = service_M.init_matrix(2, 2, 0)
    val m1 = service_M.set_element(m0, 0, 0, 1)
    val m2 = service_M.set_element(m1, 1, 1, 2)

    val obtained = service_M.list_to_matrix(l)
    val expected = m2

    assertEquals(obtained, expected)
  }

  // **************************************************************************** \\
  // *                              matrix_to_list                          	  * \\
  // **************************************************************************** \\

  test("Matrices : matrix_to_list") {

    val m0 = service_M.init_matrix(2, 2, 0)
    val m1 = service_M.set_element(m0, 0, 0, 1)
    val m2 = service_M.set_element(m1, 1, 1, 2)

    val obtained = service_M.matrix_to_list(m2)
    val expected = 1 :: 0 :: 0 :: 2 :: Nil

    assertEquals(obtained, expected)
  }

  // **************************************************************************** \\
  // *                            matrix_to_square                          	  * \\
  // **************************************************************************** \\

  test("Matrices : matrix_to_square -> matrice déjà carrée") {

    val m = service_M.init_matrix(2, 2, 1)

    val obtained = service_M.matrix_to_list(service_M.matrix_to_square(m, 0))
    val expected = 1 :: 1 :: 1 :: 1 :: Nil

    assertEquals(obtained, expected)
  }

  test("Matrices : matrix_to_square -> matrice plus longue") {

    val m = service_M.init_matrix(1, 2, 1)

    val obtained = service_M.matrix_to_list(service_M.matrix_to_square(m, 0))
    val expected = 1 :: 1 :: 0 :: 0 :: Nil

    assertEquals(obtained, expected)
  }

  test("Matrices : matrix_to_square -> matrice plus haute") {

    val m = service_M.init_matrix(2, 1, 1)

    val obtained = service_M.matrix_to_list(service_M.matrix_to_square(m, 0))
    val expected = 1 :: 0 :: 1 :: 0 :: Nil

    assertEquals(obtained, expected)
  }

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                               CONVERSIONS                                * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  // **************************************************************************** \\
  // *                             image_to_matrix                              * \\
  // **************************************************************************** \\

  test("MatrixConversions : image_to_matrix -> image carrée") {

    val image = image_to_matrix("images/test/square.png")

    val obtained = service_M.matrix_to_list(image)
    val expected = RED :: GREEN :: BLACK :: BLUE :: Nil

    assertEquals(obtained, expected)
  }
  
  test("MatrixConversions : image_to_matrix -> image plus longue") {

    val image = image_to_matrix("images/test/wider.png")

    val obtained = service_M.matrix_to_list(image)
    val expected = RED :: GREEN :: BLUE :: BLACK :: Nil

    assertEquals(obtained, expected)
  }

  test("MatrixConversions : image_to_matrix -> image plus haute") {

    val image = image_to_matrix("images/test/higher.png")

    val obtained = service_M.matrix_to_list(image)
    val expected = RED :: GREEN :: BLUE :: BLACK :: Nil

    assertEquals(obtained, expected)
  }

  // **************************************************************************** \\
  // *                          matrix_to_quadtree                              * \\
  // **************************************************************************** \\

    test("MatrixConversions : matrix_to_quadtree -> matrice carrée") {

      val l = (RED :: GREEN :: Nil) :: (BLUE :: BLACK :: Nil) :: Nil
      val m = service_M.list_to_matrix(l)

      val obtained = matrix_to_quadtree(m)
      val expected = N(C(RED), C(GREEN), C(BLACK), C(BLUE))

    assertEquals(obtained, expected)

  }

    test("MatrixConversions : matrix_to_quadtree -> matrice plus longue") {

      val l = (RED :: GREEN :: Nil) :: Nil
      val m = service_M.list_to_matrix(l)

      val obtained = matrix_to_quadtree(m)
      val expected = N(C(RED), C(GREEN), C(colorFiller), C(colorFiller))

    assertEquals(obtained, expected)
    
  }

    test("MatrixConversions : matrix_to_quadtree -> matrice plus haute") {

      val l = (RED :: Nil) :: (BLUE :: Nil) :: Nil
      val m = service_M.list_to_matrix(l)

      val obtained = matrix_to_quadtree(m)
      val expected = N(C(RED), C(colorFiller), C(colorFiller), C(BLUE))

    assertEquals(obtained, expected)
    
  }

}
