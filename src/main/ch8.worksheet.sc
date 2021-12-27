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
