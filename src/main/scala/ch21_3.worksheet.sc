
/** this statement, what does it mean?
context parameters are perhaps most often used to provide information about a type mentioned explicitly in an earlier 
parameter list, similar to the type classes of haskell. this is an important way to achieve ad hoc polymorphism.

let's break this down. context parameters get filled in implicitly by the compiler, but 
their most common usage method is to 'provide information' about a type that was 'mentioned explicity'
in an earlier parameter list. 

so say some function signature has a type in it, like Movement. people use context parameters
to implicitly provide the concrete behavior of Movement that should be done when some other method is called? 
that's my best guess but it is hard to trace the def without seeing and example. let's get into it

here is what Claude has to say: 
def getSize[T](obj: T)(implicit sizer: Sizer[T]): Int

so in this example, the sizer context parameter is providing metadata or behavior to the 
type T that getSize is parameterized with. So it is like saying 

"for this method, I'm going ot operate on a type T in some way, but T is too broad, I want to instead operate 
provide the implicit context that T uses this sizer parameter either as behavior or a field
and that contract has to be satisfied for this function to work with T. It means that 
I can use any T though so long as I get an implicit definiton of sizer in scope" So you can defer
defining sizer and a client can use getSize later and provide the sizing algorithm through their 
implicit definition of sizer
*/

// ok so now for the books definition, for namespace considerations, let's name this differently

def insertionSort(xs: List[Int]): List[Int] = 
    if xs.isEmpty then Nil 
    else insert(xs.head, insertionSort(xs.tail))

def insert(x: Int, xs: List[Int]): List[Int] =
    // need to insert x into xs
    // insertion sort I forget, maybe look at middle and see if it is greater or less
    // then look at middle, ..., then  put it in
    // nope! it is this:
    if xs.isEmpty || x <= xs.head then x :: xs
    else xs.head :: insert(x, xs.tail)
    // which is the basic, start from left and move x until you find something bigger than it

val sorted: List[Int] = insertionSort(List(3, 2, 1))

// def isort2[T](xs: List[T]): List[T] =
//     if xs.isEmpty then Nil
//     else insert2(xs.head, insert2(xs.tail))

// def insert2[T](x: T, xs:List[T]): List[T] = 
//     if xs.isEmpty || x <= xs.head then x:: xs
//     else xs.head :: insert(x, xs.tail)

// so the above won't compile up front because T is so generic that the compiler doesn't have a 
// definition of the comparison operator available to reference to make sure that the insert method
// will work at runtime. So, we can introduce a context parameter to provide that behavior
// when the insert method needs it

// in this case, the List collection has deferred the implementation for how elements should be compared
// to the types they operate on. when isort was concrete with Int, it worked fine because List[Int] cmeant 
// the compiler could look at Int and see it had a definition for .<= method. now, T needs to provide one, 
// but once it does, we'll be able t work with any T that also has this behavior

// but we are getting ahead of ourselves, what if we just provide a local impletmentation of <= for T in this method
// with an explicit parameter?


def isort3[T](xs: List[T])(lteq: (T, T) => Boolean): List[T] =
    if xs.isEmpty then Nil
    else insert3(xs.head, isort3(xs.tail)(lteq))(lteq)

def insert3[T](x: T, xs:List[T])(lteq: (T, T) => Boolean): List[T] = 
    if xs.isEmpty || lteq(x ,xs.head) then x:: xs
    else xs.head :: insert3(x, xs.tail)(lteq)


isort3(List(4, -10, 10))((x: Int, y: Int) => x <= y)

// you need parentheses around the compareTo here using infix notation, because the order of evaluation 
// is right to left for two functions, and so '<=' will go before 'compareTo' and get 'y' as its left hand operand, 
// which is a string but <= is an Int to Int comparison in this context
isort3(List("cherry", "blackberry", "apple"))((x: String, y: String) => (x compareTo y) <= 0)
// isort3(List(Rational(7, 8), Rational(5, 6), Rational(1, 2)))((x: Rational, y: Rational) => x.numer / y.denom <= x.denom * y.numer)
// left out the Rational one because I think that class was an exercise in the book and I don't have it readily available yet. 

