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

*/

