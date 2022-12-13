/**
 * 이터러블
 * ES6 에서 도입된 이터레이션 프로토콜은 순회 가능한 데이터 컬렉션을 만들기 위해 ECMAScript 사양에 정의하여 미리 약속한 규칙이다.
 * ES6 이전의 순회 가능한 데이터 컬렉션, 즉 배열, 문자열, 유사 배열 객체, DOM 컬렉션 등은 통일된 규약 없이 각자 나름의 구조를 가지고 for 문, for ...in 문, forEach 메서드등
 * 다양한 방법으로 순회할 수 있었다.
 * ES6 에서는 순회 가능한 데이터 컬렉션을 이터레이션 프로토콜을 준수하는 이터러블로 통일하여 for ...of 문, 스프레드 문법, 배열 디스트럭처링 할당의 대상으로 사용할 수 있도록 일원화 했다.
 * 이터레이션 프로토콜에는 이터러블 프로토콜과 이터레이터 프로토콜이 있다.
 */

/**
 * 이터러블 프로토콜
 * Symbol.iterator 를 프로퍼티 키로 사용한 메서드를 직접 구현하거나 프로토타입 체인을 통해 상속받은 Symbol.iterator 메서드를 호출하면 이터레이터 프로토콜을 준수한 이터레이터를 반환한다.
 * 이러한 규약을 이터러블 프로토콜이라 하며, 이터러블 프로토콜을 준수한 객체를 이터러블이라 한다. 이터러블은 for ...of 문으로 순회할 수 있으며 스프레드 문법과 배열 티스트럭처링 할당의 대상으로 사용할 수 있다.
 */

/**
 * 이터레이터 프로토콜
 * 이터러블의 Symbol.iteratort 메서드를 호출하면 이터레이터 프로토콜을 준수한 이터레이터를 반환한다.
 * 이터레이터는 next 메서드를 소유하며 next 메서드를 호출하면 이터러블을 순회하며 value 와 done 프로퍼티를 갖는 이터레이터 리절트 객체를 반환한다. 이러한 규약을 이터레이터 프로토콜이라 하며,
 * 이터레이터 프로토콜을 준수한 객체를 이터레이터라 한다. 이터레이터는 이터러블의 요소를 탐색하기 위한 포인터 역할을 한다.
 */

/**
 * 이터러블 
 * 이터러블 프로토콜을 준수한 객체를 이터러블이라 한다. 즉, 이터러블은 Symbol.iteratort 를 프로퍼티 키로 사용한 메서드를 직접 구현하거나 프로토타입체인을 통해 상속받은 객체를 말한다.
 */

// 이터러블인지 확인하는 함수
const isIterable = v => v !== null && typeof v[Symbol.iterator] === 'function';

isIterable([]); // true
isIterable(''); // true
isIterable(new Map()); // true
isIterable(new Set()); // true
isIterable({}); // false

// 배열은 Array.prototype 의 Symbol.iterator 메서드를 상속받는 이터러블이다. 이터러블은 for ... of 문으로 순회할 수 있으며, 스프레드 문법과 배열 디스트럭처링 할당의 대상으로 사용할 수 있다.
const array = [1, 2, 3];
console.log(Symbol.iterator in array); // true

for(const num of array) {
    console.log(num);
}
// 이터러블인 배열은 스프레드 문법의 대상으로 사용할 수 있다.
console.log([... array]);

const [a, ...rest] = array;
console.log(a, rest); // 1, [2, 3]

// 일반 객체는 이터러블 프로토콜을 준수한 이터러블이 아니다.
const obj = {
    a: 1,
    b: 2
}

console.lof(Symbol.iterator in obj); // false

for(const item of obj) {  // TypeError
    console.log(item);
}

const [x, y] = obj; // TypeError

/**
 * 이터레이터
 * 이터러블의 Symbol.iterator 메서드를 호출하면 이터레이터 프로토콜을 준수한 이터레이터를 반환한다.
 * 이터러블의 Symbol.iterator 메서드가 반환한 이터레이터는 next 메소드를 갖는다.
 */

