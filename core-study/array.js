/**
 * push/pop & unshift/shift
 * push 와 pop은 각각 배열의 끝에 요소를 추가하거나 제거한다.
 * push 와 unshift는 새 요소를 추가해서 늘어난 길이를 반환하고, pop 과 shift는 제거된 요소를 반환한다.
 */

var arr = ["b", "c", "d"];
arr.push("e");  // 배열의 젤 뒤에 추가
console.log(arr.pop()); // 배열의 젤 뒤에 요소 제거
arr.unshift("a") // 배열의 젤 앞에 추가
console.log(arr);
arr.shift(); // 배열의 젤 앞에 요소 제거
console.log(arr);

/**
 * concat
 * concat 메소드는 배열의 끝에 여러 요소를 추가한 사본을 반환한다.
 * concat에 배열을 넘기면 이 메소드는 배열을 분해해서 원래 배열에 추가한 사본을 반환한다.
 * concat에 제공받은 배열을 한 번만 분해 한다. 배열안에 있는 배열을 다시 분해하지는 않는다.
 */
var arr = [1, 2, 3];
console.log(arr.concat(4, 5, 6)) // [1, 2, 3, 4, 5, 6]
console.log(arr.concat([4, 5, 6])) // [1, 2, 3, 4, 5, 6]
console.log(arr.concat([4, [5, 6]])) // [1, 2, 3, 4, [5, 6]]

/**
 * slice
 * 배열의 일부만 가져올 때는 slice 메소드를 사용한다. slice 메소드는 매개변수 두 개를 받는다. 첫 번째 매개변수는 어디서부터 가져올지를, 두 번째 매개변수는 어디까지 가져올지를 작성한다.
 * 두번째 매개변수를 생략하면 배열의 마지막까지 반환한다. 음수 인덱스를 쓰면 배열의 끝에서부터 요소를 샌다.
 */
var arr = [1, 2, 3, 4, 5];
console.log(arr.slice(3)); // [4, 5]
console.log(arr.slice(2, 4)); // [3, 4]
console.log(arr.slice(-2)); // [4, 5]
console.log(arr.slice(-2, -1)); // [4]

/**
 * splice
 * 배열을 자유롭게 수정할 수 있다. 척 번째 매개변수는 수정을 시작할 인덱스이고, 두 번째 매개변수는 제거할 요소 숫자이다. 아무 요소도 제거하지 않을 때는 0을 넘긴다. 나머지 매개변수는 배열에 추가될 요소이다.
 */
var arr = [1, 5, 7];
arr.splice(1, 0, 2, 3, 4); // arr은 이제 [1, 2, 3, 4, 5, 7] dlek.
console.log(arr);
arr.splice(5, 0, 6); // [1, 2, 3, 4, 5, 6, 7]
console.log(arr);
arr.splice(1, 2); // [1, 4, 5, 6, 7]
console.log(arr);
arr.splice(2, 1, 'a', 'b'); [1, 4, 'a', 'b', 6, 7];
console.log(arr);

/**
 * copyWithin
 * ES6 에서 도입한 새 메서드.
 * 이 메서드는 배열 요소를 복사해서 다른 위치에 붙여널고, 기존의 요소를 덮어쓴다. 첫 번째 매개변수는 복사한 요소를 붙여 널을 위치이고, 두 번째 매개변수는 복사를 시작할 위치이고,
 * 세 번째 매개변수는 복사를 끝낼 위치이다. slice와 마찬가지로, 음수 인덱스를 사용하면 배열의 끝에서부터 샌다.
 */
var arr = [1, 2, 3, 4];
arr.copyWithin(1, 2); // [1, 3, 3, 4]
console.log(arr);
arr.copyWithin(2, 0, 2); // [1, 3, 1, 3]
console.log(arr);

/**
 * fill
 * 이 메서드는 정해진 값으로 배열을 채운다. 크기를 지정해서 배열을 생성하는 Array 생성자와 잘 어울린다. 배열의 일부만 채우려 할 때는
 * 시작 인덱스와 끝 인덱스를 지정하면 된다. 음수 인덱스도 사용할 수 있다.
 */
var arr = new Array(5).fill(1);
console.log(arr);
arr.fill("a"); // "a"로 다 채워진다.
console.log(arr);
arr.fill("b", 1); // 1번 부터 "b"로 채워진다.
console.log(arr);
arr.fill("c", 2, 4); // 2 ~ 3 번이 "c"로 채워진다.
console.log(arr);

