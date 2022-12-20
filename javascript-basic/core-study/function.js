/**
 * 함수
 * 자바스크립트에서는 함수도 객체이다. 따라서 다른 객체와 마찬가지로 넘기거나 할당할 수 있다.
 * 함수를 호출하지 않고 다른 값과 마찬가지로 참조하기만 할 수 있다는 특징은 자바스크립트를 매우 유연한 언어로 만듭니다.
 * 예를 들어 함수를 변수에 할당하면 다른 이름으로 함수를 호출할 수 있습니다.
 */
function printHelloWorld(){
    console.log("Hello World");
}
var f = printHelloWorld;
f();

var f = {};
f.b = printHelloWorld;
f.b();

var arr = [1, 2, 3];
arr[1] = printHelloWorld;
arr[1]();

/**
 * 함수의 매개변수 해체
 */
function test({subject, verb, object}){
    return `${subject} ${verb} ${object}`;
}

var a = {
    subject : "I",
    verb : "love",
    object : "javaScript"
};
console.log(test(a)); // 함수 호출

/**
 * Default Value
 */
function testDefault(a, b = "default", c = 3){
    console.log(`a = ${a}, b = ${b}, c = ${c}`);
}
testDefault(5, 6, 7); // a = 5, b = 6, c = 7
testDefault(5, 6); // a = 5, b = 6, c = 3
testDefault(5); // a = 5, b = "default", c = 3

/**
 * 화살표 표기법
 * function 이라는 단어와 중괄호 숫자를 줄이려고 고안된 단축 문법. 화살표 함수는 항상 익명 이다.
 */
var f1 = function() {return "Hello";}
console.log(f1());
var f1 = () => "Hello";
console.log(f1());

var f2 = function(name) {return name;}
console.log(f2("Hello"));
var f2 = (name) => `${name}`;
console.log(f2("Hello"));

var f3 = function(a, b) {return a + b;}
console.log(f3(1, 2));
var f3 = (a, b) => a + b;
console.log(f3(1, 2));

var f = {
    name : "Hong",
    backwards : function() {
        const getReverseName = () => {  
            // 화살표 함수에는 일반적인 함수와 중요한 차이가 있다. this 가 다른 변수와 마찬가지로 정적으로 묶인다는 것이다. 바로 위의 this 키워드 예제에서 봤던 예를 아래와 같이 화살표 함수를 사용하면 내부함수안에서 this를 사용할 수 있다.
            let nameBackwards = '';
            for(let i = this.name.length - 1; i >= 0; i--){
                nameBackwards += this.name[i];
            }
            return nameBackwards;
        }
        return `${getReverseName()}`;
    }
}

/**
 * 함수 스코프와 호이스팅
 * ES6 에서 let을 도입하기 전에는 var을 써서 변수를 선언했다.
 * let으로 변수를 선언하면 그 변수는 선언하기 전에는 존재하지 않는다. 반면, var로 선언한 변수는 현재 스코프 안이라면 어디서든 사용할 수 있으며, 심지어 선언하기도 전에 사용할 수 있다.
 */

// x;  // Reference Error Occured!, x is undefined
// let x = 3; // 위의 x로 인해서 도달할 수 없다.

/**
 * var 변수는 에러가 나지 않는 이유는, var로 선언한 변수는 끌어올린다는 뜻의 호이스팅이라는 메커니즘을 따르기 때문이다.
 * 자바스크립트는 함수나 전역 스코프 전체를 살펴보고 var 로 선언한 변수를 맨 위로 끌어올린다. 여기서 중요한 것은 선언만 끌어올려진다는 것이며, 할당을 끌어올려지지 않는다.
 * 즉, 밑의 코드는 
 * var x;
 * x; // undefinde
 * x = 3;
 * x; // 3 
 */
x;
var x = 3;
x;

/**
 * 함수 호이스팅
 * var 로 선언된 변수와 마찬가지로 함수 선언도 스코프 맨 위로 끌러올려진다. 따라서 함수를 선언하기 전에 호출할 수 있다.
 * 단! 변수에 할당된 함수 표현식은 끌어올려지지 않는다. 이들은 변수의 스코프 규칙을 그대로 따른다.
 */
