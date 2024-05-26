package PRO2.projet.v1

import fr.istic.scribble.*

class MySuite extends munit.FunSuite {

  // Valeurs globales.

  val service_QT: Quadtrees = ImpQuadtrees
  val dsize: Int = 9
  val dlength: Float = 512.0

  // **************************************************************************** \\
  // *                          	  quadtree_to_image                     	    * \\
  // **************************************************************************** \\

  test("quadtree_to_image : zone unicolore sans contour") {

    val obtained = service_QT.quadtree_to_image(C(WHITE), false, dsize)

    val expected =
      LineColor(FillColor(Rectangle(dlength, dlength), WHITE), WHITE)

    assertEquals(obtained, expected)

  }

  test("quadtree_to_image : zone unicolore avec contour") {

    val obtained = service_QT.quadtree_to_image(C(WHITE), true, dsize)

    val expected = LineColor(FillColor(Rectangle(dlength, dlength), WHITE), RED)

    assertEquals(obtained, expected)

  }

  test("quadtree_to_image : image non-compressee") {

    val qt: QT = N(C(WHITE), C(WHITE), C(WHITE), C(WHITE))
    val obtained = service_QT.quadtree_to_image(qt, false, dsize)

    val subrect: Image = FillColor(Rectangle(dlength / 2, dlength / 2), WHITE)
    val dsubdiv: Image = LineColor(subrect, WHITE)
    val expected = Below(Beside(dsubdiv, dsubdiv), Beside(dsubdiv, dsubdiv))

    assertEquals(obtained, expected)

  }

  test("quadtree_to_image : image non-unicolore") {

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

  test("compress : noeud dont les enfants sont 4 feuilles de même couleur") {

    val quadtree: QT = N(C(WHITE), C(WHITE), C(WHITE), C(WHITE))

    val obtained = service_QT.compress(quadtree)

    val expected = C(WHITE)

    assertEquals(obtained, expected)

  }

  test("compress : quadtree déjà compresé") {

    val obtained = service_QT.compress(C(WHITE))

    val expected = C(WHITE)

    assertEquals(obtained, expected)

  }

  // **************************************************************************** \\
  // *                          	  transformations                     	      * \\
  // **************************************************************************** \\

  test("transformation : rotation_left") {

    val quadtree: QT = N(C(RED), C(BLUE), C(BLACK), C(WHITE))
    val obtained = service_QT.rotation_left(quadtree)

    val expected = N(C(BLUE), C(BLACK), C(WHITE), C(RED))

    assertEquals(obtained, expected)

  }

  test("transformation : rotation_right") {

    val quadtree: QT = N(C(RED), C(BLUE), C(BLACK), C(WHITE))
    val obtained = service_QT.rotation_right(quadtree)

    val expected = N(C(WHITE), C(RED), C(BLUE), C(BLACK))

    assertEquals(obtained, expected)

  }

  test("transformation : flip_vertical") {

    val quadtree: QT = N(C(RED), C(BLUE), C(BLACK), C(WHITE))
    val obtained = service_QT.flip_vertical(quadtree)

    val expected = N(C(BLUE), C(RED), C(WHITE), C(BLACK))

    assertEquals(obtained, expected)

  }

  test("transformation : flip_horizontal") {

    val quadtree: QT = N(C(RED), C(BLUE), C(BLACK), C(WHITE))
    val obtained = service_QT.flip_horizontal(quadtree)

    val expected = N(C(WHITE), C(BLACK), C(BLUE), C(RED))

    assertEquals(obtained, expected)

  }

  test("transformation : transform (rotation_right -> )") {

    val quadtree: QT = N(C(RED), C(BLUE), C(BLACK), C(WHITE))
    val transfos: List[Transformation] =
      service_QT.rotation_left :: service_QT.flip_vertical :: Nil

    val obtained = service_QT.transform(quadtree, transfos)

    val expected = N(C(BLACK), C(BLUE), C(RED), C(WHITE))

    assertEquals(obtained, expected)

  }

}
