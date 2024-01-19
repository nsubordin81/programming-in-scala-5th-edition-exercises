/* 
welcome to another worksheet in the series "mr. T visualizes himself eating the moon one bite at a time." where I will talk about
a topic that I may  or may not have enough background to digest yet but I think I get the core underpinnings of: Typeclasses.
let's review, shall we? 
- typeclasses in scala 3 done in an idiomatic way seem to be done with the help of context parameters, a.k.a 'givens'
- the scala compiler actually has protocols about looking for implicit values hanging out in the scope of a function when it is 
called, that can be looked up, either up a type hierarchy, in a companion object, or directly in lexical scope through an import, 
and when it finds it it can provide it. That isn't a typeclass necessarily, that is just a given. so that can be used anytime 
you want your program to have access to somethign without having to pass it down through function parameters over and over or
extend some class and have properties passed down and made available that way.

- what typeclasses allow for you to do is leverage this ability of the compiler to go fetch something for you to 
specify the type of what is being fetched and supply a given for it of a particular type that the compiler will expect
when a function is called. so that function will be like "hey, i'm 'using' this Type and that type is parameterized by [T]" 
and then in some companion trait for that type you provide some definitions for your T type for the behavior that is expected, 
and so the method that is 'using' your typeclass trait sets a contract taht it will only deal with types that it can find such a definition 
for. That is why it is called 'ad-hoc polymorphism'. as opposed to 'inheritance polymorphism'. in inheritance polymorphism, the 
is-a relationship that is more or less set in stone between the types in a hierarchy dictates whether one type can be swapped in for anotehr.
in this typeclass ad-hoc polymorphism setup, you can be more extensible and dynamic, where you can define a new instance that falls into this set
of types with the necessary behaviors outside of any hierarchy, and you can provide that instance just in time, a long time after
the calling method was written, and so there is no need to predict whether or not you will need it or worry about how you have to 
maintain the inheritance in the event someone else changes things, your newly defined type instances that fullfill this contract are 
your own an dthey are independent of each other or any concrete supertype. 

ok, now that I wrote a book trying to say what I thought typeclasses were and why they eere better than traditional inheritance for many use 
cases, time to follow along with the book again.

*/

