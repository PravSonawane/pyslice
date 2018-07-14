package pyslice

/**
 *
 */
internal fun isValid(slicer: String): Boolean {
  return when(slicer) {
    "" -> true
    else -> false
  }
}