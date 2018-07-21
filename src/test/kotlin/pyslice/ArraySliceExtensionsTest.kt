package pyslice

import org.junit.Test

class ArraySliceExtensionsTest {

  @Test
  fun `GIVEN invalid python slice notation WHEN invoking index access operator THEN it should throw IAE`() {
    val a = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    try {
      a["abc"]
    } catch (e: IllegalArgumentException) {
      assert(e.message == "start must be an Int. Found:abc")
    }
  }

  @Test
  fun `GIVEN invalid start WHEN invoking index access operator THEN it should throw IAE`() {
    val a = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    try {
      a["abc:"]
    } catch (e: IllegalArgumentException) {
      assert(e.message == "start must be an Int. Found:abc")
    }
  }

  @Test
  fun `GIVEN invalid end WHEN invoking index access operator THEN it should throw IAE`() {
    val a = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    try {
      a[":a"]
    } catch (e: IllegalArgumentException) {
      assert(e.message == "end must be an Int. Found:a")
    }
  }

  @Test
  fun `GIVEN invalid step WHEN invoking index access operator THEN it should throw IAE`() {
    val a = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    try {
      a["::a"]
    } catch (e: IllegalArgumentException) {
      assert(e.message == "step must be a non-zero Int. Found:a")
    }
  }

  @Test
  fun `GIVEN start, stop, step not specified WHEN invoking index access operator THEN it should return the original array`() {
    val a = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    assert(a === a[":"]) { "failed for a single colon string " }
    assert(a === a["::"]) { "failed for a double colon string " }
  }

  @Test
  fun `GIVEN 0 start WHEN invoking index access operator THEN it should return the original array`() {
    val a = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    assert(a === a["0:"]) { "failed for a single colon string " }
    assert(a === a["0::"]) { "failed for a double colon string " }
  }

  @Test
  fun `GIVEN a double colon and step as 1 WHEN invoking index access operator THEN it should return the original array`() {
    val a = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    assert(a === a["::1"])
  }

  @Test
  fun `GIVEN 0 as start, a double colon and step as 1 WHEN invoking index access operator THEN it should return the original array`() {
    val a = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    assert(a === a["0::1"])
  }

  @Test
  fun `GIVEN a single colon and stop as size(exclusive) WHEN invoking index access operator THEN it should return the original array`() {
    val a = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    assert(a === a[":${a.size}"])
  }

  @Test
  fun `GIVEN an empty array WHEN invoking index access operator THEN it should return the original array`() {
    val a = arrayOf<Int>()
    assert(a === a[":"])
    assert(a === a["::"])
    assert(a === a["0::"])
    assert(a === a["::1"])
    assert(a === a[":${a.size}"])
    assert(a === a[":${a.size}:"])
    assert(a === a[":${a.size}:1"])
    assert(a === a["0:${a.size}"])
    assert(a === a["0:${a.size}:"])
    assert(a === a["0:${a.size}:1"])
  }

  @Test
  fun `GIVEN only a positive start WHEN invoking index access operator THEN it should return a sub array from start to array's size`() {
    val a = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    assert(a["2:"].contentEquals(arrayOf(2, 3, 4, 5, 6, 7, 8, 9)))
    assert(a["2::"].contentEquals(arrayOf(2, 3, 4, 5, 6, 7, 8, 9)))
    assert(a["${a.size - 1}:"].contentEquals(arrayOf(9)))
    assert(a["${a.size - 1}::"].contentEquals(arrayOf(9)))
  }

  @Test
  fun `GIVEN only a positive start greater than size WHEN invoking index access operator THEN it should return an empty array`() {
    val a = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    assert(a["1000:"].contentEquals(emptyArray()))
    assert(a["1000::"].contentEquals(emptyArray()))
  }

  @Test
  fun `GIVEN only a negative start WHEN invoking index access operator THEN it should return a sub array from start+size to array's size`() {
    val a = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    assert(a["-1:"].contentEquals(arrayOf(9)))
    assert(a["-1::"].contentEquals(arrayOf(9)))
    assert(a["-3:"].contentEquals(arrayOf(7, 8, 9)))
    assert(a["-3::"].contentEquals(arrayOf(7, 8, 9)))
  }

  @Test
  fun `GIVEN only a negative start less than negative size WHEN invoking index access operator THEN it should return the original array`() {
    val a = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    assert(a["-1000:"].contentEquals(a))
    assert(a["-1000::"].contentEquals(a))
  }

  @Test
  fun `GIVEN only a positive end WHEN invoking index access operator THEN it should return a sub array from 0 to end (exclusive)`() {
    val a = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    assert(a[":4"].contentEquals(arrayOf(0, 1, 2, 3)))
    assert(a[":4:"].contentEquals(arrayOf(0, 1, 2, 3)))
    assert(a[":${a.size}"].contentEquals(a))
    assert(a[":${a.size}:"].contentEquals(a))
  }

  @Test
  fun `GIVEN only a positive end greater than size WHEN invoking index access operator THEN it should return a sub array from 0 to size (exclusive)`() {
    val a = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    assert(a[":1000"].contentEquals(a))
    assert(a[":1000:"].contentEquals(a))
  }

