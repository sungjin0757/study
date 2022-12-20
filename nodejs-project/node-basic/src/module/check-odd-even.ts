export default class OddEvenChecker{
    private readonly ODD: string;
    private readonly EVEN: string;

    constructor() {
        this.ODD = "홀수";
        this.EVEN = "작수";
    }

    checkOddOrEven(num: number): string {
        if (num % 2) {
            return this.ODD; 
        }
        return this.EVEN;
    } 
}