/**
 * reverse
 */
var arr = [1, 2, 3, 4, 5];
arr.reverse();
console.log(arr);

/**
 * sort
 */
var arr = [5, 4, 3, 2, 1];
arr.sort();
console.log(arr);
arr.sort((a, b) => a < b);
console.log(arr);

/**
 * indexOf & lastIndexOf
 * indexOf는 찾고자 하는 것과 정확히 일치(===) 하는 첫 번째 요소의 인덱스를 반환한다.
 * indexOf의 짝인 lastIndexOf는 배열의 끝에서부터 검색한다. 배열의 일부분만 검색하려면 시작 인덱스를 지정할 수 있다. indexOf 와 lastIndexOf는 일치하는 것을 찾지 못하면 -1을 반환한다.
 */
var a = {name : "hong"};
var arr = [1, 2, 3, a, 4];
console.log(arr.indexOf(1)); // 0
console.log(arr.indexOf(a)); // 3
console.log(arr.lastIndexOf(4)); // 0

/**
 * findIndex
 * 일치하는 것을 찾지 못했을 때, -1을 반환한다는 점에서 indexOf와 비슷하지만, 보조 함수를 써서 검색조건을 지정할 수 있으므로 indexOf보다 더 다양한 상황에서 활용할 수 있다.
 * 하지만 findIndex는 검색을 시작할 인덱스를 지정할 수 없고, 뒤에서부터 찾는 findLastIndex 같은 짝도 없다.
 */
var arr = [1, 2, {name : "hong"}];
console.log(arr.findIndex(o => o === 1)); // 0
console.log(arr.findIndex(o => o === 2)); // 1
console.log(arr.findIndex(o => o.name === "hong")); // 2

/**
 * find
 * indexOf 와 findIndex는 조건에 맞는 요소의 인덱스를 찾을 때 알맞지만, 조건에 맞는 요소의 인덱스가 아니라 요소 자체를 원할 때는 find를 사용한다. find는 findIndex와 마찬가지로 검색조건을 함수로 전달할 수 있다.
 */
var arr = [{id : 3, name : "hong"}, {name : "bruce"}];
console.log(arr.find(o => o.name === "hong")) // 객체 자체를 변환
console.log(arr.find(o => o.id === 7)); // undefined

/**
 * includes
 * includes() 메소드는 배열이 특정 요소를 포함하고 있는지 판별한다. (대소문자를 구분하며, 배열뿐 아니라 String 에서도 사용가능하다.)
 */
var arr = [1, 2, 3];
console.log(arr.includes(1)); // true
console.log(arr.includes(4)); // false

/**
 * map
 * map은 배열 요소를 변형한다. 일정한 형식의 배열을 다른 형식으로 바꿔야 한다면 map을 사용하면 된다. 사본을 반환한다.
 */
var names = ["bruce", "hong"];
var bestNames = names.map(x => `${x} Best`);
console.log(bestNames);
var items = ["naver", "google"];
var vals = [100, 99];
var itemsWithVal = items.map((x, i) => ({item : x, val : vals[i]}));  // 괄호로 감꽈줘야함. 화살표 표기법에 따른 오류가 발생할 수 있음.
console.log(itemsWithVal);

/**
 * filter
 * 필요한 것들만 남길 목적으로 만들어졌다. 사본을 반환하며, 새 배열에는 필요한 요소만 남는다.
 */
var arr = [1, 4, 8, 10, 20];
var newArr = arr.filter(x => x >= 8);
console.log(newArr);

/**
 * reduce
 * 배열의 각 요소에 대해 주어진 리듀서 함수를 실행하고, 하나의 결과값을 반환한다.
 * map이 배열의 각 요소를 변현한다면, reduce는 배열 자체를 변형한다.
 * reduce라는 이름은 이 메소드가 보통 배열을 값 하나로 줄이기 때문에 지어진 이름이다.
 * 하지만, reduce가 반환하는 값 하나는 객체일 수도 있고, 다른 배열일 수도 있다.
 * 리듀서 함수는 네 개의 인자를 가진다.
 * 1. 누산기 (accumulator) : 누산기는 콜백의 반환값을 누적한다. 콜백의 이전 반환값. 또는 콜백의 첫 번째 호출이면서 initialValue를 제공한 경우에는 initialValude의 값이다.
 * 2. 현재 값(cur) : 처리할 현재 요소
 * 3. 현재 인덱스(index, optional) : 처리할 현재 요소의 인덱스. initialValue를 제공한 경우 0, 아니면 1부터 시작.
 * 4. 원본 배열(src, optional) : reduce()를 호출한 배열
 * initialValue(optional) : callback의 최초 호출에서 첫 번째 인수에 제공하는 값. 초깃값을 제공하지 않으면 배열의 첫 번째 요소를 사용한다. 빈 배열에서 초기값 없이 reduce()를 호출하면
 * 오류가 발생한다. 리듀서 함수의 반환 값은 누산기에 할당되고, 누산기는 순회중 유지되므로 결국 최종 결과는 하나의 값이 된다.
 */

