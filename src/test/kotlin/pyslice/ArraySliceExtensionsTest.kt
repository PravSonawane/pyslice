package pyslice

import org.junit.Test

class ArraySliceExtensionsTest {

  @Test
  fun `GIVEN empty string WHEN invoke index access operator THEN it should return same array`() {
    val a = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    assert(a.contentEquals(a[""]))
  }
}