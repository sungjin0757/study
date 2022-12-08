/**
 * Symbol
 * ES6 에서 도입된 7번째 데이터 타입으로 변경 불가능한 원시 타입의 값이다.
 * 심벌 값은 다른 값과 중복되지 않는 유일무이한 값이다.
 */

/**
 * Symbol 함수
 * 심벌 값은 Symbol 함수를 호출하여 생성한다. 
 * 이때 생성된 심벌 값은 외부로 노출되지 않아 확인할 수 없으며, 다른 값과 절대 중복되지 않는 유일무이한 값이다.
 */
const mySymbol1 = Symbol();
console.log(typeof mySymbol1);

console.log(mySymbol1); // Symbol()

/**
 * 언뜻 보면 생성자 함수로 객체를 생성하는 것처럼 보이지만 Symbol 함수는 String, Number, Boolean 생성자 함수화는 달리 new 연산자와 함께 호출하지 않는다.
 * new 연산자와 함께 생성자 함수 도는 클래스를 호출하면 객체가 생성 되지만 심벌 값은 변경 불가능한 원시 값이다.
 */

new Symbol(); // TypeError

/**
 * 심벌 값에는 선택적으로 문자열을 인수로 전달할 수 있다. 이 문자열은 생성된 심벌 값에 대한 설명으로 디버깅 용도로만 사용되며, 심벌 값 생성에 어떠한 영향도 주지 않는다. 즉,
 * 심벌 값에 대한 설명이 같더라도 생성된 심벌 값은 유일무이한 값이다.
 */

const mySymbol2 = Symbol("mySymbol");
const mySymbol3 = Symbol("mySymbol");

console.log(mySymbol2 === mySymbol3); // false

console.log(mySymbol2.description); // mySymbol
console.log(mySymbol2.toString()); // Symbol(mySymbol2)
// 심벌 값도 문자열, 숫자, 불리언과 같이 객체처럼 접근하면 암묵적으로 래퍼 객체를 생성한다.
// 심벌 값은 암묵적으로 문자열이나 숫자 타입으로 변환되지 않지만 불리안으로 타입 변환은 된다.

console.log(mySymbol2 + ''); // TypeError
console.log(+mySymbol2); // TypeError
console.log(!!mySymbol2); // true

/**
 * Symbol.for 메서드는 인수로 전달받은 문자열을 키로 사용하여 키와 심벌 값의 쌍들이 저장되어 있는 전역 심벌 레지스트리에서 해당 키와 일치하는 심벌 값을 검색한다.
 */
const s1 = Symbol.for('mySymbol');
const s2 = Symbol.for('mySymbol');

console.log(s1 === s2); // true

// Symbol.keyFor 메서드를 사용하면 전역 심벌 레지스트리에 저장된 심벌 값의 키를 추출할 수 있다.
Symbol.keyFor(s1); // mySymbol

const s3 = Symbol('foo'); // 전역 레지스트리에 등록되지 않는다.
Symbol.keyFor(s3); // undefined

/**
 * enum
 * 객체의 변경을 방지하기 위해 객체를 동결하는 Object.freeze 메서드와 심벌 값을 사용한다.
 */
const direction = Object.freeze({
    UP: Symbol('up'),
    DOWN: Symbol('down'),
    LEFT: Symbol('left'),
    RIGHT: Symbol('right')
});

const myDirection = direction.UP;

if(myDirection === direction.UP) {
    console.log('YOU are going up.');
}

/**
 * 심벌과 프로퍼티 키
 * 객체의 프로퍼티 키는 빈 문자열을 포함하는 모든 문자열 또는 심벌 값으로 만들 수 있으며, 동적으로 생성할 수도 있다.
 * 심벌 값을 프로퍼티 키로 사용하려면 프로퍼티 키로 사용할 심벌 값에 대괄호를 사용해야 한다.
 * 프로퍼티에 접근할 때도 마찬가지로 대괄호를 사용해아한다.
 */
const obj = {
    [Symbol.for('mySymbol1')]: 1
};

obj[Symbol.for('mySymbol1')]; // 1

/**
 * 심벌과 프로퍼티 은닉
 * 심벌 값을 프로퍼티 키로 사용하여 생성한 프로퍼티는 for ...in 문이나 Object.keys, Object.getOwnPropertyNames 메서드로 찾을 수 없다.
 * 이처럼 심벌 값을 프로퍼티 키로 사용하여 프로퍼티를 생성하면 외부에 노출할 필요가 없는 프로퍼티를 은닉할 수 있다.
 */
const obj1 = {
    [Symbol.for('mySymbol1')]: 1
};
for(const key in obj1) {
    console.log(key); // 아무것도 출력되지 않는다.
}
console.log(Object.keys(obj1)); //[]
console.log(Object.getOwnPropertyNames(obj1)); // []

// ES6 에서 도입된 Object.getOwnPropertySymbols 메서드를 사용하면 심벌 값을 프로퍼티 키로 사용하여 생성한 프로퍼티를 찾을 수 있다.
console.log(Object.getOwnPropertySymbols(obj1)); // [Symbol(mySymbol1)]
const symbolKey1 = Object.getOwnPropertySymbols(obj1)[0];
console.log(obj1[symbolKey1]);

/**
 * 심벌과 표준 빌트인 객체 확장
 * 일반적으로 표준 빌트인 객체에 사용자 정의 메서드를 직접 추가하여 확장하는 것은 권장하지 않는다. 표준 빌트인 객체는 읽기 전용으로 사용하는 것이 좋다.
 * 그럼에도 확장이 필요할때 심벌을 사용한다.
 */
Array.prototype[Symbol.for('sum')] = function() {
    return this.reduce((arr, cur) => acc + cur, 0);
}
[1, 2][Symbol.for('sum')](); // 3

/**
 * Well-known Symbol
 * 자바스크립트가 기본 제공하는 빌트인 심벌 값이 있다. 빌트인 심벌 값은 Symbol 함수의 프로퍼티에 할당되어 있다.
 * for ...of 문으로 순회 가능한 빌트인 이터러블은 Well-Known Symbol 인 Symbol.iterator 를 키로 갖는 메서드를 가지며,
 * Symbol.iteratort 메서드를 호출하면 이터레이터를 반환하도록 ECMAScript 사양에 규정되어 있다. 
 * 빌트인 이터러블은 이 규정 즉, 이터레이션 프로토콜을 준수한다.
 */
const iterable = {
    [Symbol.iterator]() {
        let cur = 1;
        const max = 5;
        // Symbol.iterator 메서드는 next 메서드를 소유한 이터레이터를 반환
        return {
            next() {
                return {value: cur++, done: cur > max + 1};
            }
        }
    }
}

for (const num of iterable) {
    console.log(num); // 1 2 3 4 5
}

/**
 * 이때 이터레이션 프로토콜을 준수하기 위해 일반 객체에 추가해야 하는 메서드의 키 Symbol.iterator 는 기존 프로퍼티 키 또는 미래에 추가될 프로퍼티 키와 절대로 중복되지 않을 것이다.
 */