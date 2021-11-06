import scala.compiletime.ops.string
val args = List.empty

val name =
  if !args.isEmpty then args(0)
  else "default.txt"

name

def gcdLoop(x: Long, y: Long): Long =
  var a = x
  var b = y
  while a != 0 do
    val temp = a
    a = b % a
    b = temp
  b

val test = gcdLoop(100, 200)
val counter = gcdLoop(5, 5)

def gcdLoopDoWhile(x: Long, y: Long): Long =
  var a = x
  var b = y
  while
    // always executes at least once?
    val temp = a
    a = b % a
    b = temp
    a != 0
  do ()
  b

def countDownToOne(nums: Int): Int =
  var i = 0
  while
    i = i + 1
    nums - i > 3
  do ()
  nums - i

val x = countDownToOne(10)
// see you can do a while loop where it is like a do while and executes at least once
// inline reassignments are discouraged though by having them always return unit (because it is a side effect.)
// however you can still reassign vars which is a side effect, I did it above, this is not the preferred way to write
// it is an imperative style and it mutates i. However if you do this only locally then it isn't the worst kind of violation of immutability.
val y = countDownToOne(3)

val test1 = gcdLoopDoWhile(1, 1)
val counter1 = gcdLoopDoWhile(5, 5)

// this is the right way to do it, use recursion, no variable mutation, and it is more concise and easy to read. The book says challenge
// while loops and vars they are artifacts of imperative style making it into your code.
def gcd(x: Long, y: Long): Long =
  if x == 0 then y else gcd(y % x, x)

gcd(100, 200)

// here we go, the start of the show it is the for expression

val filesHere = new java.io.File(".").listFiles

// this syntax is called a generator
// write down while you are thinking of this the connection between generators and the functional paradigm
// generators will create a new val per iteration and execute the contents of the expression on it
// this seems connected to python generators and also Unity generator expressions for working with
// continuations, and I suppose javascript as well. Worth understanding how these are all the same and maybe
// how they differ across languages.
for file <- filesHere do println(file)

for i <- 1 to 10 do println(s"Iteration $i")

//if you don't want to include the last number in the range
for i <- 1 until 4 do println(s"Iteration $i")

// scala doesn't use Ranges to iterate typically, so this is uncommon to see:
for i <- 0 until filesHere.length - 1 do println(filesHere(i))

for file <- filesHere do if file.getName.endsWith(".bloop") then println(file)

// for expressions can contain filters, in fact they can contain multiple filters, so no need to write the if inside the for expression.
for
  file <- filesHere
  if file.isFile
  if file.getName.endsWith(".bloop")
do println(file)

def fileLines(file: java.io.File) =
  scala.io.Source.fromFile(file).getLines().toArray

val filesInThisDir = java.io.File("./src/main/scala").listFiles

def grep(pattern: String) =
  for
    file <- filesInThisDir
    if file.getName.endsWith(".worksheet.sc")
    line <- fileLines(file)
    if line.trim.matches(pattern)
  do println(s"$file: ${line.trim}")

grep(".*gcd.*")

// just like the last one except notice how we will bind the result of line.trim to a variable so we don't have to compute it again
// this is useful when you want to define an operation that is expensive inline
def otherGrep(pattern: String) =
  for
    file <- filesInThisDir
    if file.getName.endsWith(".worksheet.sc")
    line <- fileLines(file)
    trimmedLine = line.trim
    if trimmedLine.matches(pattern)
  do println(s"$file: $trimmedLine")

otherGrep(".*file.*")

// look! it is a yield statement. Tis is good because the output of each iteration of the generator expression is not Unit, not side effecting, results
// in a collection whose type is given by the type of the output of the generator expression.
def grepToArray(pattern: String) =
  for
    file <- filesInThisDir
    if file.getName.endsWith(".worksheet.sc")
  yield file

grepToArray(".*gcd.*")

def getLines() =
  for
    file <- filesHere
    if file.getName().endsWith(".worksheet.sc")
    line <- fileLines(file)
    trimmed = line.trim
    if trimmed.matches(".*for.*")
  yield trimmed.length

getLines()

// throw new IllegalArgumentException

