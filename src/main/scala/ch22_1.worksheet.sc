// comparing strings for equality but with special whitespace rules: 
// 1. need to trim whitespace on either end of the string
// 2. the regions of whitespace need to correspond, but within the regions, whitespace doesn't matter
// i.e. "the quick brown dog." should be the same as "  the  quick brown   dog.  ", but not "the quick brown dog ." because of the space before the period

def singleSpace(s: String): String =
    //transformation to make two strings with matching whatespace 'regions' the same string
    s.trim.split("\\s+").mkString(" ")

singleSpace(" this is     wait for it   the bomb")

// it works with those special whitespace chars too
singleSpace("this is so\n I mean \t omg")

// so this works, 
val s1 = "a       lot lot of space"
val s2 = "a lot lot of space    "
singleSpace((s1)) == singleSpace(s2)

// but to the point of this chaptger it would be prettier if, s1.singleSpace == s2.singleSpace was a thing
// even though we don't have access to the scala standard lib to update the string class

extension (s:String)
    def oneSpace:String =
        s.trim.split("\\s+").mkString(" ")

// rules: 
    // place a single variable of the type to which you want to add the extension
    // extension (s: String)
    // in this example, s is the 'receiver' variable, indicating that the type String will receive the method into it. 

s1.oneSpace == s2.oneSpace

// et viola!

// these are called applications of the extension method, when you use it like it belonged to the type in question like shown above

// implementation note about twhat the compiler is doing and what it is not doing. 
// it is not creating an anonymous class out of the extension method which receives that reciever as a constructor arg
// they instead rewrite the method in place to take the receiver as an argument and then maintains an internal extension marker
// so the compiler knows how to handle it when clients call if off the back of an object of that type. 

// the main reason for this is there is no extra boxing, which was a cost of scala 2 implicit classes. I'm not sure if I've followed that point through
// but suffice to say I think it means there were costs performance wise to doing it with an anonymous class just to get the code to be more readable. 

// the easiest way to to make an extension method available is to bring the  name of the rewritten method into lexical scope
// wtf is lexical scope? so this is the book's way of drawwing contrast between using something like context parameters or implicits 
// and just using the language support for explicit separation of the parts of a program and management of what identifiers are bound in that 
// portion of the program. 

// ok on to generic extension methods. 
// how does this differ from what I've already done? I just exxtended string to transform strings for comparison with each other

List(1, 2, 3).head
// but the below won't work, throws no such element exception. 
// List.empty.head

// so let's use a headOption method to get back and Option. A ha! I see where this is going now. headOption on the List 
// collection should owrk for any type that a collection can contain. 

List(1, 2, 3).headOption
List.empty.headOption

// ok, but we already ahve this, so why does this come up for extension methods? well, as the book says, tail doesn't have the same

List(1, 2, 3).tail
// this will throw exception no such element
// List.empty.tail

// but this won't work either because the method DNE
// List.empty.tailOption
// value tailOption is not a member of List[Any] - did you mean List[Any].lastOption?

// so we define a generic extension method so we don't ahve to work with Ints. 

extension [T](xs: List[T])
    def tailOption: Option[List[T]] =
        if xs.nonEmpty then Some(xs.tail) else None

List.empty.tailOption

List(1, 2, 3).tailOption

// cool!

// collective extensions now. 
// main idea here is that you don't just want to define one method. 

// for some reason again the authors decide that a good example is one that requires deeper comp sci knowledge
// fair enough
// twos complement. take an Int and invert all its bits but then add one
// representation of numbers low level computing usually done with a sign bit, which means positive and negative nums
// That is as far as I can take it from this descrption, more tomorrow when I look i tup and refresh my memory. 