/* Ok, so what have we now learned about why context parameters are useful? well, it looks as though we could achieve the same generality
of the insertion sort function by having an explicit parameter that supplied the comparison method as a function. but to do so, we had to have
it in the context of the original insertion sort method and then also to the insert method it called down to. Really it isn't used until the insertion 
method, but because the type T is linked to this explicit function parameter, we couldn't define it local to the insert function. so now we are just
hiding away the definition of our explicit parameter to keep the focus on the important thing, which is that we are sorting two numbers with 'some' method.
j */

// when we do this hiding though, we need to make a more specific type label to hold it, so that it can be implicitly provided without colliding with anything. 
// and also, when you define a specific type to hold this implicit behavior, you are also creating a name for what you want to do with that parameter. it also 
// lets you create a place where you can add other parameter values that are implicitly provided for types and this doesn't break the code you'v ealready set up. 
// quesiton for claude for later, sure this doesn't break existing code, but why would the alternative do so? if I had an explicit parameter, then I'd probably 
// need to add or remove more explicit paramters to achieve the change in functionality, but I feel like I would have had to touch something int he fuction 
// to leverage any new implicit parameters anyway. but it seems like maybe that is not true, I might be able to do some things in a more extensible way, I just don't quite see
// it yet. 

// this might be the example I want, a type that is about ordering two elements
trait Ord[T]:
    def compare(x: T, y: T): Int
    def lteq(x: T, y: T): Boolean = compare(x, y) < 1
    def lt(x: T, y: T): Boolean = compare(x, y) < 0
    def gt(x: T, y: T): Boolean = compare(x, y) > 0 
    def gteq(x: T, y: T): Boolean = compare(x, y) > -1

    // ok so fleshed out this Ord trait with more of the comparison operators, and then from chapter 22 I 
    // have these extension methods. not onnly are they named with the operator names, but they will also be 
    // rewritten by the compiler so that I can write them as if they were on the 'T' type as a method, so 
    // say T was Int concretely. There is a given for Int defined in the Ord type class object, so that means
    // I can do something like what I just rewronte insert4 to do. it is using a given which is Ord[T]
    // so if the list I pass in is Int, then the comparison x <= xs.head will summon the Ordt[T] extension method 
    // for <= in Ord

    // some cool things here, notice that there is no need to have (using tc: ) here. you are in the trait which is the 
    // type class, so you are invoking the extension on same parameterized type as Ord[T], the T type. and 
    // because you aren't using ord in that other way, with a given instance, you also can just call the methods off of the 
    // this object, like lt instead of ord.lt. the typclass part will still work the same way and so will the extension
    extension (lhs: T)
        def < (rhs: T): Boolean = lt(lhs, rhs)
        def <= (rhs: T): Boolean = lteq(lhs, rhs)
        def > (rhs: T): Boolean = gt(lhs, rhs)
        def >= (rhs: T): Boolean = gteq(lhs, rhs)

// ok so this doesn't show too much that I'm removing a lot of verbosity, I still have to supply the Ord type with using at both sites, but then 
// when invoking isort4 I don't have to pass the sorting algorithm in at the callsite for insert4 and when calling the public interface of isort4 I won't have to either

def isort4[T](xs: List[T])(using ord: Ord[T]): List[T] =
    if xs.isEmpty then Nil
    else insert4(xs.head, isort4(xs.tail))

def insert4[T](x: T, xs: List[T])(using ord: Ord[T]): List[T] =
    if xs.isEmpty || x <= xs.head then x::xs
    else xs.head :: insert4(x, xs.tail)

// but now I do need to provide a sorting function to be provided by the compiler, so I need a given
/* it says a good home for natural givens (a natural way to do something with a type, like compare it to another type) is in the companion object of a type that is 
'involved'. So for this, the comparison we want is for Int and Ord is the trait type representing the operations, we can choose to update the object for either of these types
with the given we want. The compiler will look in lexical scope firest, and then the next place it will look is the companion objects of those types that are 'involved'
apparently though we can't access the companion object for Int (why???), so we will need to update Ord's
*/

// object Ord:
//     // until we do the whole thing, there is a comment here reminding use that this is 'not yet idiomatic'
//     given intOrd: Ord[Int] = 
//         new Ord[Int]:
//             // ord trait requires a definition for compare
//             def compare(x: Int, y: Int) =
//                 if x == y then 0 else if x > y then 1 else -1
            

