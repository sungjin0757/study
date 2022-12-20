/**
 * 프로미스 
 * 프로미스는 콜백의 단점을 해결하려는 시도 속에서 만들어졌다. 프로미스는 번거롭게 느껴질 수 있지만, 일반적으로 안전하고 관리하기 쉬운 코드를 만들 수 있게 된다.
 * 프로미스가 콜백을 대체하는 것은 아니다. 사실 프로미스에서도 콜백을 사용한다. 프로미스는 콜백을 예측 가능한 패턴으로 사용할 수 있게 하며, 프로미스 없이 콜백만
 * 사용했을 때 나타날 수 있는 엉뚱한 현상이나 찾기 힘든 버그를 상당수 해결한다.
 * 프로미스의 기본 개념은 간단하다. 프로미스 기반 비동기적 함수를 호출하면 그 함수는 promise 인스턴스를 반환한다. 프로미스는 성공하거나 실패하거나
 * 단 두가지 뿐이다. 또한, 성공이든 실패든 단 한번만 일어난다.
 * 프로미스가 성공하거나 실패하면 그 프로미스를 결정됐다고 한다.
 * 프로미스는 객체이므로 어디든 전달할 수있다는 점도 콜백에 비해 간편한 장점이다.
 * 비동기적 처리를 여기서 하지 않고 다른 함수에서 처리하게 하고 싶다면 프로미스를 넘기기만 하면 된다.
 */

const { resolve } = require("path");
const { EventEmitter } = require("stream");

// 프로미스 만들기
function countdown(seconds) {
    return new Promise((resolve, reject) => {
        for(let i = seconds; i >= 0; i --){
            setTimeout(() => {
                if(i > 0)
                    console.log(i);
                else
                    resolve(console.log("Go!"));
            }), (seconds - i) * 1000;
        }
    })
}
countdown(5).then(
    () => {
        console.log("count down completed Successfully");
    }, (err) => {
        console.log("countdown experienced an error : " + err.message);
    }
)

countdown(5).then(
    () => {
        console.log("count down completed Successfully");
    }
).catch((err) => {
    console.log("countdown experienced an error : " + err.message);
})

/**
 * 이벤트
 * 이벤트는 자바스크립트에서 자주 사용된다. 이벤트의 개념은 간단하다. 이벤트가 일어나면 이벤트 발생을 담당하는 개체에서 이벤트가 일어났음을 알린다.
 * 필요한 이벤트는 모두 주시할 수 있다.
 * 노드에는 이미 이벤트를 지원하는 모듈 EventEmitter가 내장돼 있다. 이 모듈을 써서 countdown 함수를 개선해보자.
 * EventEmitter는 countdown 같은 함수와 함께 사용해도 되지만, 원래는 클래스와 함께 사용하도록 설계되었다.
 * 그러니 먼저 countdown 함수를 Countdown 클래스로 바꿔보자.
 */

var eventEmitter = require(`events`).EventEmitter;

class Countdown extends EventEmitter {
    constructor(seconds, supersitious) {
        super();
        this.seconds = seconds;
        this.supersitious = !!supersitious;
    }

    go() {
        const countdown = this;
        return new Promise((resolve, reject) => {
            for(let i = countdown.seconds; i >= 0; i--){
                setTimeout(() => {
                    if(countdown.supersitious &&i === 13)
                        return reject(new Error("error happened"));
                    countdown.emit(`tick`, i);
                    if(i === 0)
                        resolve();
                }, (countdown.seconds - i) * 1000);
            }
        })
    }
}

/**
 * EventEmitter를 상속하는 클래스는 이벤트를 발생시킬 수 있다. 실제로 카운트다운을 시작하고 프로미스를 반환하는 부분은 go 메소드이다.
 * go 메소드 안에서 가장 먼저 한 일은 countdown에 this를 할당한 것이다.
 * 카운트다운이 얼마나 남았는지 알기 위해서는 this값을 알아야 하고, 13인지 아닌지 역시 콜백안에서 알아야 한다.
 * this는 특별한 변수이고 콜백 안에서는 값이 달라진다.
 * 따라서 this의 현재 값을 다른 변수에 저장해야 프로미스 안에서 쓸 수 있다.
 * 가장 중요한 부분은 countdown.emit('tick', i) 이다.
 * 이 부분에서 tick 이벤트를 발생시키고, 필요하다면 프로그램의 다른 부분에서 이 이벤트를 주시할 수 있다. 위 countdown 클래스는 아래와 같이 사용할 수 있다.
 */

var c = new Countdown(5);

c.on(`tick`, (i) => {
    if(i > 0)
        console.log(i);
});

c.go().then(() => {
    console.log(`Go!`);
}).catch((err) => {
    console.log(err.message);
});

// EventEmitter의 on 메소드가 이벤트를 주시하는 부분이다. tick이 0이 아니면 출력한 다음 카운트다운을 시작하는 go를 호출한다. 카운트 다운이 끝나면 go!를 호출한다.

/**
 * 프로미스 체인
 * 프로미스에는 체인으로 연결할 수 있다는 장점이 있다. 즉, 프로미스가 완료되면 다른 프로미스를 반환하는 함수를 즉시 호출할 수 있다.
 * launch 함수를 만들어 카운트 나운이 끝나면 실행되게 해보자.
 */

