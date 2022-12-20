/**
 * 정적 유효범위
 * 자바스크립트에서는 코드가 적힌 순간 변수의 유효 범위가 정해진다. 이것을 정적 유효범위, 또는 렉시컬 스코프라고 부른다.
 */

var name = "Hong"; // 전역
function print() {
    console.log(name);
}

function main() {
    name = "A"; // 전역 변수인 name을 바꿈.
    print(); // "A"
}
main(); 

var name = "Hong";
function main1() {
    var name = "A"; 
    print(); // "Hong", main 안에서 변수가 새로 생성되었기 때문에, print() 함수는 전역 변수를 통해서 함수를 진행한다.
}
main1();

function print1() {
    console.log(`global`);
}

function main2() {
    function print1() {
        console.log(`local`);
    }
    print1();
}

main2(); // local, 자바스크립트는 동적이지만, 유효범위는 코드가 작성되는 순간 정해지는 정적인 특성을 가진다.

/**
 * 블록레벨 스코프 & 함수레벨 스코프
 * 대부분의 프로그래밍 언어는 블록레벨 스코프를 따르지만, 자바스크립트는 함수 레벨 스코프를 따른다.
 * 함수 레벨 스코프 : 함수 내에서 선언된 변수는 함수 내에서만 유효하며, 함수 외부에서는 참조할 수 없다. 즉, 함수 내부에서 선언한 변수는 지역변수이며, 함수 외부에서 선언한 변수는 모두 전역 변수이다.
 * 블록 레벨 스코프 : 모든 코드 블록(함수, if문, for문, while문, try~catch문 등..) 내에서 선언된 변수는 코드 블록 내에서만 유효하며, 코드 블록 외부에서는 참조할 수 없다. 즉, 코드 블록 내부에서 선언한 변수는 지역 변수이다.
 * 
 * let 키워드로 선언된 변수는 블록 레벨 스코프를 따른다.
 */
var foo = 123; // 전역 변수
console.log(foo); // 123
{
    var foo = 456; // 전역 변수
}
console.log(foo); // 456

let a = 123;  // 전역 변수
{
    let a = 123;  // 지역 변수
    let b = 456;  // 지역 변수
}

console.log(a); // 123
console.log(b); // ReferenceError : b is not defined