  @Test
  fun `GIVEN only a negative end WHEN invoking index access operator THEN it should return a sub array from 0 to end+size(exclusive)`() {
    val a = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    assert(a[":-1:"].contentEquals(arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8)))
    assert(a[":-1:"].contentEquals(arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8)))
    assert(a[":${a.size * -1}"].contentEquals(emptyArray()))
    assert(a[":${a.size * -1}:"].contentEquals(emptyArray()))
  }

  @Test
  fun `GIVEN only a negative end less than negative size WHEN invoking index access operator THEN it should return an empty array`() {
    val a = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    assert(a[":-1000"].contentEquals(emptyArray()))
    assert(a[":-1000:"].contentEquals(emptyArray()))
  }

  @Test
  fun `GIVEN positive start and positive end WHEN invoking index access operator THEN it should return an array from start to end (exclusive)`() {
    val a = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    assert(a["1:4"].contentEquals(arrayOf(1, 2, 3)))
    assert(a["1:4:"].contentEquals(arrayOf(1, 2, 3)))
    assert(a["1:${a.size}"].contentEquals(arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9)))
    assert(a["1:${a.size}:"].contentEquals(arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9)))
  }

  @Test
  fun `GIVEN positive start greater than positive end WHEN invoking index access operator THEN it should return an empty array`() {
    val a = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    assert(a["4:1"].contentEquals(emptyArray()))
    assert(a["4:1:"].contentEquals(emptyArray()))
    assert(a["${a.size}:1"].contentEquals(emptyArray()))
    assert(a["${a.size}:1:"].contentEquals(emptyArray()))
  }

  @Test
  fun `GIVEN negative start and negative end WHEN invoking index access operator THEN it should return an array from start+size to end+size (exclusive)`() {
    val a = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    assert(a["-8:-4"].contentEquals(arrayOf(2, 3, 4, 5)))
    assert(a["-8:-4:"].contentEquals(arrayOf(2, 3, 4, 5)))
    assert(a["-8:-1"].contentEquals(arrayOf(2, 3, 4, 5, 6, 7, 8)))
    assert(a["-8:-1:"].contentEquals(arrayOf(2, 3, 4, 5, 6, 7, 8)))
    assert(a["-8:-9"].contentEquals(emptyArray()))
    assert(a["-8:-9:"].contentEquals(emptyArray()))
  }

  @Test
  fun `GIVEN negative start greater than negative end WHEN invoking index access operator THEN it should return an empty array`() {
    val a = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    assert(a["-1:-4"].contentEquals(emptyArray()))
    assert(a["-1:-4:"].contentEquals(emptyArray()))
    assert(a["-1:${a.size - 1}"].contentEquals(emptyArray()))
    assert(a["-1:${a.size - 1}:"].contentEquals(emptyArray()))
  }

  @Test
  fun `GIVEN positive start and negative end WHEN invoking index access operator THEN it should return an array from start to end+size (exclusive)`() {
    val a = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    assert(a["1:-2"].contentEquals(arrayOf(1, 2, 3, 4, 5, 6, 7)))
    assert(a["1:-2:"].contentEquals(arrayOf(1, 2, 3, 4, 5, 6, 7)))
    assert(a["5:-5"].contentEquals(emptyArray()))
    assert(a["5:-5:"].contentEquals(emptyArray()))
    assert(a["1:${a.size * -1}"].contentEquals(emptyArray()))
    assert(a["1:${a.size * -1}:"].contentEquals(emptyArray()))
    assert(a["1:-1000"].contentEquals(emptyArray()))
    assert(a["1:-1000:"].contentEquals(emptyArray()))
    assert(a["1000:-1000"].contentEquals(emptyArray()))
    assert(a["1000:-1000:"].contentEquals(emptyArray()))
  }

  @Test
  fun `GIVEN negative start and positive end WHEN invoking index access operator THEN it should return an array from start+end to end (exclusive)`() {
    val a = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    assert(a["-6:7"].contentEquals(arrayOf(4, 5, 6)))
    assert(a["-6:7:"].contentEquals(arrayOf(4, 5, 6)))
    assert(a["-5:5"].contentEquals(emptyArray()))
    assert(a["-5:5:"].contentEquals(emptyArray()))
    assert(a["${a.size * -1}:3"].contentEquals(arrayOf(0, 1, 2)))
    assert(a["${a.size * -1}:3:"].contentEquals(arrayOf(0, 1, 2)))
    assert(a["-1000:1000"].contentEquals(a))
    assert(a["-1000:1000:"].contentEquals(a))
  }

  @Test
  fun `GIVEN a positive step WHEN invoking index access operator THEN it only return elements at step intervals`() {
    val a = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    assert(a["::2"].contentEquals(arrayOf(0, 2, 4, 6, 8)))
    assert(a["::7"].contentEquals(arrayOf(0, 7)))
    assert(a["3:8:3"].contentEquals(arrayOf(3, 6)))
    assert(a["-4:8:2"].contentEquals(arrayOf(6)))
    assert(a["-4:-1:2"].contentEquals(arrayOf(6, 8)))
    assert(a["-1:-11:3"].contentEquals(emptyArray()))
  }

  @Test
  fun `GIVEN a negative step WHEN invoking index access operator THEN it only return elements at step intervals`() {
    val a = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    assert(a["::-2"].contentEquals(arrayOf(9, 7, 5, 3, 1)))
    assert(a["::-7"].contentEquals(arrayOf(9, 2)))
    assert(a["3:8:-3"].contentEquals(emptyArray()))
    assert(a["-1:-8:-2"].contentEquals(arrayOf(9, 7, 5, 3)))
    assert(a["-1:-4:-2"].contentEquals(arrayOf(9, 7)))
    assert(a["-1:-11:-3"].contentEquals(arrayOf(9, 6,3,0)))
  }


}