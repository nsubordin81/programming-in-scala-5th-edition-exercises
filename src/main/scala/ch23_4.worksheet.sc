/* case study typcalasses JSON serialization

being widely applicable to types that are otherwise unrealted, that is wwhy typeclasses are a good fit here

this could be serialization and deserialization but let's check just the one of serialization at this point to keep it simple


json serialization makes sense to focus on becasue JSON is such a common interchange format. 
very common between javascript clients and backend servers

there are formats in json for strings, numbers, booleans, arrays, and objects. you have to express what you want to 
serialize as one of those basic types. 

so the literal types look a lot like scala literals, but then the object itself is s comma separated set of key value pairs
enclosed in curly braces,, and the key is a string. then you have json arrays as another data structure, 
then you have a null value just in case. 

example of what you can do with json: 

{
    "style": "tennis",
    "size": 10,
    "inStock": true,
    "colors": ["beige", "white", "blue"],
    "humor": null
}


brief aside on why it would be hard to accomplish json serialization as an extensible concept on types 
with subtyping polymorphism. it boils down to there are some types that you can't modify in the standard library 
to have whatever trait you came up with for the serialization process. 

so let's use a typeclass, here is the trait
*/

trait JsonSerializer[T]:
    def serialize(o: T): String

    // invokes on 'this' when this extension method is found
    extension (a: T)
        def toJson: String = serialize(a)

object JsonSerializer:
    given stringSerializer: JsonSerializer[String] with
        def serialize(s: String) = s"\"$s\""
    given intSerializer: JsonSerializer[Int] with
        def serialize(n: Int) = n.toString
    given longSerializer: JsonSerializer[Long] with
        def serialize(n: Long) = n.toString
    given booleanSerializer: JsonSerializer[Boolean] with
        def serialize(b: Boolean) = b.toString
    given listSerializer[T](using JsonSerializer[T]): JsonSerializer[List[T]] with
        def serialize(ts: List[T]) =
            s"[${ts.map(t => t.toJson).mkString(", ")}]"

object ToJsonMethods:
    // invokes on jser when this extension method is found
    extension [T](a: T)(using jser: JsonSerializer[T])
        def toJson: String = jser.serialize(a)

import ToJsonMethods.* 
"tennis".toJson
10.toJson
true.toJson

// TODO follow up and make sure you really grok why we need both extension methonds here and what they mean in the explanation in the book about what is in scope vs. available

// need to add a toJson methdo to any types T where there is a JSON Serializer [T]. 
// they say the extensions method on the trait doesn't do it because it only will work if the serializer is in scope. 
// what is the distinction in scope vs available? well, in scope is either in lexical scope or imported from somewhere
// or part of some hierarchy of types that enclose the scope we are in at the moment. Available meands defined, 
// as in if you were to have access to this companion object, you would have it. but you can't just import a trait, 
// you have to mix it in. so what you can do is wrap an extension method in a singleton object and that will make it 
// easier to bring in.

/* ok so we figured out how to serialize the basic types that json uses, but what if we need to break down more complex 
custom scala objects and serialize them as json? let's look at an example there
*/

case class Address(
    street: String,
    city: String,
    state: String,
    zip: Int)

case class Phone(
    countryCode: Int,
    phoneNumber: Long
)

case class Contact(
    name: String,
    addresses: List[Address],
    phones: List[Phone]
)

case class AddressBook(contacts: List[Contact])

// so here are some custom serializers defined in companion objects, 
// notice how we rename the mextension method to 'asJson' from 'toJson'. 
// this is because we will already be inheriting the toJson extension method from the JsonSerializer trait

// TODO something about the below object definitions was causing the memory usage to spike adn vscode was killing the window. not sure what was happening
// I should look into it

object Address:
    given addressSerializer: JsonSerializer[Address] with
        def serialize(a:Address) = 
            import ToJsonMethods.{toJson as asJson}
            s"""|{
                |   "street": ${a.street.asJson}, 
                |   "city": ${a.city.asJson}, 
                |   "state": ${a.state.asJson}, 
                |   "zip": ${a.zip.asJson} 
                |}""".stripMargin

 object Phone:
    // ok I think the problem that was eating all the memory, was, get this, an extra space before the start of this next line
    // I wasn't able to figure out until I ran Metals:Run Doctor from the command prompt and that revealed a bunch of compiler errors on various files. 
    // one of those errors was that the given instance didn't fall positionally inside of the bounds of the parent object. I think that was the issue because
    // once I uncommented a ducplicated version of the problem below I was able to reproduce the problem. 
    given phoneSerializer: JsonSerializer[Phone] with
        def serialize(p: Phone) = 
            import ToJsonMethods.{toJson as asJson}
              s"""|{
                  |  "countryCode": ${p.countryCode.asJson},
                  |  "phoneNumber": ${p.phoneNumber.asJson}
                  |}""".stripMargin 

// this is showcasing that we can have an extension method toJson added to the List class
// that can serialize json strings from any type that also supports json serialization
// so it involves type classes and context parameters. 
// type classes for the LIst and other types that are given the 'serialize' method
// even though many of them are closed to modificaiton at this point, and 
// extension methods to make it look like the serialize method was efined on the same interface
// within each of these classes and exposed a toJson utility method. some nested typeclass stuff
// going on with list, because list is using the typeclass of serialize to accept another member of the 
// set of types within the json serializer typeclass in order to do its job. 
List(1, 2, 3).toJson