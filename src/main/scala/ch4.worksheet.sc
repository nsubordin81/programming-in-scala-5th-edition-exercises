import scala.collection.mutable

// CLASSES

class ChecksumAccumulator:
  private var sum = 0
  // add is a type of method known as a procedure because
  // it is only executed to achieve a side effect, no return
  def add(b: Byte): Unit = sum += b
  //notice the return types are explicit in add and checksum. They 
  // can be inferred but then a reader would have to do the inference.
  // best practice is to call out types of params and return values for 
  // public methods. 
  def checksum(): Int = ~(sum & 0xff) + 1


// COMPANION OBJECTS

// first, some background on checksums, citing this article: 
// https://www.howtogeek.com/363735/what-is-a-checksum-and-why-should-you-care/. 
// Checksums are sequences of letters and numbers, 
// which result from running your file contents through a cryptographic hash function. 
// The has is a result of some fixed length, and allow you to verify the file's integrity
// if you have the checksum you made from the file earlier. This is because hashes change 
// even when small changes are made to the files they are made from. This can be helpful
// when you are downloading a file or storing a file and something corrupts it. If you 
// have a hash of the original then you can verify it is still the same content.
// popular algorithms for checksum are MD-5, SHA-1 and SHA-256. MD-5 and SHA-1 have
// known collisions, so when using a checksum for authenticity you may be vulnerable to attack
// since an attacker could find a known collision to your file and manipulate their modification
// to make the checksum the same.but for corruption it is unlikely you would experience collision. 
// SHA-256 doesn't have any known collisions yet. 

// scala is more OO than Java in many ways
// one is that you can't have static members in scala classes. 
// instead you have singleton objects. 
// if it has the same name as a class it is associated with, 
// then it is that classe's companion object. This ChecksumAccumulator singleton
// object is the companion object of the ChecksumAccumulator class. 
// rules: 
// singleton companion object has to have the same name as the class
// singleton companion object has to be defined in the same source file as the class
// what do you get for this? the companion class and companion object can both see each other's 
// private members. This is cool, in java static private members could not see out to the 
// unlike types defined by classes, you can't pass parameters to a constructor of a singleton object. 
// singleton objects are instances of 'synthetic classes' referenced from a static variable, so they 
// have the same initialization semantics as statics do in java. It is initialized the first time it is accessed 
// by some code. (lazy?)
object ChecksumAccumulator:
  private val cache = mutable.Map.empty[String, Int]

  // companion objects hold what in java would be static member functions
  // you could invoke this with ChecksumAccumulator.calculate("this is the string I want to checksum")
  // But the companion object isnt' just a place that static methods live, it is also a first class object
  // a type is not defined when you define a singleton object, you don't have a way to instantiate the object
  // like you would with a java class. you have an instance that just exists. The companion class defines the type
  // But, the reified singleton companion object does extend a superclass and it can mix in traits, so you can 
  // refer to the singleton object using variables of these types or invoke methods using them. Ch. 12 has more.
  def calculate(s: String): Int =
    if cache.contains(s) then cache(s)
    else
      val acc = new ChecksumAccumulator
      for c <- s do
        // shifting char 8 bits (1 byte) to the right
        acc.add((c >> 8).toByte)
        // also adding original char value in bytes
        acc.add(c.toByte)
      val cs = acc.checksum()
      // add the tuple for this string and checksum to the cache.
      cache += (s -> cs)
      cs



val acc = new ChecksumAccumulator
val csa = new ChecksumAccumulator

ChecksumAccumulator.calculate("chapter 4 is enlightening")

// SINGLETON OBJECTS THAT ARE NOT COMPANION OBJECTS

// these can be used for a lot of things, like housing utility methods or defining the entrypoint to a scala application. 

import ChecksumAccumulator.calculate

object Summer:
  def main(args: Array[String]): Unit = 
    for arg <- args do
      println(arg + ": " + calculate(arg))

val args = Array("here", "goes", "nothing")
Summer.main(args)

// CASE CLASSES
// scala has the declaration in front of a class that has a 'case' modifier in front of it
// these types of classes implement the hashCode, toString, equals and factory methods for you
case class Person(name: String, age: Int)
//ok, so now we have a Person companion object for nothing, it has an apply factory method as well
// this is really val p = Person.apply("Sally", 39), which sets the private members of Person to these values
val p = Person("Sally", 39)
p.name
p.age
p.toString
p == Person("Sally", 21)
p.hashCode == Person("Sally", 21).hashCode
p == Person("James", 39)
p.hashCode == Person("James", 39).hashCode
p == Person("Sally", 39)
p.hashCode == Person("Sally", 39).hashCode

case class Person2(name: String, age: Int):
  def appendToName(suffix: String): Person = 
    Person(s"$name$suffix", age)

object Person2:
  //this object will override the parts of Person2 that we don't want the compiler to do for us
  def apply(name: String, age: Int): Person2 = 
    val capitalizedName = 
      if !name.isEmpty then
        val firstChar = name.charAt(0).toUpper
        val restOfName = name.substring(1)
        s"$firstChar$restOfName"
      else throw new IllegalArgumentException("Empty name")
    new Person2(capitalizedName, age)

val q = Person("sally", 39)
val r = Person2("sally", 39)
// the below throws the illegal arg exception
// val failPlease = Person2("", 39)
r.appendToName("jones")

/* final note about case classes: 
All these conventions add a lot of convenience -- at a small price. 
You have to write the case modifier, and your classses and objects become a bit larger.
They are larger because additional methods are generated and an implicit field is added fo reach constructor parameter.
*/
// can't access the internal sum, its private
// acc.sum = 3

//semicolon inference 
val s = "hello"
println(s)

// same line you need the semicolon, complier can't infer it
val t = "hello"; println(s)

// but you can also split a statement up with newlines for readability and scala will know you don't need semicolons: 
val x = 1
if x < 2 then "too small" else "ok"

if x < 2 then
  "too small"
else
  "ok"

// semicolon inference rules: 
// the end of a line will be considered a semicolon unless:
// 1. the line ends in something that couldn't end a statement, like a period or infix operator
// examples someObject.\n,  1 + \n, (1, 2, 3,\n
// 2. the next line starts with a word that can't start a statement
// examples if x then \n (as above)
// 3. the lines end within parentheses or brackets, since these have to only contain one statement. 



val myList: List[String] = List("hi", "I'm", "me")
myList.map {item => s"${item} Taylor"}

myList.flatMap { item => List(item)}