function launch() {
    return new Promise((resolve, reject) => {
        console.log("AAA");
        setTimeout(() => {
            resolve("BBB");
        }, 2 * 1000);
    })
}

var c = new Countdown(5).on(`tick`, i => console.log(i));
c.go()
.then(launch)
.then((msg) => {
    console.log(msg);
})
.catch((err) => {
    console.error("Error Happened");
});


/**
 * Promise.all
 * 여러 개의 비동기 처리를 모두 병렬 처리할 때 사용 한다.
 */
const requestData1 = () => {
    new Promise(resolve => setTimeout(() => resolve(1), 3000));
}
const requestData2 = () => {
    new Promise(resolve => setTimeout(() => resolve(2), 2000));
}
const requestData3 = () => {
    new Promise(resolve => setTimeout(() => resolve(3), 1000));
}

// 세 개의 비동기 처리 메서드를 순차적으로 처리
// 약 6 초 정도의 시간이 걸림
const res = [];
requestData1().then(data => {
    res.push(data);
    return requestData2();
}).then(data => {
    res.push(data);
    return requestData3();
}).then(data => {
    res.push(data);
    console.log(res); // [1, 2, 3]
});

// 약 3 초가 걸림
Promise.all([requestData1(), requestData2(), requestData3()])
    .then(console.log) // [1, 2, 3]
    .catch(console.error);

/**
  * Promise.all 메서드는 프로미스를 오소로 갖는 배열 등의 이터러블을 인수로 전달 받는다. 그리고 전달받은 모든 프로미스가 fulfilled 상태가 되면
  * 모든 처리 결과를 배열에 저장해 새로운 프로미스를 반환한다.
  *  이 때 첫번째 프로미스가 처리순서가 늦어 지더라고 resolve 한 처리 결과부터 차례대로 배열에 저장해 그 배열을 resolve 하는 새로운 프로미스를 반환한다. 즉, 처리 순서가 보장 된다.
  * Promise.all 메서드는 인수로 전달받은 배열의 프로미스가 하나라도 rejected 상태가 되면 나머지 프로미스가 fulfilled 상태가 되는 것을 기다리지 않고 즉시 종료한다.
  */
Promise.all([
    new Promise((_, reject) => setTimeout(() => reject(new Error('Error 1'), 3000))),
    new Promise((_, reject) => setTimeout(() => reject(new Error('Error 2'), 2000))),
    new Promise((_, reject) => setTimeout(() => reject(new Error('Error 3'), 1000)))
])
    .then(console.log)
    .catch(console.error); // Error: Error 3

/**
 * Promise.all 메서드는 인수로 전달받은 이터러블의 요소가 프로미스가 아닌 경우 Promise.resolve 메서드를 통해 프로미스로 래핑한다.
 */
Promise.all([
    1, // -> Promise.resolve(1) 
    2, // -> Promise.resolve(2)
    3 // -> Promise.resolve(3)
]);

/**
 * Promise.race
 * Promise.race 메서드는 Promise.all 메서드와 동일하게 프로미스를 요소로 갖는 배열 등의 이터러블을 인수로 전달 받는다.
 * 가장 먼저 fulfilled 상태가 된 프로미스의 처리 결과를 resolve하는 새로운 프로미스를 반환한다.
 */
Promise.race([
    new Promise(resolve => setTimeout(() => resolve(1), 3000)),
    new Promise(resolve => setTimeout(() => resolve(2), 2000)),
    new Promise(resolve => setTimeout(() => resolve(3), 1000))
])
    .then(console.log) // 3
    .catch(console.error);

// 프로미스가 rejected 상태가 되면 Promise.all 메서드와 동일하게 처리된다. 즉, Promise.race 메서드에 전달된 프로미스가 하나라도 rejected 상태가 되면 에러를 reject 하는 새로운 프로미스를 즉시 반환한다.
Promise.race([
    new Promise((_, reject) => setTimeout(() => reject(new Error('Error 1'), 3000))),
    new Promise((_, reject) => setTimeout(() => reject(new Error('Error 2'), 2000))),
    new Promise((_, reject) => setTimeout(() => reject(new Error('Error 3'), 1000)))
])
    .then(console.log)
    .catch(console.error); // Error: Error 3

/**
 * Promise.allSettled
 * 프로미스를 요소로 갖는 배열 등의 이터러블을 인수로 전달받는다. 그리고 전달 받은 프로미스가 모두 settled 상태(비동기 처리가 수행된 상태, 즉 fulfilled 또는 rejected 상태) 가 되면 처리 결과를 배열로 반환한다.
 */
Promise.allSettled([
    new Promise(resolve => setTimeout(() => resolve(1), 2000)),
    new Promise((_, reject) => setTimeout(() => reject(new Error('Error!'), 1000)))
])
    .then(console.log);
/*
[
    {status: "fulfilled", value: 1},
    {status: "rejected", reson: Error: Error! at <anonymous>:3:54}
]
*/

/**
 * 마이크로 태스크 큐
 * 프로미스의 후속 처리 메서드의 콜백 함수는 태스크 큐가 아니라 마이크로태스크 큐에 저장됨
 * 마이크로태스크 큐는 태스크 큐보다 우선순위가 높음
 * 즉, 이벤트 루프는 콜 스택이 비면 먼저 마이크로태스크 큐에서 대기하고 있는 함수를 가져와 실행
 */