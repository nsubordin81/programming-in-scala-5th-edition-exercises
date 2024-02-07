// learning about types. this should feel like review, but I don't know the scala side of it and the java side is kinda rusty

// first thing to note, the ranges of all the integral types. I don't think I ever thought to imagine them and remember them in powers of 2

// byte is -2^7 to 2^7 - 1. that is because of twos complement, we learned about in another chapter, the negative range is larger
// because we get an extra bit by having only one zero representation (not positive and negative zero)

// the same is try for the rest. also noting that it is pretty much a progression through 8, 16, 32, 64 but each time the exponent is 
// one smaller so really 7, 15, 31, 63. I'm not sure why it is smaller, maybe because they chose it that way so that
// the next biggest range would start with 2^(whatever power of 2, 3, 4, 5, 6 they wanted)

// char is 16 bit nsigned, and that is 0 to 2^16-1 slots available for unicode characters 65535

// integral have no decimal
// numeric set includes integral but also has decimal types

// even though these are hex types, the REPL converts them to decimal
val hex = 0x5
val hex2 = 0x00FF
val magic = 0xcafebabe
val billion = 1_000_000_000

