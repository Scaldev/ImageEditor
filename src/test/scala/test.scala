package PRO2.tp4

class MySuite extends munit.FunSuite {

	test("testing failure") {
		val obtained = 42
		val expected = 42
		assertEquals(obtained, expected)
	}
	
}
