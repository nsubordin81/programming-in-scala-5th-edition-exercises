import org.junit.Test
import org.junit.Assert.*

class Test1:
  @Test def t1(msg: String): Unit = 
    assertEquals("I was compiled by Scala 3. :)", msg)