var res = [0, 1, 2, 3, 4].reduce((accumulator, currentValue, currentIndex, array) => {
    console.log(`accumulator : ${accumulator}`);
    console.log(`currentIndex : ${currentIndex}`);
    console.log(`currentValue : ${currentValue}`);
    console.log(`array : ${array}`);
    console.log("                              ");
    return accumulator + currentValue;
}, 5);

console.log(`res : ${res}`);

/**
 * 배열의 문자열 병합
 * 배열의 문자열 요소들을 몇몇 구분자로 합치려 할 때가 많다. Array.prototype.join은 매개변수로 구분자 하나를 받고, 요소들을 하나로 합친 문자열을 반환한다.
 * 이 매개변수가 생략됐을때의 기본값은 쉼표이며, 문자열 요소를 합칠 때 정의되지 않은 요소, 삭제된 요소, null, undefined는 모두 빈 문자열로 취급한다.
 */
var arr = [1, null, "hello", "world", true, undefined];
delete arr[3];
console.log(arr.join());
console.log(arr.join(''));
console.log(arr.join('--'));

/**
 * 배열과 유사배열 그리고 Array.from 구문
 */
// var nodes = document.querySelectorAll('div'); [div, div ... ] []로 감싸져 있지만, 배열이 아닌 것들을 유사 배열이라고 부른다.

var similar = {
    0 : 'a',
    1 : 'b',
    2 : 'c',
    length : 3
}  // 이와 같은 것들을 유사배열이라고 부른다 similar[0], similar[1] 이런식으로 접근이 가능하기 때문이다. 즉, 배열과 똑같은 메소드를 사용하지 못한다.
// 이럴 때, 메소드를 빌려쓰는 방법이 있다. 배열 프로토타입에서 call이나 apply를 이용하여 forEach 메소드를 빌려오는 것이다.
Array.prototype.forEach.call(similar, e => {
    console.log(e);
});

var arr = Array.from(similar);
console.log(arr);

/**
 * Array.every
 * every 메소드는 배열안의 모든 요소가 주어진 판별 함수를 통과하는지 테스트한다.
 * 빈배열에서 호출하면 무조건 true를 반환함
 */
var arr = [1, 30, 39, 29, 10, 13];
console.log(arr.every(v => v < 40));

/**
 * 배열조작 예제.
 * 두 배열의 교집합을 구하시오.
 */
var arr1 = [1, 2, 3, 4, 5];
var arr2 = [4, 5, 6, 7, 8, 9, 10];
var arr3 = arr1.filter(x => arr2.indexOf(x) != -1);
console.log(arr3);

/**
 * 배열조작 예제
 * 두 배열의 합집합을 구하시오. (중복되는 값이 있어서는 안됨)
 */
var arr1 = [1, 2, 3, 4, 5];
var arr2 = [4, 5, 6, 7, 8, 9, 10];
function sum(arr1, arr2) {
    var tmpArr = arr1.concat(arr2);
    let sumArr = tmpArr.reduce((accumulator, value, idx) => {
        if(accumulator.includes(value)){
            return accumulator;
        }
        accumulator[idx] = value;
        return accumulator;
    }, []);
    var res = sumArr.filter((v) => v);
    console.log(res);
    console.log(sumArr);
}
sum(arr1, arr2);

/**
 * Object.keys
 * Object.keys는 객체에서 나열가능한 문자열 프로퍼티를 배열로 반환. 객체의 프로퍼티 키를 배열로 가져와야할 때는 Object.keys가 편리하다.
 * ex) 객체에서 x로 시작하는 프로퍼티를 모두 가져와야 하는 경우
 */
const o = {
    apple : 1,
    grape : 2,
    banane : 3,
    x : 5
}
Object.keys(o).filter(x => x.match(/^x/))
                .forEach(xx => console.log(`${xx} : ${o[xx]}`));