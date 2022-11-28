export default class TimerTest {
    private const timeout1 = setTimeout(() => {
        console.log('1.5초 후 실행');
    }, 1500);
    

    private const interval = setInterval(() => {
        console.log('1초 마다 실행');
    }, 1000);

    private const timeout2 = setTimeout(() => {
        console.log("실행 되지 않아 ");
    }, 3000)


    test1(): void {
        setTimeout(() => {
            clearTimeout(this.timeout2);
            clearInterval(this.interval);
        })
    }
}