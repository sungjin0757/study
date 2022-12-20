/**
 * Error
 * 에러 처리에 간편하게 사용할 수 있다.
 * 자바스크립트는 에러를 일으킬 때 꼭 객체만이 아니라, 숫자나 문자열 등 어떤 값이든 catch절에 넘길 수 있다. 하지만 Error 인스턴스를 넘기는 것이 가장 편리하다.
 * 대부분의 catch블록은 Error인스턴스를 받을 것이라고 간주하고 만든다. 예를들어 당신이 만든 함수를 동료가 사용한다면, 그 동료는 Error 인스턴스를 받을것으로 생각할 것이다.
 */
function validateEmail(email) {
    return email.match(/@/) ? email : new Error(`Invalid Email : ${email}`);
}

var email = "s";

try{
    var validatedEmail = validateEmail(email);
    if(validatedEmail instanceof Error){
        console.error(`Error : ${validatedEmail.message}`);
    }else {
        console.log(`Valid Email : ${validatedEmail}`);
    }
}catch(err) {
    console.log(`Error : ${err.message}`);
}

function billPay(amount, payee, account) {
    if(amount > account.balance){
        throw new Error("insufficient funds");
    }
    account.transfer(payee, amount);
}
// throw를 호출하면 현재 함수는 즉시 실행을 멈춘다. 따라서 위 예제에서, account.transfer() 함수를 호출하지 않으므로 잔고가 부족한데도 현금을 찾아가는 사고는 발생하지 않는다.

/**
 * 예외처리와 호출 스택
 * 프로그램이 함수를 호출하고, 그 함수는 다른 함수를 호출하고, 호출된 함수는 또 다른 함수를 호출하는 일이 반복된다. 자바스크립트 인터프리터는 이런 과정을 모두 추적하고 있어야 한다.
 * 함수 a가 함수 b를 호출하고,
 * 함수 b가 함수 c를 호출한다면,
 * 함수 c가 실행을 마칠 때, 실행 흐름은 함수 b로 돌아간다. 
 * 그리고 b가 실행을 마칠 때, 실행 흐름은 함수 a 로 돌아간다.
 * 바꿔말해, 함수 c가 실행중일 때는 a와 b는 완료될 수 없다. 이렇게 완료되지 않은 함수가 쌓이는 것을 호출스택이라 부른다.
 * 
 * 에러는 호출 스택 어디에서든 캐치할 수 있다. 어디에서든 이 에러를 캐치하지 않으면 자바스크립트 인터프리터는 프로그램을 멈춘다. 이런것을 처리하지 않은 예외, 캐치하지 않은 예외라 부르며 프로그램이 충돌하는 원인이 된다.
 * 에러가 일어날 수 있는곳은 정말 다양하므로 가능한 에러를 모두 캐치하기는 정말 어렵다.
 * 에러를 캐치하면 호출 스택에서 문제 해결에 유용한 정보를 얻을 수 있다. 예를들어 a가 함수 b를 호출하고 b가 c에서 에러가 일어났다면, 호출 스택은 c에서 일어난 에러를 보고하는데 그치지 않고 b가 c를 호출했으며
 * b는 a에서 호출했다는 것도 함께 알려준다.
 * 대부분의 자바스크립트 환경에서 Error 인스턴스에는 스택을 문자열로 표현한 stack 프로퍼티가 있다. 이 기능은 자바스크립트 표준은 아니지만 대부분의 환경에서 지원한다.
 */
function a() {
    console.log(`a : calling b`);
    b();
    console.log(`a : done`);
}

function b() {
    console.log(`b : calling c`);
    c();
    console.log(`b : done`);
}

function c() {
    console.log(`c : throwing error`);
    throw new Error(`c Error`);
    console.log(`c: done`);
}

function d() {
    console.log(`d : calling c`);
    c();
    console.log(`c : done`);
}

try{
    a();
}catch(err) {
    console.log(err.stack);
}

try{
    d();
}catch(err){
    console.log(err.stack);
}