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

var nums = 3::2::3::Nil

while
    nums 