def half(n: Int) =
  // if the result is the exception the type of the exception will be Nothing
  // this works, so the type of this whole expression is a floating point
  // even though it migh tresult in an expression that returns type Nothing.
  if n % 2 == 0 then n / 2 else throw new RuntimeException("n must be even")

import java.io.FileReader
import java.io.FileNotFoundException
import java.io.IOException

// catching checked exceptions or putting in a throws clause is not required in scala.
//try catch syntax example.
// try

//   val f = new FileReader("input.txt")
// // Use and close file
// catch
//   case ex: FileNotFoundException => // Handle missing file
//   case ex: IOException           =>
// // Hanle other I/O error

val file = new FileReader("src/main/scala/ch7.worksheet.sc")
try println(file.read()) // Use the file
finally file.close() // be sure to close the file

// try catch finally clause in scala is the same as java's but it doesn't result in a value in java.

// notice though that if you have an explicit return clause then the finally will overrule the return value from the try.

// overall advice though is don't even try to return anything from a finally clause, you generally don't want the finally value to have that
// purpose it should be cleaning up connections to none memory managed objects.
def contrived(): Int = try return 1
finally return 2

contrived()

def contrivedButBetter(): Int = try 1
finally 2

contrivedButBetter()

// match expressions (memory match game :p)

def matcher(item: String): Unit =
  item match
    case "salt"  => println("pepper")
    case "chips" => println("salsa")
    case "eggs"  => println("bacon")
    case _       => println("huh?")

matcher("salt")
matcher("conglesiated")
matcher("exactly")

def matcherWithReturn(item: String): String =
  item match
    case "salt" => "pepper"
    case _      => "huh??"

val huh = matcherWithReturn("consigned")

// there is no break or continue control structure in scala!!
// you can replace continues with a different if statement
// you can replace breaks with a boolean indicator
// you can use recursion to get rid of the while loop and the state mutation it requires.

// shadow is possible in scala unlike in java where the compiler will not let you have two variables with the same name in nested scopes

def makeRowSeq(row: Int) =
  for col <- 1 to 10 yield
    val prod = (row * col).toString
    val padding = " " * (4 - prod.length)
    padding + prod

def makeRow(row: Int) = makeRowSeq(row).mkString

def multiTable() =
  val tableSeq =
    for row <- 1 to 10
    yield makeRow(row)

  tableSeq.mkString("\n")

multiTable()

// exercise I gave myself, do this again, trying not to look at solution, add feature of coefficient

def makeRowDouble(row: Int, coefficient: Int) =
  for col <- 1 to 10 yield
    val prod = (col * row * coefficient).toString
    val padding = " " * (4 - prod.length)
    padding + prod

def makeRow(row: Int, coefficient: Int) =
  makeRowDouble(row, coefficient).mkString

def multiTableDouble() =
  val tableSeq =
    for row <- 1 to 10
    yield makeRow(row, 2)

  tableSeq.mkString("\n")

multiTableDouble()

// recall test
// list the control structures available in scala 3:
/** for expressions - immutable, don't modify an index to iterate, use
  * generators. can have conditional filters, can be nested, can yield new
  * values into a collection while loops - should be viewed skeptically, require
  * the use of a var, don't return anything, side effecting if expressions -
  * return a value, different from java in this way, makes things more
  * expressive less error prone match expressions - can be used for pattern
  * matching we haven't explored this that much yet.
  *
  * results, did I miss anything? sorry formatter really messed this up
  * 1.break and continue don't exist, but you shouldn't have been using them in
  * the first place. instead for continue, you guard your code with a
  * conditional statement which will skip the loop if it is false. if you want
  * to break out of the whole loop, then you could use an indicator as part of
  * your looping criteria 2. scala's match control structure doesn't fall
  * through and breaks are implicit 3. totally missed try catch. It is pretty
  * similar, maybe a few syntactic differences 4. for expressions let you bind
  * variables inside the stream, so you can reduce computation. 5. while loops
  * can be turned into do while loops by putting the do() at the end. 6.
  * something you might expect to work in scala that works in java but it
  * doesn't is using reassignment inline. in java this returns the newly
  * assigned value, in scala it returns Unit.
  */
