package PRO2.projet

import fr.istic.scribble.*
import Quadtrees.*

class MySuite extends munit.FunSuite {

	// Elements par défaut
	
	val dlength: Float = 512.0
	val dcolor: Color = WHITE

	// Tests sur quadtree_to_image.

	test("quadtree_to_image : image uniforme compressée, sans contour") {

		val qt: QT = C(dcolor)
		val obtained = quadtree_to_image(qt, false, dlength)

		val dregion: Image = FillColor(Rectangle(dlength, dlength), dcolor)
		val expected = LineColor(dregion, dcolor)

		assertEquals(obtained, expected)

	}

	test("quadtree_to_image : image uniforme compressée, avec contour") {

		val qt: QT = C(dcolor)
		val obtained = quadtree_to_image(qt, true, dlength)

		val dregion: Image = FillColor(Rectangle(dlength, dlength), dcolor)
		val expected = LineColor(dregion, RED)

		assertEquals(obtained, expected)

	}
	
	test("quadtree_to_image : image uniforme non-compressée") {

		val qt: QT = N(C(dcolor), C(dcolor), C(dcolor), C(dcolor))
		val obtained = quadtree_to_image(qt, false, dlength)
		
		val subrect: Image = FillColor(Rectangle(dlength / 2, dlength / 2), dcolor)
		val dsubdiv: Image = LineColor(subrect, dcolor)
		val expected = Below(Beside(dsubdiv, dsubdiv), Beside(dsubdiv, dsubdiv))

		assertEquals(obtained, expected)
		
	}
	
}
