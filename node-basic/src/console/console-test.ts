export default class ConsoleTest {
    private str: string = "abc";
    private num: number = 1;
    private bool: boolean = true;
    private obj: Object = {
        outside: {
            inside: {
                key: 'value'
            }
        }
    };

    run(): void {
        console.time("전체 시간");
        console.log("평범한 로그입니다. 쉼표로 구분해 여러 값을 찍을 수 있습니다.");
        console.log(this.str, this.num, this.bool);
        console.error('에러 메시지는 console.error 에 담아주세요');

        console.table([{name: 'hong', age: 25}, {name: 'sung', age: 23242}]);

        console.dir(this.obj, {colors: false, depth: 2});
        console.dir(this.obj, {colors: true, depth: 1});

        console.time('시간 측정');
        for(let i = 0 ; i < 1000; i++) {

        }
        console.timeEnd('시간 측정');

        const b = () => {
            console.trace('에러 위치 추적');
        }
        const a = () => {
            b();
        }

        a();

        console.timeEnd('전체 시간');
    }
}