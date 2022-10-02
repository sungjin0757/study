/**
 * 이터레이터
 * 이터레이터는 지금 어디 있는지 파악할 수 있도록 돕는다는 면에서 일종의 책갈피와 비슷한 개념이다. 배열은 이터러블 객체의 좋은 예이다.
 */

var items = [];
for(let i = 0; i < 10; i++) {
    items.push(`item${i}`);
}

var it = items.values();

for(let i = 0; i < items.length; i++){
    console.log(it.next());
}

/**
 * 이터레이터 프로토콜
 * 이터레이터는 그 자체로 크게 쓸모가 있다기 보다는, 더 쓸모 있는 동작이 가능해지도록 한다는데 의미가 있다.
 * 이터레이터 프로토콜은 모든 객체를 이터러블 객체로 바꿀 수 있다.
 */
class Log {
    constructor() {
        this.messages = [];
    }

    add(message) {
        this.messages.push({message, timestamp: Date.now()});
    }

    /**
     * 이터레이션 프로토콜은 클래스에 심볼 메소드 Symbol.iterator가 있고, 이 메소드가 이터레이터처럼 동작하는 객체, 즉 value와 done 프로퍼티가 있는 객체를 반환하는 next 메소드를
     * 가진 객체를 반환한다면,
     * 그 클래스의 인스턴스는 이터러블 객체라는 뜻이다.
     */
    [Symbol.iterator]() {
        return this.messages.values();
    }
}

var log = new Log();
log.add("aaa");
log.add("bbb");
log.add("ccc");

for(let entry of log) {
    console.log(`${entry.message}, ${entry.timestamp}`);
}