/**
 * this 키워드
 * 함수 바디 안에는 특별한 읽기 전용 값인 this가 있다. 메소드를 호출하면 this는 호출한 메소드를 소유하는 객체가 된다.
 */
var a = {
    name : "name",
    speak(){
        return `my name is ${this.name}`
    }
}
console.log(a.speak());  // my name is name
/**
 * a.speak() 를 호출하면, this는 a에 묶입니다. 따라서 a.speak(); 을 호출하면 my name is name 을 리턴받는다.
 * this는 함수를 어떻게 선언했느냐가 아니라 어떻게 호출했느냐에 따라 달라진다는 것을 이해해야 한다.
 * 즉, this 가 a에 묶인 이유는 speak 가 함수 a의 프로퍼티여서가 아니라, a 에서 speak를 호출했기 때문 입니다.
 */

var test = a.speak;
console.log(test()); //my name is undefined, 함수를 이렇게 호출하면 자바스크립트는 이 함수가 어디에 속하는지 알 수 없으므로 this는 undefined에 묶입니다.

var a = {
    name : "name",
    backwards : function(){
        function getReverseName(){
            let nameBackwards = '';
            for(let i = this.name.length - 1; i >= 0; i--){
                nameBackwards += this.name[i];  // 중첩 함수에서의 this 사용
            }
            return nameBackwards;
        }
        return `${getReverseName()}`;
    }
}
/**
 * 위의 예의 getReverseName 은 의도한 대로 동작하지 않는다. a.backwords() 를 호출하는 시점에서 자바스크립트는 this를 의도한대로 a에 연결하지만,
 * backwards 안에서 getReverseName을 호출하면, this는 a가 아닌 가른 것에 묶인다. 이런 문제를 해결하기 위해서 널리 사용하는 방법은 아래와 같이 다른 변수에 this를 할당하는 것이다.
 */
 var a = {
    name : "name",
    backwards : function(){
        const self = this;
        function getReverseName(){
            let nameBackwards = '';
            for(let i = self.name.length - 1; i >= 0; i--){
                nameBackwards += self.name[i];  // 중첩 함수에서의 this 사용
            }
            return nameBackwards;
        }
        return `${getReverseName()}`;
    }
}
console.log(a.backwards()); // 정상 동작

/**
 * this를 사용하는 방법에는 일반적인 방법 외에 call 과 apply, bind를 사용하여, 함수를 어디서 어떻게 호출했느냐와 관계없이 this가 무엇인지 지정할 수 있다.
 */

// call 메소드, 모든 함수에서 사용할 수 있으며, this를 특정 값으로 지정할 수 있다.
var bruce = {name : "bruce"};
var hong = {name : "hong"};

function greet() {
    return `hello, ${this.name}`
}

console.log(greet()); // "hello, undefined"
console.log(greet.call(bruce)); // "hello, bruce"
console.log(greet.call(hong)) // "hello, hong"

// 함수를 호출하면 call을 사용하고 this 로 사용할 객체를 넘기면 해당 함수가 주어진 객체의 메소드인 것처럼 사용할 수 있다.
// call의 첫번 째 매개변수는 this로 사용할 값이고, 매개변수가 더 있으면 그 매개 변수는 호출하는 함수로 전달된다.
function update(val1, val2) {
    this.val1 = val1;
    this.val2 = val2;
}

update.call(bruce, 1, 2);
console.log(bruce.val1); // 1
console.log(bruce.val2); // 2

// apply 메소드, 함수 매개변수를 처리하는 방법을 제외하면 call과 완전히 같다. call은 일반적인 함수와 마찬가지로 매개변수를 직접 받지만, apply는 매개변수를 배열로 받는다.
update.apply(bruce, [1, 2]);
console.log(bruce.val1); // 1
console.log(bruce.val2); // 2

// apply 는 배열 요소를 함수 매개변수로 사용해야 할 때 유용하다.
var arr = [2, 3, -5, 15];
console.log(Math.min.apply(null, arr)); // -5 null을 넣은 이유는 this와 관계없이 함수가 동작하기 때문이다.
console.log(Math.max.apply(null, arr)); // 15

console.log(Math.min(...arr));

/**
 * bind 메소드
 * bind를 사용하면 함수의 this 값을 영구히 바꿀 수 있다. update 메소드를 이리저리 옮기면서도 호출할 때 this 값은 항상 brudce가 되게끔, call 이나 apply
 * 다른 bind와 함께 호출하더라고 this 값이 bruce가 되도록 하려면 bind를 사용한다.
 * bind는 함수의 동작을 영구적으로 바꾸므로 찾기 어려운 버그의 원인이 될 수 있다. 
 * 함수를 여기저기서 call이나 apply로 호출해야 하는데, this가 값이 그에 맞춰 바뀌어야 하는 경우라면, bind를 사용하면 문제가 된다.
 */
var updateBruce = update.bind(bruce);
updateBruce(4, 5);
console.log(bruce.val1); // 4
console.log(bruce.val2); // 5

updateBruce.call(hong, 30, 40); // bruce는 이제 {name : "bruce", val1 : 30, val2 : 40} 이 되었다. hong은 bind로 인해 적용되지 않는다.
console.log(bruce.val1); // 30
console.log(bruce.val2); // 40

var updateBruce2 = update.bind(bruce, 2); // bind 에 매개변수를 넘기면 항상 그 매개변수를 받으면서 호출되는 새 함수를 만드는 효과가 있다. 
updateBruce2(3);
console.log(bruce.val1); // 2
console.log(bruce.val2); // 3