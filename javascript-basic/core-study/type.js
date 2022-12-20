/**
* Object Simple Example
 */
var obj = {};
obj.color = "blue";
obj["abc"] = 3;
console.log(obj);

var obj = {
    color : "black",
    "abc" : 1
}
console.log(obj);
console.log(obj["color"]);
console.log(obj["abc"]);

/**
 * Primitve Object
 * Let's Know Primitive Type, Object Type
 */
var str = "Hello World";
// 원시 타입인 숫자 (number), 문자 (string), 불리언 (boolean) 에도 대응되는 객체 타입들이 존재하며 각각 Number, String, Boolean 과 같이 객체처럼 사용 가능하다. 
console.log(str.length); // 12

var objStr = new String(str);
console.log(objStr.length);

console.log(typeof str); // string
console.log(typeof objStr); // object

/**
 * Data Type Converting
 */
var numStr = "33.3";
var num = Number(numStr); 
console.log(num);
console.log(typeof num);

var numStr = "H";
var num = Number(numStr);
console.log(num);  // Nan 데이터 타입 변환이 불가능하기 때문에.
console.log(typeof num);

var numStr = "25.5";
var num = parseInt(numStr); // 숫자로 판단할 수 있는 부분까지만 변환하고, 그 뒤에 있는 문자열은 무시
console.log(num);
console.log(typeof num);

// 문자열로 변환
var arr = [1, true, "Hello"];
console.log(arr.toString());

/**
 * Decimal Caution
 */
var a = 0.1;
var b = 0.2;
var sum = a+ b;
console.log(sum); 
// 기대했던 값인 0.3이 나오지 않을 것이다. (부동 소수점에 대한 이해 필요) Simply, 컴퓨터는 2진법을 사용한다. 즉, 10진법을 2진법으로 변환하는 과정중에 일부가 무한 소수가 되어 버린다. 컴퓨터 메모리에 의한 한계로 무한 소수를 다 담지는 못하고 유한 소수로 저장하게 된다.

sum = sum.toFixed(2); // toFixed 메소드는 입력받은 숫자 만큼, 자리수를 반올림하여 String 으로 변환하여 준다. (0 부터 20까지 입력 가능)
console.log(sum);
console.log(Number(sum));

var a = 0.1;
var b = 0.2;
var sum = a+ b;
sum = Math.round(sum * 10) / 10; // Math.round() 는 반올림해주는 함수 이다. 가장 가까운 정수값을 반환한다.

/**
 * 해체 할당
 * ES6에서 새로 도입한 해체할당은 매우 유용한 기능입니다. 이 기능은 객체나 배열을 변수로 '해체'할 수 있습니다.
 */
var obj = {
    b : 2,
    c : 3,
    d : 4
}
// var {a, b, c} = obj; // 해체 할당 
// console.log(a); // undefined, "a" 프로퍼티가 없기 때문
// console.log(b);
// console.log(c);
// console.log(d); // d는 정의되지 않음. Error Occured
// 즉, 객체를 할당할 때는 반드시 변수 이름과 객체의 프로퍼티 이름이 일치해야 합니다.

// var b, c, d;
// // {a, b, c} = obj; // Error Occured!
// ({b, c, d}) = obj;
// console.log(d);
// console.log(b);
// console.log(c);

var arr = [1, 2, 3];
var [x, y] = arr;
console.log(x); // 1
console.log(y); // 2

var arr = [1, 2, 3, 4, 5];
var [x, y, ...rest] = arr;
console.log(x); // 1
console.log(y); // 2
console.log(rest); // [3, 4, 5]

/**
 * Template String
 * ES6 이전에는 변수나 상수를 문자열 안에 쓰는 방법은 문자열 병합 뿐이었다.
 * ES6 에서는 문자열 템플릿이라는 기능을 도입했다. 백틱을 사용하면 된다.
 */
var name = "Hong";
var message1 = "my name is " + name;
console.log(message1);
var message2 = `my name is ${name}`;
console.log(message2);
console.log(message1 === message2); // true