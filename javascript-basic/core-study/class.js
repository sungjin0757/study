/**
 * 클래스와 인스턴스 생성
 * ES6 이전에 자바스크립트에서 클래스를 만드는 건 직관적이지도 않고 무척 번거로운 일이었다. ES6에서는 클래스를 만드는 간편한 새 문법을 도입했다.
 */
class Car{
    // 오버로딩이 불가능 한듯.
    // constructor() {
        
    // }
    constructor(make, model) {
        this.make = make;
        this.model = model;
        // this.userGears = ['P', 'N', 'R', 'D'];
        this._userGears = ['P', 'N', 'R', 'D']; // 접근하면 안 되는 프로퍼티 이름 앞에 밑줄을 붙이는, 소위 '가짜 접근 제한'을 사용했다.
        // 물론, 완벽한 방지법은 아니지만 직접 접근하면 안되는 프로퍼티라는 것을 인식시킬수 있게 하는 방편이라고 봐야 합니다.
        // this.userGear = this.userGears[0];
        this._userGear = this._userGears[0];
        this.nextVin = Car.getNextVin();
    }
    /**
     * 접근 제한자가 따로 존재하지 않기 때문에, 잘못된 기어를 선택하는 실수를 완전히 방지하지는 못한다.
     * 자바스크립트에는 이런 메커니즘이 없고, 이는 언어의 문제로 자주 비판을 받는다.
     * 실수로 프로 퍼티를 고치지 않도록 어느정도 막을 수 있다.
     */
    shift(gear){
        if(this.userGears.indexOf(gear) < 0)
            throw new Error(`Invalid Gear : ${gear}`);
        this._userGear = gear;
    }

    /**
     * 정적 메서드
     * this는 인스턴스가 아니라 클래스 자체에 묶인다. 하지만 일반적으로 정적 메소드에는 this 대신 클래스 이름을 사용하는 것이 좋은 습관이다.
     */
    static getNextVin() {
        return Car.nextVin++;
    }

    static isSimilar(car1, car2) {
        return car1.make === car2.make;
    }

    static isSame(car1, car2) {
        return car1.nextVin === car2.nextVin;
    }

    get userGear(){
        return this._userGear;
    }
    set userGear(value){
        if(!this._userGears.includes(value))
            throw new Error(`Invalid Gear : ${gear}`);
        this._userGear = gear;
    }

    toString() {
        return `make = ${this.make}, model = ${this.model}, userGear = ${this._userGear}, nextVin = ${this.nextVin}`
    }
}

/**
 * 상속
 */
class DescendantsCar extends Car{
    constructor(make, model) {
        super(make, model);
        console.log("Createad Descendants Car!!");
    }

    printTestLog() {
        console.log("Test Log!!");
    }
}

/**
 * 다중상속 구현, Mixin
 * 다중 상속에는 충돌의 위험이 있다. 예를 들어서 각 super 클래스의 메소드가 겹친다면, 서브 클래스에서는 어떤 메서드를 사용해야할 지 모호해질 수 도 있기 때문이다.
 * 자바스크립트에서 다중 상속이 필요한 문제에 대해 해답으로 내놓은 개념이 믹스인(mixin) ㅣ다.
 * 믹스인이란, 기능을 필요한 만큼 섞어놓은 것이다. 자바스크립트는 느슨한 타입을 사용하고 대단히 관대한 언어이므로, 그 어떤 기능이라도 언제든 어떤 객체든 추가할 수 있다.
 */
class InsurancePolicy {
}

function makeInsurable(o) {
    o.addInsurancePolicy = (p) => {
        this.insurancePolicy = o;
    }
    o.getInsurancePolicy = () => this.insurancePolicy;
}

var car1 = new Car();
console.log(car1 instanceof Car); // true
/**
 * 클래스는 함수다.
 * ES6에서 class 키워드를 도입하기 전까지, 클래스를 만든다는 것은 곧 클래스 생성자로 사용할 함수를 만든다는 의미였다.
 * class 문법이 훨씬 더 직관적이고 단순하긴 하지만, 사실 class는 단축 문법일 뿐이며 클래스 자체가 바뀐 것은 아니다.
 * 위의 Car 클래스는 다음과 같은 함수일 뿐이다.
 */

function CarFuncVer(make, model){
    this.make = make;
    this.model = model;
    this._userGears = ['P', 'N', 'R', 'D'];
    this._userGear = this.userGears[0];
}

