/** Chapter 8 Functions and closures * */

// Methods

object Padding:
  def padLines(text: String, minWidth: Int): String =
    val paddedLines =
      for line <- text.linesIterator yield padLine(line, minWidth)
    paddedLines.mkString("\n")

  private def padLine(line: String, minWidth: Int): String =
    if line.length >= minWidth then line
    else line + " " * (minWidth - line.length)

Padding.padLines(
  "a long multi line string \n try and best the length of this string \n you won't be able to becuase it is too long.",
  25
)

// Nested Functions

def padLines(text: String, minWidth: Int): String =
  def padLine(line: String, minWidth: Int): String =
    if line.length >= minWidth then line
    else line + " " * (minWidth - line.length)

  val paddedLines =
    for line <- text.linesIterator yield padLine(line, minWidth)
  paddedLines.mkString("\n")

def padLinesMinWidthContext(text: String, minWidth: Int): String =

  def padLine(line: String): String =
    if line.length >= minWidth then line
    else line + " " * (minWidth - line.length)

  val paddedLines =
    for line <- text.linesIterator yield padLine(line)
  paddedLines.mkString("\n")

print(
  padLinesMinWidthContext(
    "this is a line \n and another line \n and even a third line",
    10
  )
)

// key lesson here was that nested functions are aware of the context of their parent functions and so
// you can share variables instead of passing scope around.

// function literal is the name given to the lambda style functions in scala 3. they mention that these are compiled into a method handle
// and that this method handle is then instantiated at runtime into an object which is called a function value.
// example of function literal at compile time: (x: Int) => x + 1.
// using this as a function value is when you, say, assign it to a variable like so: val increase = (x: Int) => x + 1. now you can call increase diretly with args

def testingMultilineLiteral = (x: Int) =>
  val y = 2
  x + y

testingMultilineLiteral(2)

val someNumbers = List(3, 2, 1, 0)

val literalPrint = someNumbers.foreach((x: Int) => println(x))

val testFilter = someNumbers.filter((x: Int) => x > 0)
// scala compiler knows someNumbers is a list so it infers the type of x
// targeted typing
val testFilterTypeInference = someNumbers.filter((x) => x > 0)
val testNoParens = someNumbers.filter(x => x > 0)