const array2 = [1, 2, 3];
const iterator = array2[Symbol.iterator]();
console.log('next' in iterator); // true

/**
 * 이터레이터의 next 메서드는 이터러블의 각 요소를 순회하기 위한 포인터의 역할을 한다. 즉, next 메서드를 호출하면 이터러블을 순차적으로 한 단계씩 순회하며 순회 결과를 나타내는 이터레이터
 * 리절트 객체를 반환한다.
 */
const array3 = [1, 2, 3];
const iterator2 = array3[Symbol.iterator]();

console.log(iterator2.next()); // {value:1, done: false}
console.log(iterator2.next()); // {value:2, done: false}
console.log(iterator2.next()); // {value:3, done: false}
console.log(iterator2.next()); // {value:undefined, done: true}

// 이터레이터의 next 메서드가 반환하는 이터레이터 리절트 객체의 value 프로퍼티는 현재 순회 중인 이터러블의 값을 나타내며 done 프로퍼티는 이터러블의 순회 완료 여부를 나타낸다.

/**
 * 빌트인 이터러블
 * 자바스크립트는 이터레이션 프로토콜을 준수한 객체인 빌트인 이터러블을 제공한다.
 * Array, String, Map, Set, TypedArray, arguments
 */

/**
 * for ... of 문
 * for ... of 문은 이터러블을 순회하면서 이터러블의 요소를 변수에 할당한다.
 * for ... in 문은 객체의 프로토타입 체인 상에 존재하는 모든 프로토타입의 프로퍼티 중에서 프로퍼티 어트리뷰트 [[Enumerable]] 의 값이 true 인 프로퍼티를 순회하며 열거한다.
 * for ... of 문은 내부적으로 이터레이터의 next 메서드를 호출하여 이터러블을 순회하며 next 메서드가 반환한 이터레이터 리절트 객체의 value 프로퍼티 값을 for ... of 문의 변수에 할당한다.
 * 그리고 이터레이터 리절트 객체의 done 프로퍼티 값이 false 이면 이터러블의 순회를 계속하고 true이면 순회를 중단한다.
 */
for (const item of [1, 2, 3]) {
    console.log(item);
}

const iterable = [1, 2, 3];
const iterator3 = iterable[Symbol.iterator]();

for(;;) {
    const res = iterator3.next();

    if(res.done) {
        break;
    }

    const item = res.item;
    console.log(item); // 1 2 3
}

/**
 * 이터러블과 유사 배열 객체
 * 유사 배열 객체는 마치 배열처럼 인덱스로 프로퍼티 값에 접근할 수 있고 length 프로퍼티를 갖는 객체를 말한다. 유사 배열 객체는 length 프로퍼티를 갖기 때문에 for 문으로 순회할 수 있고, 인덱스를 나타내는 숫자 형식의 문자열을
 * 프로퍼티 키로 가지므로 마치 배열처럼 인덱스로 프로퍼티 값에 접근할 수 있다.
 */
const arrayLike = {
    0: 1,
    1: 2,
    2: 3,
    length: 3
}

for(let i = 0; i < arrayLike.length; i++) {
    console.log(arrayLike[i]); // 1 2 3
}
// 유사 배열 객체는 이터러블이 아닌 일반 객체다. 따라서 유사 배열 객체에는 Symbol.iterator 메서드가 없기 때문에 for ... of 문으로 순회할 수 없다.
for (const item of arrayLike) {
    console.log(item); // 1 2 3
} // TypeError
// 단, arguments, NodeList, HTMLCollection 은 유사 배열 객체이면서 이터러블이다.
// Array.from 메서드는 유사 배열 객체 또는 이터러블을 인수로 전달 받아 배열로 변환하여 반환한다.
const arrayLike2 = {
    0: 1,
    1: 2,
    2: 3,
    length: 3
};

