export default class TimerTest {
    private timeout1 = setTimeout(() => {
        console.log('1.5초 후 실행');
    }, 1500);
    

    private interval = setInterval(() => {
        console.log('1초 마다 실행');
    }, 1000);

    private timeout2 = setTimeout(() => {
        console.log("실행 되지 않아 ");
    }, 3000)


    test1(): void {
        setTimeout(() => {
            clearTimeout(this.timeout2);
            clearInterval(this.interval);
        })
    }
}