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
