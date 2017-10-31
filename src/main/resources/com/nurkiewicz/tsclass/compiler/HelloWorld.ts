class Greeter {

    /**
     * Absolute value
     */
    abs(x: number): number {
        if(x < 0)  //Testing inline comments
            return -x;
        else
            return x;
    }

    /*static fib(n: number): number {
        if(n > 1)
            return Greeter.fib(n - 1) + Greeter.fib(n - 2);
        else
            return n;
    }*/
}