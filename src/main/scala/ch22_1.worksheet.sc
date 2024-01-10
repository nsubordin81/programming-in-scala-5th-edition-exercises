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

