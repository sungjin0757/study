/**
 * forEach
 * 오직 Array 객체에서만 사용가능한 메소드이다. 
 * ES6 부터는 Map, Set 등에서도 지원됨
 * 배열의 요소들을 반복하여 작업을 수행할 수 있다.
 * forEach 구문의 인자로 callback 함수를 등록할 수 있고, 배열의 각 요소들이 반복될 때 이 callback 함수가 호출된다.
 */
var items = ["item1", "item2", "item3"];
items.forEach(item => {
    console.log(item);
});

/**
 * fon ...in
 * 객체의 속성들을 반복하여 작업을 수행할 수 있다. 모든 객체에서 사용가능하다. for in 구문은 key 값에 접근할 수 있지만, value 값에 접근하는 방법은 제공하지 않는다.
 * 자바스크립트에서 객체 속성들은 내부적으로 사용하는 숨겨진 속성들을 가지고 있다.
 * 그 중 하나가 Enumerable이며, for in 구문은 이 값이 true로 셋팅되어 있는 속성들만 반복할 수 있다. 이러한 속성들을 열거형 속성이라고 부르며,
 * 객체의 모든 내장 메소드를 비롯해 각종 내장 프로퍼티 같은 비열겨형 속성은 반복되지 않는다.
 */
var obj = {
    a : 1,
    b : 2,
    c : 3
};

for(var o in obj) {
    console.log(`key = ${o}, value = ${obj[o]}`);
}

/**
 * for ...of
 * for of 반복문은 ES6에 추가된 새로운 컬렉션 전용 반복 구문이다. for of 구문을 사용하기 위해선 iterator 속성을 가지고 있어야만 한다.
 */
var items = ["item1", "item2", "item3"];

for(var item of items){
    console.log(item);
}