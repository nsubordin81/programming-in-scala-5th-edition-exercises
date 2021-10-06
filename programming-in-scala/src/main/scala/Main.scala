import scala.collection.mutable
import scala.collection.immutable.HashSet


object Hello:
  // Step 4
  def main(args: Array[String]): Unit =
    for arg <- args yield
      println(arg)

  // commenting other steps below because it creates a lot of main methods

  def step7_1(): java.math.BigInteger =
    val big = new java.math.BigInteger("12345")
    return big

  def step7_2(): Array[String] =
    val greetStrings = new Array[String](3)

    /*
    parentheses for indexing an array
    this is a general rule in scala
    opposed to java arrays with special case []
    all objects in scala have apply() and
    update() methods and all that is happening
    here is greetString.update(0, "Hello")
    */
    greetStrings(0) = "Hello"
    greetStrings(1) = ", "
    greetStrings(2) = "World!\n"

    // infix notation on method 'to'
    // operators like + are really methods too
    for i <- 0 to 2 do print(greetStrings(i))
    return greetStrings

  def step7_3(): Array[String] =
    val numNames = Array("zero", "one", "two")
    return numNames

  def step8_1(): List[Int] =
    return List(1, 2, 3)

  def step8_2(): List[Int] =
    val oneTwo = List(1, 2)
    val threeFour = List(3, 4)
    // concatenate two lists with ::: it is an operation on immutable lists, new list created
    val oneTwoThreeFour = oneTwo ::: threeFour
    println(oneTwo)
    println(threeFour)
    return oneTwoThreeFour

  def step8_3(): List[Int] =
    val twoThree = List(2, 3)
    // cons most common operations on lists, immutably adds an element, is an operator of the second operand
    val oneTwoThree = 1 :: twoThree
    return oneTwoThree

  def step8_4(): List[Int] =
    // another way to instantiate a list
    // Nil is a list that is empty, but it has ::, so going from right to left
    // it will always be a list adding a new Int element
    val oneTwoThree = 1 :: 2 :: 3 :: Nil
    return oneTwoThree

  def step8_5(): List[Int] =
    val oneTwo = List(1, 2)
    // append. Generally if you don't need to it ia better to prepend because prepending is constant time
    // while appending is linear with n = size of list. Suggestions, prepend then reverse or use a ListBuffer
    val oneTwoThree = oneTwo :+ 3
    return oneTwoThree

  def step8_6(): List[String] =
    // empty list
    val emptyList = Nil
    val anotherList = List("basic", "list")
    println("with apply " + anotherList)
    val nonEmptyList = "my" :: emptyList
    println("empty and cons " + nonEmptyList)
    val concatList = nonEmptyList ::: anotherList
    println("concatenate two lists " + concatList)
    val basic = concatList(1)
    println("print indexed item " + basic)
    val elementIsFour = concatList.count(s => s.length == 4)
    println("how many elements have 4 chars: " + elementIsFour)
    val removeOwnership = concatList.drop(1)
    println("removing first element: " + removeOwnership)
    val removeDescription = concatList.dropRight(1)
    println("removing last element: " + removeDescription)
    val find = concatList.exists(s => s == "basic")
    println("does list have word 'basic' inside? " + find)
    val usePredicate = concatList.filter(s => s.length > 2)
    println("get every word with more than two chars: " + usePredicate)
    val checkAll = concatList.forall(s => s.length > 1)
    println("True if all elements are greater than 1 char: " + checkAll)
    val loopOver = concatList.foreach(s => print(s))
    println()
    val loopOverConcise = concatList.foreach(print)
    println()
    val getFirst = concatList.head
    println("get the first element of the List: " + getFirst)
    println("all but last: " + concatList.init)
    println("check if empty: " + concatList.isEmpty)
    println("get the last element: " + concatList.last)
    println("how many elements: " + concatList.length)
    println("do something to all elements: " + concatList.map(s => s + "ly"))
    println("convert to string: " + concatList.mkString(" "))
    println("inverse filter: " + concatList.filterNot(s => s.length < 3))
    println("reverse the list: " + concatList.reverse)
    println(
      "sort  alphabetically: " + concatList.sortWith((s, t) =>
        s.charAt(0).toLower < t.charAt(0).toLower
      )
    )
    println("get tail of list: " + concatList.tail)
    return concatList

  def step9_1() =
    val pair = (99, "Red Luftballoons")
    println("tuple value 1: " + pair(0))
    println("tuple value 2: " + pair(1))

  def step10_1() =
    // this is about mutability of data structures
    // scala calls lists different things when they are mutable vs immutable
    // arrays are mutable, lists are not
    // sets and maps manage mutability through the class hierarchy
    // sets have a base trait, then mutable and immutable sub traits with concrete children
    // they are all called Set, but FQnamespace is diff because package level separation
    // the packages are what are 'mutable' or 'immutable'

    // notice, jetset is a mutable reference, but the Set we assign to it is immutable
    var jetSet = Set("Boeing", "Airbus")
    // we can use += on a mutable reference, but scala will recognize that immutable map we are referencing
    // doesn't have a += method and it knows to expand this expression to
    // jetSet = jetSet  + "Lear", aka give me a new immutable set with "Boeing", "Airbus", "Lear"
    // and change jetset's reference to point to it.
    jetSet += "Lear"
    val query = jetSet.contains("Cessna")
    print("should be false, no Cessnas in jetSet: " + query)
    //mutable sets require imports, so at top we will import scala.collection.mutable
    //ok so seen now how the reference is immutable but the set is mutable
    val movieSet = mutable.Set("Spotlight", "Moonlight")
    movieSet += "Parasite"
    print("set of movies, with mutable set: " + movieSet)
    val hashSet = HashSet("Tomatoes", "Chilies")
    val ingredients = hashSet + "Coriander"
    print("ingredients list with a hash set because we can: " + hashSet)

  def step10_2() =
    //static factory method (surprised we don't need ())
    val treasureMap = mutable.Map.empty[Int, String]
    // -> method is available for all scala objects and it makes a tuple of (lhs, rhs)
    // then += on the mutable map accepts that tuple and adds it as an entry to the map
    treasureMap += (1 -> "Go to island")
    treasureMap += (2 -> "Find big X on ground.")
    treasureMap += (3 -> "Dig.")
    val step2 = treasureMap(2)
    print("this should be about finding an x on the ground: " + step2)
    val romanNumeral = Map(1 -> "I", 2 -> "II", 3 -> "III", 4 -> "IV", 5 -> "V")
    val four = romanNumeral(4)
    print("this shoudl be IV: " + four)

  def step11_1(args: List[String]): Unit =
    //mutable reference
    var i = 0
    while i < args.length do
      println(args(i))
      // mutating the integer i points to
      i += 1

  def step11_2(args: List[String]): Unit =
    // another preview of the exciting powers of
    // the for expression, which doesn't mutate the args list
    // or require a mutable index to work
    for arg <- args do println(arg)

  def step11_3(args: List[String]): Unit =
    // check out how concise, list has a foreach which doesn't have side effects,
    // and because we are passing in a function that takes an argument, we
    // don't need to bother with wrapping it with a lambda, it knows what to do with args
    // java method references were java's answer to this, but these require less syntax
    args.foreach(println)

  def step11_4(args: List[String]): String =
    args.mkString("\n")

  // important difference between foreach and map
  // foreach mutates in place in the same collection
  // map creates a new collection and transforms each element into a new element
  def step12_1(): String =
    val adjectives = List("One", "Two", "Red", "Blue")
    // since the anonymous function results in a  string, this will return List[String]
    val nouns = adjectives.map(adj => adj + " Fish")
    val result = "The Suess fishes are: " + nouns
    return result

  def step12_2(): String =
    // using a for expression this time
    val adjectives = List("One", "Two", "Red", "Blue")
    val nouns =
      // this compiles to a map call on adjectives
      for adj <- adjectives yield adj + " Fish"
    val result = "The Suess fishes are: " + nouns
    return result

  def step12_3(): String =
    // Vector, another type that implements Map,
    // has the additoinal quality that all of its operations
    // are constant time.
    val ques = Vector("Who", "What", "When", "Where", "Why")

    val usingMap = ques.map(q => q.toLowerCase + "?")

    val usingForYield =
      for q <- ques yield q.toLowerCase + "?"
    val result = "the questions: " + usingForYield
    return result

  def step12_4(): Option[String] =
    val ques = Vector("Who", "What", "When", "Where", "Why")

    val startsW = ques.find(q => q.startsWith("W")) // Some(Who)
    val hasLen4 = ques.find(q => q.length == 4) // Some(What)
    val hasLen5 = ques.find(q => q.length == 5) // Some(Where)
    val startsH = ques.find(q => q.startsWith("H")) // None

    val upWho = startsW.map(word => word.toUpperCase) // Some(WHO)
    val noHow = for word <- startsH yield word.toUpperCase // None

    return noHow

  //CH2
  /*
  // Step 4 using arguments
  @main def hello(args: String*): Unit =
    println("hellow, " + args(0) + ", from a script!")

  // Step 5 - iteration
  @main def printargs(args: String*) =
      var i = 0
      while i < args.length do
          println(args(i))
          i += 1

  @main def echoargs(args: String*) =
      var i = 0
      while i < args.length do
          if i != 0 then
              print(" ")
          print(args(i))
          i += 1
      println(

  // Step 6 - functional style
  @main def argsfunctional(args: String*) =
      // replace arg => println(arg) with just println
      args.foreach(println)

  // intro to for comprehension
  @main def forargs(args: String*) =
      for arg <- args do
          println(arg)

  */
  val msg = "I was compiled by Scala 3. :)"