// aliases we've seen before are called alias givens. you use given as the keyword for a variable and then it aliases whatever is on the other side
// usually the right hand side of that assignment is an anonymous instance of a trait or class. scala knows that this is the cases and you don't have to 
// type the equals sign or use the 'new' keyword to define the anonymous instance. 
// so let's comment the above out and redo our companion object definition with the 'with' keyword

object Ord:
    given Ord[Int] with
        def compare(x: Int, y: Int) =
            if x == y then 0 else if x > y then 1 else -1

    given Ord[String] with
        def compare(x: String, y: String) =
            (x compareTo y)

isort4(List(10, 2, -10))
isort4(List("cherries", "apples", "strawberries"))

// notes about givens. if the delcaration doesn't take value parameters, then the given is initialized the first time 
// it is accessed. if it does take value paramters, then a new given is created on every access. the compiler handles this and also 
// handles marking them as available for using declarations. 

// not to self, refresh yourself on lazy vals and defs. 

/* an interesting way to think about context parameters vs. lazy vals and defs. vals you provide a term and a value and the compiler infers type for you
with context params, you provide the type and the compiler infers which of its available terms to supply when the type is required. 
thus, there is something called term inference, just like type inference, that the compiler is doing for you here. */

/* a fun offshoot of thinking about term inference this way, is that you label the term when it is a val, and you can label a given, however, the compiler
couldn't care less about this name, you can leave it off and declare them anonymously instead. */

// to prove this to myself I just too kthe names of my givens out in that Ord companion object, it still works!
// it seems like a stylistic choice to not use the term name when declaring givens depending on whether or not you care to tell the reader of the code your intent

/* get ready, we are referencing the Liskov substitution principle: objects can be replaced with their subtypes without altering the desireable properties of 
the program. working backwards from what this means, we could assume that because now we have created behaviors that match across several types (all 'orderable'), 
that they can replace each other without disturbing the functionality of the methods that need sorting. by LSP, that suggests there is a 'supertype' out there
which can encompass the sorting behavior that these types share that they coudl all inherit from. They don't though, and in this example they can't because we 
provided the behavior to two types that can't subclass anything because they can't be modified, they belong to the standard lib (Int, String)
ok, ok, so what are these things then? well giving them instances of Ord[T] with their type defined makes them membershiop of a set of types outside of a hierarchy
defined by the fact that those types can be sorted. The name for a set of types like this is a typeclass. and this is also where we get our clearer picture of what
ad-hoc polymorphism refers to. methods that use context parameters to supply member instances of type classes are engaging in ad hoc polymorphism, because there is no 
class hierarchy being followed, but the method will only compile if the type it is called with has a given in scope. */

// FYI to self, this example is really a partial implementation of the math.Ordering typeclass from the scala standard library. 

// you also don't need to give a name to context parameters that are just pass throughs to the functionsl that actually use the parameters. this is called an anonymous parameter

// I may need to rewrite this several times or invent my own examples so it really sticks. 

/* and pasting this tidbit from the book: 

Change the properties of one ball without affecting the others using the Inspector.




*/


/* order scala compiler uses to find extension methods

ok so you have some object in scala, say it is the number 1, 1.addOne. scala compiler does 
1. is it on Int? that is the calss of the type itself. if it is, no need to look further
2. otherwise candidate compile error. so scala goes into try catch mode, first
    2. a. does an extension heal it? 
        - look in lexical scope for extension methods as the first phase
            - defined there
            - imported there
            - inherited from a supertype that this scope is using
            - example, say that we import the TwosComplementOps.absOption from the ch22 worksheet,
              then it will work in that acope because it is resolved in lexical scop
        - second phase: 
            - look at the given instances defined in lexical scope
            - look t members of companion objects of the receipvers class, supoerclasses, and supertraints
            - look at the members of given instances in those same companion objects
            - another way is to attempt to tranform the type of receiver with an implicit conversion, that is 2b
    2. b. does an implicit converstion heal it? 

    in either of these two phases, isf the compiler sees a more specific extension method, it will use that one. 
    for example, if the extension method was for a supertype and there is a subtype defined, or the given was for a type that
    the trait inherits from, it will use t emroe explicit one. this goes back to needing to define specific types 
    for your context params so that there isn't a chance of accidentally getting the wrong things passed in in context. 
         
3. if you couldn't find anything with 2 a or 2 b, throw that canadiate compile error
*/