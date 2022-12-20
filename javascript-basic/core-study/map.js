/**
 * Map
 * ES6 이전에는 키와 값을 연결하려면 객체를 사용해야 했다. 하지만 이런 목적으로 사용하면 여러가지 단점이 생긴다.
 * 프로토타입 체인 때문에 의도하지 않은 연결이 생길 수 있다.
 * 객체 안에 연결된 키와 값이 몇개나 되는지 쉽게 알아낼 수 있는 방법이 없다.
 * 키는 반드시 문자열이나 심볼이어야 하므로 객체를 키로 써서 값과 연결할 수 없다.
 * 객체는 프로퍼티 순서를 전혀 보장하지 않는다.
 */
function printValue(value, key) {
    console.log(`key = ${key.name}, value = ${value}`);
}

var user1 = {name : "a"};
var user2 = {name : "b"};
var user3 = {name : "c"};
var user4 = {name : "d"};

// set, set은 이미 있는 키에다가 하면 value가 교체 된다.
var userRoles = new Map();
userRoles.set(user1, "User");
userRoles.set(user2, "User");
userRoles.set(user3, "Admin");
userRoles.set(user4, "User");
userRoles.forEach(printValue);

// Map 의 set() 메소드는 체인으로 연결이 가능하여, 위 코드를 아래와 같이 간략히 표현할 수 있다.
var userRoles = new Map();
userRoles.set(user1, "User").set(user2, "User").set(user3, "Admin").set(user4, "User");
userRoles.forEach(printValue);

// 생성자에 배열의 배열을 넘기는 형태로 써도 된다.
var userRoles = new Map([[user1, "User"], [user2, "User"], [user3, "Admin"], [user4, "User"]]);
userRoles.forEach(printValue);

// get
console.log(userRoles.get(user1)); // "User"
console.log(userRoles.get(1)); // Undefined

// has, key가 있는지 확인
console.log(userRoles.has(user1)); // true
console.log(userRoles.has(3)); // false

// size
console.log(userRoles.size); // 4


// keys, values, entries(key, value)
for(let key of userRoles.keys()) {
    console.log(`${key.name}`);
}

for(let value of userRoles.values()) {
    console.log(`${value}`);
}

for(let [k, v] of userRoles.entries()) {
    printValue(v, k);
}

// entries() 메소드는 맵의 기본 이터레이터이다. 
for(let [k, v] of userRoles) {
    printValue(v, k);
}

// map의 요소를 지울 때는 delete() 메소드를 사용한다.
console.log(userRoles.delete(user1)); // true
console.log(userRoles.size === 3); // true

// map의 요소를 모두 지울 때는 clear()를 사용
userRoles.clear()
console.log(userRoles.size === 0); // true


/**
 * map 생성자 함수는 이터러블을 인수로 전달 받아 Map 객체를 생성한다. 이 때 인수로 전달되는 이터러블은 키와 값의 쌍으로 이루어진 요소로 구성되어야 한다.
 */
const map = new Map(['key1', 'value1'], ['key2', 'value2']);
const {size} = new Map(['key1', 'value1'], ['key2', 'value2']);
console.log(size); // 2

/**
 * map.prototype.keys : Map 객체에서 요소키를 값으로 갖는 이터러블이면서 동시에 이터레이터인 객체를 반환한다.
 * map.protorype.values : Map 객체에서 요소값을 값으로 갖는 이터러블이면서 동시에 이터레이터인 객체를 반환한다.
 * map.prototype.entries : Map 객체에서 요소키와 요소값을 값으로 갖는 이터러블이면서 동시에 이터레이터인 객체를 반환한다.
 */