const arr = Array.from(arrayLike2);
console.log(arr);

/**
 * 이터레이션 프로토콜의 필요성
 * for ... of 문, 스프레드 문법, 배열 디스트럭처링 할당 등은 Array, String, Map, Set, TypedArray, arguments 등과 같이 다양한 데이터 소스를 사용할 수 있다.
 * ES 6 에서는 순회 가능한 데이터 컬렉션을 이터레이션 프로토콜을 준수하는 이터러블로 통일하여 for ... of 문, 스프레드 문법, 배열 디스트럭처링 할당의 대상으로 사용할 수 있도록 일원화 했다.
 * 이터러블은 for ... of 문, 스프레드 문법, 배열 디스트럭처링 할당과 같은 데이터 소비자에 의해 사용되므로 데이터 공급자의 역할을 한다고 할 수 있다.
 * 이터러블을 지원하는 데이터 소비자는 내부에서 Symbol.iterator 메서드를 호출해 이터레이터를 생성하고, next 메서드를 호출하여 이터러블을 순회하며ㅕ 이터레이터 리절트 객체를 반환한다.
 * 이터레이션 프로토콜은 다양한 데이터 공급자가 하나의 순회 방식을 갖도록 규정하여 데이터 소비자가 효율적으로 다양한 데이터 공급자를 사용할 수 있도록 데이터 소비자와 데이터 공극자를 연결하는 인터페이스 역할을 한다.
 */

/**
 * 사용자 정의 이터러블
 */
const fibonacci = {
    [Symbol.iterator]() {
        let [pre, cur] = [0, 1];
        const max = 10;

        return {
            next() {
                [pre, cur] = [cur, pre + cur];
                return {value: cur, done: cur >= max}
            }
        };
    }
};

const arr1 = [...fibonacci];
console.log(arr1); // [1, 2, 3, 5, 8]

const [first, second, ...rest1] = fibonacci;
console.log(first, second, rest1); // 1 2 [3, 5, 8]

/**
 * 이터러블을 생성하는 함수
 */
const fibonacciFunc = function(max) {
    let [pre, cur] = [0, 1];

    return {
        [Symbol.iterator]() {
            return {
                next() {
                    [pre, cur] = [cur, pre + cur];
                    return {value: cur, done: cur >= max};
                }
            }
        }
    };
};

/**
 * 이터러블이면서 이터레이터인 객체를 생성하는 함수
 * 이터레이터를 생성하려면 이터러블의 Symbol.iterator 메서드를 호출해야 한다.
 */
const iterable2 = fibonacciFunc(5);
const iterator4 = iterable2[Symbol.iterator]();

console.log(iterator4.next()); // {value: 1, done: false}
console.log(iterator4.next()); // {value: 2, done: false}
console.log(iterator4.next()); // {value: 3, done: false}
console.log(iterator4.next()); // {value: 5, done: true}

/**
 * 이터러블이면서 이터레이터인 객체를 생성하면 Symbol.iterator 메서드를 호출하지 않아도 된다.
 * 다음 객체는 Symbol.iterator 메서드와 next 메서드를 소유한 이터러블이면서 이터레이터다.
 */
const fibonacciFunc2 = function(max) {
    let [pre, cur] = [0, 1];

    return {
        [Symbol.iterator]() {return this;},
        next() {
            [pre, cur] = [cur, pre + cur];
            return {value: cur, done: cur >= max};;
        }
    };
};

let iter = fibonacciFunc2(10);

for(const num of iter) {
    console.log(num); // 1 2 3 5 8
}

console.log(iter.next()); //{value: 1, done: false}
console.log(iter.next()); //{value: 2, done: false}
console.log(iter.next()); //{value: 3, done: false}
console.log(iter.next()); //{value: 5, done: false}
console.log(iter.next()); //{value: 8, done: false}
console.log(iter.next()); //{value: 13, done: true}