/**
 * 일급객체
 * 특정 언어에서 일급객체라하면, 컴퓨터 프로그래밍 언어에서 일반적으로 다른 객체들에 적용 가능한 연산을 모두 지원하는 객체를 가리킨다.
 * 다음과 같은 조건을 만족할 때, 일급객체라 할 수 있다.
 * 1. 변수에 담을 수 있다.
 * 2. 파라미터로 전달할 수 있다.
 * 3. 반환값으로 사용할 수 있다.
 * 자바스크립트에서는 이런 특성 때문에 숫자와 문자처럼 함수를 변수에 저장하거나, 파라미터로 전달하고, 함수의 리턴값으로도 사용할 수가 있는 것이다.
 */

/**
 * 일급함수
 * 함수를 일급 객체로 취급하는 것을 일급함수라 한다.
 * 함수 또한 객체로 표현하기 때문에, 자바스크립트의 함수를 일급객체라고도 말하고, 일급 함수라고도 말한다.
 */

/**
 * 프로토타입
 * 클래스 인스턴스에서 사용할수 있는 메소드라고 하면 그건 프로토타입 메소드를 말하는 것이다.
 * 예를들어, 앞서 Car의 인스턴스에서 사용할 수 있는 shift 메소드는 프로토타입 메소드이다. 프로토타입 메소드는 Car.prototype.shift 처럼 표기할 때가 많다.
 * Array의 forEach를 Array.prototype.forEach라고 쓰는 것과 마찬가지다.
 * 모든 함수에는 prototype이라는 특별한 프로퍼티가 있다. 일반적인 함수에서는 프로토타입을 사용할 일이 없지만, 객체 생성자로 동작하는 함수에서는 프로토타입이 대단히 중요하다.
 * 객체 생성자, 즉, 클래스는 Car처럼 항상 첫 글자를 대문자료 표기한다. 자바스크립트에서 이런 표기법을 요구하는 것은 아니지만, 일반적인 함수 이름이 대문자로 시작하거나 객체 생성자가
 * 소문자로 시작한다면 이를 경고하는 린트 프로그램이 많다.
 * 
 * 함수의 prototype 프로퍼티가 중요해지는 시점은 new키워드로 새 인스턴스를 만들었을 때이다. new 키워드로 만든 새 객체는 생성자의 prototype 프로퍼티에 접근할 수 있다.
 * 객체 인스턴스는 생성자의 prototype 프로퍼티를 __proto__ 프로퍼티에 저장한다.
 * 프로토타입에서 중요한 것은 동적 디스패치라는 메커니즘이다. 여기서 디스패치는 메소드 호출과 같은 의미이다. 객체의 프로퍼티나 메소드에 접근하려할 때, 그런 프로퍼티나 메소드가 존재하지 않으면
 * 자바스크립트는 객체의 프로토타입에서 해당 프로퍼티나 메소드를 찾는다.
 * 클래스의 인스턴스는 모두 같은 프로토타입을 공유하므로 프로토타입에 프로퍼티나 메소드가 있다면 해당 클래스의 인스턴스는 모두 그 프로퍼티나 메소드에 접근할 수 있다.
 * 클래스의 프로토타입에서 데이터 프로퍼티를 수정하는 것은 일반적으로 권장하지 않는다. 모든 인스턴스가 그 프로퍼티의 값을 공유하기는 하지만, 인스턴스 중 하나에 그런 이름의 프로퍼티가 있다면
 * 해당 인스턴스는 프로토타입에 있는 값이 아니라, 인스턴스에 있는 값을 사용한다. 이는 혼란과 버그를 초래할 수 있다. 인스턴스에 초깃값이 필요하다면 생성자에서 만드는 편이 낫다.
 */

var car1 = new Car();
var car2 = new Car();

console.log(car1.shift === Car.prototype.shift);  // true
console.log(car1.shift == car2.shift);  // true

car1.shift = (gear) => {
    this.userGear = gear.toUpperCase();
};  // 프로퍼티가 바뀐 것이 아닌 객체에 있는 값을 바꿈.
console.log(car1.shift === Car.prototype.shift);  // false
console.log(car1.shitf === car2.shift)  // false

Car.nextVin = 0;

var car1 = new Car("Hyundai", "A");
var car2 = new Car("Hyundai", "B");

console.log(car1.nextVin);  // 0
console.log(car2.nextVin);  // 1

console.log(Car.isSimilar(car1, car2));  // true
console.log(Car.isSame(car1, car2));  // false

var car1 = new DescendantsCar("Hyundai", "C");
car1.printTestLog();
console.log(car1.nextVin); // 2
console.log(car1 instanceof Car); // true
console.log(car1 instanceof DescendantsCar); // true

console.log(car1.toString());

makeInsurable(Car.prototype);
var car1 = new Car();
car1.addInsurancePolicy(new InsurancePolicy());