/* right off the bat the book is dropping knowledge bombs on me
- there are two other ways to constrain what types can use behaviors in scala that aren't typeclasses
    - you can write some overloaded methods, so based on the parameter list and differences in the signatures something different will be used, but nothing 
      outside the overloaded list will be used. 
    - requiring that the class passed in as a parameter to a funciton mixes in a particular trait  
    - finally, you can define a fypeclass and write teh function to work with types for which a giv3en instance of the typeclass trait is defined 
    -- the third is supposed to be the most flexible, but we don't know why yet, let's find out.   

  first off, typeclass doesn't mean what you think it means. it really means 'a set of types that are grouped tog3ether for some purpose'. you probably
  thought it meant 'a special kind of class that in some way incorporates type information' didn't you? thought maybe that was the case. don't worry past T,
  I"m future T and I'm hear to correct all of your mental; inconsistencies. just wait for me to exercise my incredible knowledge and evolved modes of thought to 
  rock your world. ok. anyway...

  so typeclasses support ad-hoc polymorphism, which we have beaten to death above, but just shorthanding here as 'you can restrict functions to work
  with only this subset of types'

  original usage of this term ad-hoc polymorphism was applied to things like + and - for langauges like C++ that did operator overloading. 
  if your type didn't implement the + operator, then the function wouldn't work for it, and you'd get a compilation error. In 
  Scala, you don't implement an operator on the class, that isn't a thing, methods themselves can be named like operators, you could do it in scala, but it 
  would be method overloading you would achieve it with. example: 

  def -(x:Double ):Double
  def -(x: Float):Float
  def -(x: Long):Long
  def -(x: Int):Int
  def -(x: Char):Char
  def -(x: Short):Short
  def -(x: Byte):Byte

  so whether these are defined in the same type or not, if they are all in lexical scope, then you will be able to use them in subtraction

so pretend this is all that scala had defined - for. that means, these 7 examples comprise the set of types that will work with the method '-'
that is what a typeclass is.

oh but wait! you can also achieve this in scala with a class hierarchy!

sealed trait RainbowColor
    class Red extends RainbowColor
    class Orange extends RainbowColor
    class Yellow extends RainbowColor
    class Green extends RainbowColor
    class Blue extends RainbowColor
    class Indigo extends RainbowColor
    class Violet extends RainbowColor

so that is a hierarchy that can't be extended outside of itself, and you can define a method that takes some 
RainbowColor subtype as an argument, 

def paint(rc: RainbowColor): Unit

and you could only pass in arguments that have one of the eight types shown in that list. there will be a compiler error if you try to 
use something other than that type. So this is technically a special case of ad hoc polymorphism called 'subtyping polymorphism' 
because the classes of all the instances passed to paint have to mix in RainboColor, and also adhere to any constraints established by the interface of RainbowColor. 

so there is a difference between the rainbow and the - examples. in the - example, the overloading of methods one, the types that
are implementing - don't have to adhere to any constraints imposed by any type except the one they all have in common to inherit from, 
which in scala is the 'Any' type. 

you can sum that all up to one key difference between the two approaches
- subclassing polymorphism is for let you use types interchangeably that are *related*worksheet
- ad hoc polymorphism allows you to use types interchangeably that are * unrelated* or at least can be


so when to use which? 
- subtyping is good when you have a well defined, self-contained family of types. enums are a good example, days of the week, colors of the rainbow,
floors in a building, planets in the solar system, etc. sealed traits also. so these things
    - are small in number, not a lot to manage
    - you can think of and define the whole set now, you don't expect it to change frequently
    - the types are related, you know what they have in common and the then that they have in common shares some code you'd like to reuse
    - maybe they are unsealed but are very related and tend to be extended together with the same set of behaviors
    - the behaviors that they are sharing through the polymorphism are not these extremely comman and widely applicable behaviors. for example: 
        - serialization - any object can be written into a bystestream and sent over the wire, or at least you want that for many different kinds of objects as a feature
        - ordering - whether things are  in order is a common concern that could come up for many of the types you would define, especially if they are some type of collection

    picking on their own standard library, the authors here (I presume this includes some scala core contributors) talk about the Ordered trait

    it is a trait that supports ordering with subtyping. you have to mix Ordered trait into a class and implement the compare method. if you do this, 
    you get implementations of <, >, <=, and >= for free with it. you als can now use Ordered as an uper bound in a sort method. I'm not sure what 
    they are saying here exactly, note to self look at Listing 18.11 and record what you learn abot upper bound . 

    I need to go back and go over the type variance chapters, they are starting to use those to make a point here and I'm not getting it. 

    section 11.2 one way traits are used: add methods to a class in terms of methods the class already has. 
    so you ahve a thin interface of a class and you make it right by extending it with more methods

    rich interface - nice for calllers, have everything they would need, they can pick a more specific behavior and have to do less coding on top of it 
    to get what they want. 
    thin interface - fewer methods, easier on the implementers because they only have to maintain those methods. 

    traits make it so that you define the method one time in the trait and then mix it in to the interface of the classes that 
    mix in the trait. that means you don't hae to keep reimplementing the extra methods for each new class, just mix them in. 

    the way to do this in scala with traits is to implement some abstract methods in the trait that represent your thin interface, 
    and then also implement some concrete ones and have a class mix in the trait and implement the abstract ones so that it can get the rich interface
    that comes with the trait. 

    so this section uses the example of Ordered trait to demonstrate how you can simplify the interface of anything that needs 
    comparison operators. you define compare() method in terms of the type of the class you are implementing and you get <, >, <=, >= all
    for free for that type. 

    you don't get = though because apparently due to type erasure you can't know the type of the passed in object to do a proper equals() override
    and there is a way around this but they have chosen to reserve it for "Advanced Programming in Scala" which I don't have a copy of. 

*/

trait Ordered[T]:
  def compare(that: T): Int

  def <(that: T): Boolean = (this compare that) < 0
  def >(that: T): Boolean = (this compare that) > 0
  def <=(that: T): Boolean = (this compare that) <= 0
  def >= (that: T): Boolean = (this compare that) >= 0

