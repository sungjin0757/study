/**
 * IIFE (Immediately Invoked Function Expressions)
 * 즉시 함수 호출을 말한다. 즉시 실행 함수의 기본적인 형태는 다음과 같다.
 * IIFE 를 이해하기 위해서는 먼저, 함수의 선언과 함수의 표현의 차이점에 대해 이해해야 한다.
 */

// 함수 선언식, 일반 프로그래밍 언어에서의 함수 선언 과 비슷한 형식이다.
function printTestLog () {
    console.log(`Test`);
}
printTestLog();

// 함수 표현식, 유연한 자바스크립트 언어의 특징을 활용한 선언 방식
var f = () => console.log(`Test`);
f();

/**
 * 함수 선언은, 미리 자바스크립트의 실행 컨텍스트에 로딩되어 있으므로 언제든지 호출 할 수 있지만
 * 함수 표현식은 인터프리터가 해당 라인에 도달하였을 때만 실행된다. 
 * 작, 함수 선언식은 호이스팅에 영향을 받지만, 함수 표현식은 호이스팅에 영향을 받지 않는다.
 * 따라서 함수 선언식은 코드를 구현한 위치와 관계 없이 자바스크립트의 특징인 호이스팅에 따라 브라우저가 자바스크립트를 해석할 때, 맨 위로 끌어 올려진다.
 * 예를 들어, 아래의 코드를 실행 할 때
 */

logMessage(); // `worked`
// sumNumbers(); // Uncaught TypeError : sumNumbers is not a function

function logMessage() {
    console.log(`log`);
}

var sumNumbers = () => console.log(`${1 + 2}`); 

/**
 * 즉시 호출 함수 표현식의 동작 방법
 * 괄호 쌍이 익명의 함수를 감싸서 함수 표현식으로 표현할 수 있다.
 * 이와 유사하게 이름을 부여하고, 즉시 호출된 함수 표현식으로 생성할 수 있다.
 * showName은 선언과 동시에 실행이 되며, 이름이 있으므로 나중에 호출될 수도 있다.
 * 왜 즉시 실행 함수는 익명함수를 ()로 감싸야만 할까? function(){}과 같이 작성되면, 자바스크립트 코드를 해석하는 파서는 이것을 함수 선언문으로 인지한다.
 * 선언문은 자바스크립트 해석기에게 명령을 지시하고 사라지는 것이기 때문에, "값"으로 남지 않는다. 따라서 "()"와 같이 괄호로 묶어주어, 이것은 "함수 선언문"이 아닌, "함수 표현식"이라는 것을
 * 명시적으로 나타내야 한다.
 */
(showName = function(name) {
    console.log(name || "No Name");
}) ();
showName("Rich");
showName();

/**
 * 즉시 함수 호출은 언제, 왜 사용하는가?
 * IIFE는 주로 전역을 오염시키지 않기 위해 사용한다. 즉, 변수를 전역으로 선언하는 것을 피하기 위해서 주로 사용한다.
 * 전역에 변수를 추가하지 않아도 되기 때문에 코드 충돌없이 구현할 수 있어, 플러그인이나 라이브러리 등을 만들때 많이 사용한다.
 * 또, 함수를 즉시 실행시켜야 하는 경우, 함수를 딱 한번만 실행시켜야 하는 경우 사용된다.
 */

var initText;

(function(number) {
    var textList = ["isOddText", "is Even Text"];
    if(number % 2 == 0){
        initText = textList[1];
    }else {
        initText = textList[1];
    }
}) (5);

console.log(initText);
console.log(textList); // not defined