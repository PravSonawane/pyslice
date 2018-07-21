package pyslice

import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

inline operator fun <reified T> Array<out T>.get(slicer: String): Array<out T> {
  require(slicer.isNotEmpty()) { "slicer must be a valid python slice notation. Found:$slicer" }

  if (this.isEmpty()) return this

  when (slicer) {
    ":", "::", "0:", "0::" -> return this
    "::1", "0::1" -> return this
    ":${this.size}", ":${this.size}:", ":${this.size}:1" -> return this
    "0:${this.size}", "0:${this.size}:", "0:${this.size}:1" -> return this
  }
  var (start, stop, step) = computeAbsoluteSlicers(slicer, this.size)
  when {
    step > 0 && start >= size -> return emptyArray()
    step > 0 && stop < -(size+1) -> return emptyArray()
    step < 0 && start < -size -> return emptyArray()
    step < 0 && stop > size -> return emptyArray()
    step > 0 && stop > -1 && start > stop -> return emptyArray()
  }

  val slicers = computeSlicers(slicer, this.size)
  return if (slicers[2] > 0) {
    if(slicers[0] >= slicers[1]) return emptyArray()
    val newSize = ceil((slicers[1] - slicers[0]).toDouble() / slicers[2]).toInt()
    val newArray = Array(newSize) { this[slicers[0]] }
    var index = 0
    for (i in slicers[0] until slicers[1] step slicers[2]) newArray[index++] = this[i]
    newArray
  } else {
    if(slicers[1] >= slicers[0]) return emptyArray()
    val newSize = ceil((slicers[0] - slicers[1]).toDouble() / abs(slicers[2])).toInt()
    val newArray = Array(newSize) { this[slicers[0]] }
    var index = 0
    for (i in slicers[0] downTo slicers[1] + 1 step abs(slicers[2])) newArray[index++] = this[i]
    newArray
  }
}

inline fun computeSlicers(slicer: String, size: Int): IntArray {
  val splits = slicer.split(":")
  return when {
    splits.size == 1 -> intArrayOf(getStart(splits[0], 1, size), size, 1)
    splits.size == 2 -> intArrayOf(getStart(splits[0], 1, size), getEnd(splits[1], 1, size), 1)
    splits.size == 3 -> {
      val step = getStep(splits[2])
      intArrayOf(getStart(splits[0], step, size), getEnd(splits[1], step, size), step)
    }
    else -> throw AssertionError("start, end and step cannot be determined from:$slicer")
  }
}

inline fun computeAbsoluteSlicers(slicer: String, size: Int): IntArray {
  val splits = slicer.split(":")
  return when {
    splits.size == 1 -> intArrayOf(getAbsoluteStart(splits[0], 1, size), size, 1)
    splits.size == 2 -> intArrayOf(getAbsoluteStart(splits[0], 1, size), getAbsoluteEnd(splits[1], 1, size), 1)
    splits.size == 3 -> {
      val step = getStep(splits[2])
      intArrayOf(getAbsoluteStart(splits[0], step, size), getAbsoluteEnd(splits[1], step, size), step)
    }
    else -> throw AssertionError("start, end and step cannot be determined from:$slicer")
  }
}

inline fun getStart(startStr: String, step: Int, size: Int): Int {
  try {
    when {
      step > 0 && startStr.isEmpty() -> return 0
      step < 0 && startStr.isEmpty() -> return size - 1
    }

    val start = startStr.toInt()

    when {
      start >= 0 -> return min(start, size - 1)
      start < 0 -> return max(start + size, 0)
    }
  } catch (e: NumberFormatException) {
    throw IllegalArgumentException("start must be an Int. Found:$startStr")
  }
  throw AssertionError("start cannot be determined. Found:$startStr")
}

inline fun getEnd(endStr: String, step: Int, size: Int): Int {
  try {
    when {
      step > 0 && endStr.isEmpty() -> return size
      step < 0 && endStr.isEmpty() -> return -1
    }

    val end = endStr.toInt()

    when {
      end == 0 -> return 0
      end > 0 -> return min(end, size)
      end < 0 -> return max(end + size, -1)
    }
  } catch (e: NumberFormatException) {
    throw IllegalArgumentException("end must be an Int. Found:$endStr")
  }
  throw AssertionError("end cannot be determined. Found:$endStr")
}

inline fun getAbsoluteStart(startStr: String, step: Int, size: Int): Int {
  try {
    return when {
      step > 0 && startStr.isEmpty() -> 0
      step < 0 && startStr.isEmpty() -> size - 1
      else -> startStr.toInt()
    }
  } catch (e: NumberFormatException) {
    throw IllegalArgumentException("start must be an Int. Found:$startStr")
  }
}

inline fun getAbsoluteEnd(endStr: String, step: Int, size: Int): Int {
  try {
    return when {
      step > 0 && endStr.isEmpty() -> size
      step < 0 && endStr.isEmpty() -> -1
      else -> endStr.toInt()
    }
  } catch (e: NumberFormatException) {
    throw IllegalArgumentException("end must be an Int. Found:$endStr")
  }
}

fun getStep(stepStr: String): Int {
  try {
    if (stepStr.isEmpty()) return 1
    val step = stepStr.toInt()
    if (step != 0) return step
    else throw IllegalArgumentException("step must be a non-zero Int. Found:$stepStr")
  } catch (e: NumberFormatException) {
    throw IllegalArgumentException("step must be a non-zero Int. Found:$stepStr")
  }
}
