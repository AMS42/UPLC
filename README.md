# UPLC
Unnamed Programming Language Compiler

Syntax: {Braces and Semicolons}

Built-In Types: {
    none: null type
    bool: true | false
    int: {x : x is an integer}
    float: {x : x is a decimal point number}
    str: {x : x is 0 or more characters enclosed in double quotes}
    list: [objects...]
}

Type Casts: {
    object -> bool: {
        bool: {
            true -> true
            false -> false
        }
        int: {
            n > 0 -> true
            n <= 0 -> false
        }
        float: {
            n > 0 -> true
            n < 0 -> false
        }
        str: {
            length of string > 0 -> true
            length of string == 0 -> false
        }
        list: {
            length of list > 0 -> true
            length of list == 0 -> false
        }
    }
    object -> int: {
        bool: {
            true -> 1
            false -> 0
        }
        int: { x -> x }
        float: { x -> floor(x) }
        str: { s -> TypeError }
        list: { length of list }
    }
    object -> float: {
        bool: {
            true -> 1.0
            false -> 0.0
        }
        int: { x -> x.0 }
        float: { x -> x }
        str: { s -> TypeError }
        list: { length of list }
    }
    object -> str: {
        bool: { b -> "b" }
        int: { x -> "x" }
        float: { x -> "x" }
        str: { s -> s }
        list: { l -> "l" }
    }
    object -> list: {
        bool: { b -> [b] }
        int: { x -> [x] }
        float: { x -> [x] }
        str: { s -> [ch for ch in s] }
        list: { l -> l }
    }
}

Unary Operators: {
    not(object) -> bool
        # object is cast to bool
}

Binary Operators: {
    and(object, object) -> object
        # returns the second object if the first is true; otherwise, returns the first object
    or(object, object) -> object
        # returns the first object if true; otherwise, returns the second object
    ==(object, object) -> bool
    <(object, object) -> bool
    <=(object, object) -> bool
    >(object, object) -> bool
    >=(object, object) -> bool
    +(object, object) -> object
        +(bool, bool) -> int
        +(bool, int) -> int
        +(bool, float) -> float
        +(list, list) -> list
            # set union of lists
        +(object, str) -> TypeError
        +(object, list) -> list
            # casts object to list
    -(object, object) -> object
        -(bool, bool) -> int
        -(bool, int) -> int
        -(bool, float) -> float
        -(list, list) -> list
            # set difference of lists
        -(object, str) -> TypeError
        -(object, list) -> list
            # casts object and list to int
    *(object, object) -> object
        *(bool, bool) -> int
        *(bool, int) -> int
        *(bool, float) -> float
        *(str, int) -> str
            # repeats the str int times
        *(str, object) -> TypeError
        *(list, int) -> list
            # repeats the list int times
        *(list, object) -> TypeError
    /(object, object) -> object
        /(bool, bool) -> float
        /(bool, int) -> float
        /(bool, float) -> float
        /(object, str) -> TypeError
        /(object, list) -> TypeError
    //(object, object) -> object
        # /(object, object) cast as int
    **(object, object) -> object
        **(bool, bool) -> int
        **(bool, int) -> int
        **(bool, float) -> float
        **(str, object) -> TypeError
        **(list, object) -> TypeError
}

Control (Conditions and Loops): {
    x = 42;
    if x < 0 {
        print("x is negative");
    } else if x == 0 {
        print("x is 0");
    } else {
        print("x is positive");
    };
    
    while x > 0 {
        print(x);
        x = x//2;
    };
    
    do x times {
        print(x);
    };
}

Built-In Functions: {
    print(object...) -> none {
        STD:OUT: space separated object
    }
    
    typeof(object) -> str {
        Returns the type of an object as a string
    }
}

Built-In Exceptions: {
    DivideByZeroError: raised when division by 0 happens
    
}