class Greeter {
    greeting: string = "abc";

    constructor(message: string) {
    }

    static main(args: string[]): void {
    }

    greet(): string {
        return "Hello, " + this.greeting;
    }

    greetLen(): number {
        return this.greeting.length;
    }
}