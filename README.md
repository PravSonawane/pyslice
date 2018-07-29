# pyslice

Python style [array slicing][3] in Kotlin

[![Build Status](https://travis-ci.org/PravSonawane/pyslice.svg?branch=master)](https://travis-ci.org/PravSonawane/pyslice)
[![Maven central](https://img.shields.io/maven-central/v/io.github.pravsonawane/pyslice.svg)](https://repo1.maven.org/maven2/io/github/pravsonawane/pyslice/)

## Examples

```kotlin
import pyslice.get

fun main(args: Array<String>) {
  val a = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
  
  // stepping
  val even = a["::2"]           // even         = [0, 2, 4, 6, 8]
  val odd = a["1::2"]           // odd          = [1, 3, 5, 7, 9]
  
  // reversing
  val reversed = a["::-1"]      // reversed     = [9, 8, 7, 6, 5, 4, 3, 2, 1, 0]
  val reversedHalf = a[":4:-1"] // reversedHalf = [9, 8, 7, 6, 5]
  
  // string interpolation
  val start = 8
  val stop = 2
  val variables = a["$start:$stop:-3"] // variables = [8, 5]
  
  // negative start, stop, step
  val reversedOdd = a["-1:-11:-2"] // reversedOdd = [9, 7, 5, 3, 1] 
  
  // no ArrayIndexOutOfBoundsException
  val outOfBounds = a["500:600:2"] // outOfBounds = []
}
```

## Download

Download [the latest JAR][1] or grab via Maven:
```xml
<dependency>
  <groupId>io.github.pravsonawane</groupId>
  <artifactId>pyslice</artifactId>
  <version>0.x</version>
</dependency>
```
or Gradle:
```groovy
implementation 'io.github.pravsonawane:pyslice:0.x'
```

## Running the tests

The following will run all tests

```
./gradlew test
```

## Considerations

This library is a way to bring the very concise python style array slicing notations to Kotlin. If you understand how array slicing works in Python, using this library should be fairly straight forward. However, the following are worth considering:

* This library aims to exactly match the behaviour of python array slice notations (no `ArrayIndexOutOfBoundsException`).
* It uses a [magic string][2] to specify the slicing which might not be idiomatic Kotlin.


## License

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


[1]: https://search.maven.org/remote_content?g=io.github.pravsonawane&a=pyslice&v=LATEST
[2]: https://en.wikipedia.org/wiki/Magic_string
[3]: https://docs.python.org/2.3/whatsnew/section-slices.html