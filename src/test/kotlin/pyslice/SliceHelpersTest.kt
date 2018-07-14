package pyslice

import org.junit.Test

class SliceHelpersTest {

  @Test
  fun `GIVEN an empty string WHEN isValid THEN it should return true`() {
    assert(isValid(""))
  }
}