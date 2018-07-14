package pyslice

operator fun <T> Array<T>.get(slicer: String): Array<T> {
  require(isValid(slicer)) { "Invalid slice string. Found:$slicer" }
  return when(slicer) {
    "" -> this
    else -> throw AssertionError("Operation not handled")
  }
}