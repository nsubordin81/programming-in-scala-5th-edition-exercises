import scala.collection.mutable

class ChecksumAccumulator:
  private var sum = 0
  // add is a type of method known as a procedure because
  // it is only executed to achieve a side effect, no return
  def add(b: Byte): Unit = sum += b
  def checksum(): Int = ~(sum & 0xff) + 1

object ChecksumAccumulator:
  private val cache = mutable.Map.empty[String, Int]

  def calculate(s: String): Int =
    if cache.contains(s) then cache(s)
    else
      val acc = new ChecksumAccumulator
      for c <- s do
        // shifting char 8 bits to the right
        acc.add((c >> 8).toByte)
        // also adding original char value in bytes
        acc.add(c.toByte)
      val cs = acc.checksum()
      // add the tuple for this string and checksum to the cache.
      cache += (s -> cs)
      cs

val acc = new ChecksumAccumulator
val csa = new ChecksumAccumulator

// can't access the internal sum, its private
// acc.sum = 3