/* this trait comes up again in chapter 18 when talking about uppwer bounds and variance. I need to read this full chapter to 
fully appreciate what it is saying, but let's just go over the sectoin they referenced for now. they pull in 
the ordered trait by having Person inherit it to get the comparison operators. They implement the compare method as described before
in terms of the last name and first name  as a series of 'rules' that are pretty intuitive. if the last names are the same irrespective of casing and
the first name is also this way, then you probably have the same person, otherwise you will get the typical 1, -1, etc. result and use that for <, >, <=, >=

hooray, 2 for 1 deal, this also covers upper bounds so I don't have to look somewhere else for it. T<: Ordered[T] indicates 
that the type that the list is parameterized with in the signature of 
def orderedMergeSort[T <: Ordered[T]]<xs: List[T]): List[T] 
must be a sybtype of a type that mixes in Ordered. So you have that subtyping constraint in place. got it.

So now we are back to chapter 23 and typeclasses and why the inheritance and subtyping polymorphism can be a hinderance more than a help
if you share the behavior and create type constraints this way, you are additionally creating the constraint that 
any type that participates in this set of types will need to mix in Ordered[T], and also adhere to the Ordered[T] interfeace

so what could happen? well, the class you mix Ordered into could define methods where the names or contracts are in conflict with ordered
do I really know what this means? I guess it means it already has a 'compare' method in this case? maybe it expects somethign else to be true
about the class that isn't true in Ordered? I'm reaching a little, I have no idea how often this is an issue without trying it out a few times. 

variance conflicts next. looking at the Hope Enum: 


*/

enum Hope[+T]:
  case Glad(o: T)
  case Sad

/* this is part of a treatise on algebraic data types which I know nothing about and am coming in in the middle of. so let's see how long 
I can coast without reading yet anothe rprior chapter of the book (so far I'm 0 for 3 on avoiding this).lazy val 
generally the enum is introduced to talk about composite types. that is because Glad and Sad get combined into Hope
cardinalities in a compbined type that follow the laws of addition are called sum types. in this way we have 
an enum. so they discuss in this chapter, though I'm only reading one paragraph, that there is a cardinality of Hope
that is equaal to the sum of the cardinalities of Glad[T] and Sad. Glad[T] has the cardinality of T. Whatever 
the number of different values T can represent, Glad can represent that number of values. Boolean can do 2, 
 so Glad[Boolean] can do 2. Sad can only do Sad because it is a singleton and doesn't have a type parameter. So it has a 
 cardinality of 1. there is a chart to show it, but it is basically T+1 for Hope[+T]

 This is all very interesting and I'm going to come back to it, but how does it relate to typeclasses and the point that Hope[+T] was \
 summoned for in the first place? 

 well, it shows that if you were to mix Ordered[T] into Hope[+T] 

the idea being that compare will make Sad the lowest value adn then ordering Glad according to the ordering rules that 
the parameterized type uses. 

 class Hope[+T <: Ordered[T]] extends Ordered[Hope[T]]

 doesn't work because HOpe is covariant and Ordered is invariant. So the type parameters don't agree

 another issue with subtayping polymorphism is that interfaces already existed and are incompatible.

 more often though, really, is that you can't go back and touch the interface.

 for example, glaring example, Int doesn't mix in Ordered, so you can't sort List[Int] with the orderedMergeSort 

 this is probably the overwhelming reason to use typeclasses over subtyping for polymorphism. For subtyping, 
 the author of the code would have had to anticipate that your trait was going to exist and extended it 
 for typeclasses, you can 'grandfather' libraries and classes that already exist into the set of types that work
 with your functions. 

 This is what I remember from reading about the extension problem and the solution to it that was typclasses. 

 so the cool thing, and the core thing to remember probably, about typeclasses is that they create a new hierarchy 
 centered around the shared behavior and concept of executing that behavior instead of codifying the hierarchy 
 with a known set of types that have to extend each other's interfaces as subtypes to become members. 

 ensuring the compatibility becomes easier because the concept that you are focusing on tfor the typeclass
 is one thing, you don't have to worry that you are muddying up an interface by having your types participate. 
 also, you can kind of use the typeclass like a wrapper when you declare instances, you specify the type that 
 the isntance will operate on in your typeclass given instance and then augment it there, instead of having that
 type itself inherit from some hierarchy. offshoot of that is you don't have to change the type in any way.

 so now they revisit the Ordering trait. surprise! it is also a typeclass. we were using subtyping for it, but 
 we didn't have to. for things that don't fit well into the hierarhcy or are classes we can't add to the hierarchy, we can still easily 
 provide an instance of them. so we do this for that Hope enum from before: 


*/

object HopeUtils:
  // add type t for all types that are members of the typeclass already, lets add a hope type to that
  given hopeOrdering[T](using 
    ord: Ordering[T]): Ordering[Hope[T]] with 
    def compare(lh: Hope[T], rh: Hope[T]): Int =
      import Hope.{Glad, Sad}
      (lh, rh) match
        case (Sad, Sad) => 0
        case (Sad, _) => -1
        case (_, Sad) => +1
        case (Glad(lhv), Glad(rhv)) =>
          ord.compare(lhv, rhv)

