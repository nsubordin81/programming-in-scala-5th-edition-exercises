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
