
/* next example of applied typecalsses, multiversal equality

concept: scala has universal equality concept. everything is an object, so we should be able to compare any two objects 
with == and also != operators. there was also support built in for cooperative equality. where different types compare themselves for equality with other 
types that cooperate. example of this is Int and Long becing compared to each other with scala 2 just like in Java with 
no conversions happening.

so, those things exist, but they mask bugs, where something will appear to be correct as a universal equality comparison, but 
it really was the author of the code accidentally changing the type to two uncomparable types and happening to want the result 
of the comparison to be 'false'. Scala 3 has implemented something else that is an improvement in the safety of 
the code that will actually give you a message about the types fundamentally not being comparable. The feature that enables this 
is called multiversal equality. This is an enhancement to the universal equality treatment for == and !=
the definitions of == and != in scala 2 to scala 3 remained unchanged, but the compiler behavior changed.

here are the defnitions of the equality comparison operator methods: 

*/

// final def ==(that: Any): Boolean
// final def !=(that: Any): Boolean

/* 
here is the procedure for universal equality in the scala 2 compiler: 
    1. encounter == or != somewhere in the code. 
    2. are lhs and rhs both primitive? => then emit the code to compare them efficiently
    3. is one side a reference type? => emit code to box the primitive and get apples to apples with reference types
        3 a. check if the left operand was null. => then do a comparison for null equality up front to avoid null pointer exception
        3. b. ok, finally compare the two reference types for equality as normal
    
and what changes for the scala 3 multiversal equality? well, the compiler looks for a given instance of 
CanEqual, which is a typeclass  as the first step before doing all of these things. 
*/

// sealed trait CanEqual[-L, -R]

/* 
    placeholder for taking notes on the CanEqual typeclass

    what does the minus sign mean again? as if I even knew the first time. 
    I know it has something to do with co variance, variance, and contravariancew

    ok, it means that CanQqual is contravariant in both L and R arguments. 
    this apparently means that CanEqual is a subtype of any other type and it doesn't matter 
    what the L or R types are. that means that you could use an instance of CanEqual[Any, Any]
    to give permission for equality comparison between any two types. 
    e.g. say you have a given instance CanEqual[Int, Int], you can use CanEqual[Any, Any] and that
    will satisfy CanEqual[Int, Int] 

    So apparently that meanas we can define CanEqual as a sealed trait with just the type of 
    CanEqual[Any, Any] as its member. you can provide this object called derived, which is defined in the 
    CanEqual companion object: 

object CanEqual:
    Oobject derived extends CanEqual[Any, Any]

ok, interesting, so then they say that if you want to satisfy can Equal with a given instance, 
you have to then provide the only applicable instance, since none of the other CanEqual types are 
part of the trait and therefore can't be extended to have them. you have to supply CanEqual.derived. 

but the plot thickens, the compiler still has to allow some exceptions to this 'only one type allowed' business
to have backwards compatibility to scala 2, so it does allow some equality comparisons even if the given instance has not 
been provided. 

1. if L and R are the same type
2. if either L or R is a subtype of teh other after they are _lifted_ (Ooo there is that magic word).
3. there is no given _reflective_ CanEqual instance 
4. there is no given reflexive instance, such as one with (L, L)

for that last one, the presence of a reflexive instance is a sign to the compiler that it shouldn't just allow 
equality comparisons to happen if there is a way to compare values of the type to itself. not sure why this 
backwards  compat is necessary. So here is an example of hwo that is used. Scala 3 would have allowed equality comparisions
between String and Option[String], but because there is a given instance provided for CanEqual[String, String], 
that means the scala 3 compoiler will notice this and not allow the comparsion between String and any other type 
without a given instance of CanEqual specifically permitting it

so there will be a smooth upgrade path with backwards compatibility to scala2 example: 

 */

 //typeclass derivation basically adds the lines that are commented out below
case class Apple(size: Int) derives CanEqual
    // the below is supposed to get around the stricness, but isn't working
    // object Apple:
        // given canEq: CanEqual[Apple, Apple] = CanEqual.derived

val appleTwo = Apple(2)
val appleTwoToo = Apple(2)
appleTwo == appleTwoToo

/* so the point here is that the comparison will work in scala 3 because the left and right sides are the same
but then you have this issue which will also work in scala 3:
*/

case class Orange(size:Int)
val orangeTwo = Orange(2)
// appleTwo == orangeTwo
 
/* so it is tru that these are not the same, but the compiler should have caught this and thrown an error, 
not waited for the condition to evaluate all the way */

// but the scala 3 compiler allows you to either pass a compiler option for strict equality or you can import the following

import scala.language.strictEquality

// appleTwo == orangeTwo < == this doesn't compile it is working now. 

// but I want to make it possible to compare apples and other things, 


appleTwo == appleTwoToo
// appleTwo == orangeTwo <- this still doesn't compile under strict mode because we have a reflexive instance

// question after going through this part. is derived always going to be a version of the typclass with Any  as the type parameters?