f4(); // abcd
function f4() {
    console.log("abcd");
}

/**
 * 사각지대
 * 사각지대란 let으로 선언하는 변수는 선언하기 전까지 존재하지 않는다는 직관적 개념을 잘 나타내는 표현이다.
 * 스코프 안에서 변수의 사각지대는 변수가 선언되기 전의 코드이다.
 * 변수를 선언하기 전에 사용할 일은 거의 없으므로, 사각지대에 빠질 일도 거의 없지만 ,ES6 이전의 자바스크립트에 익숙한 사람이라면 주의해야할 경우가 하나 있다.
 * typeof 연산자는 변수가 선언 됐는지 알아볼 때 널리 쓰이고, 존재를 확인하는 안전한 방법으로 알려져 있다. 즉, let 키워드가 도입되고 변수의 사각지대가 생기기 전에는
 * 다음과 같은 코드는 항상 안전하며 에러가 발생하지도 않는다.
 */

if(typeof z === "undefined") {
    console.log("x doesn't exist or is undefined");
}else {
    // x를 사용해도 안전한 코드
}

// let z = 5; // Error Occured! 

/**
 * 스트릭트 모드
 * ES5문법에서는 암시적 전역변수라는 것이 생길수 있었다.
 * var 로 변수를 선언하는 것을 잊으면 자바스크립트는 전역 변수를 참조하려 한다고 간주하고, 그런 전역변수가 존재하지 않으면 스스로 만들었다.
 * ex)
 * function foo(){
 *  x = 10;
 * }
 * 
 * console.log(x);
 * 위 예에서, foo 함수내에서 선언하지 않은 변수 x에 값 10을 할당하였따. 이때 변수 x를 찾아야 x에 값을 할당할 수 있기 때문에 자바스크립트 엔진은 변수 x가 어디에서 선언되었는지 스코프 체인을 통해 검색하기 시작한다.
 * 자바스크립트 엔진은 먼저 foo 함수의 스코프에서 변수 x의 선언을 검색한다. foo 함수의 스코프에는 변수 x의 선언이 없으므로 검색에 실패할 것이고, 자바스크립트 엔진은 변수 x를 검색하기 위해
 * foo 함수 컨텍스트의 상위 스코프에서 변수 x의 선언을 검색한다.
 * 전역 스코프에도 변수 x의 선언이 존재하지 않기 때문에 ReferenceError를 throw 할 것 같지만 자바스크립트 엔진은 암묵적으로 전역 객체에 프로퍼티 x를 동적 생성한다.
 * 결국 식별자 x는 전역변수가 된다. 이렇게 전역 변수가 암묵적 전역변수이다.
 * 
 * 이런 이유로 자바스크립트에서는 스트릭트 모드를 도입했다. 스트릭트 모드에서는 암시적 전역 변수를 허용하지 않는다. 스트릭트 모드를 사용하려면 'use strict' 하나만으로 이루어진 행을 코드 맨 앞에 쓰면 된다.
 * 전역 스코프에서 'use strict' 를 사용하면 전체가 스트릭트 모드로 실행되고, 함수 안에서 'use strict'를 사용하면 해당 함수만 스트릭트 모드로 실행한다.
 * 전역 스코프에 스트릭트 모드를 적용하면 스크립트 전체의 동작 방식이 바뀌므로 주의해야 한다. 최신 웹사이트는 대부분 다양한 스크립트를 불러와서 사용하므로 전역스코프에서 스트릭트 모드를 사용하면 불러온 스크립트 전테에
 * 스트릭트 모드가 강제된다.
 * 따라서 일반적으로 전역 스코프에서 스트릭트 모드를 사용하지 않는 편이 좋다.
 * 그렇다고 작성하는 함수 하나하나마다 전부 'use strict' 를 붙이긴 힘들다. 이 경우, 코드 전체를 즉시 실행되는 함수 하나로 감싸주면 된다.
 */


