/**
 * Set
 * weakSet 도 사용가능하다.
 */

function isSameCount(set, count) {
    return set.size === count;
}
var users = new Set();
users.add("user");
users.add("user");

console.log(isSameCount(users, 1));  // true

// set 생성자 함수는 이터러블을 인수로 전달받아 Set 객체를 생성한다. 이때 이터러블의 중복된 값은 Set 객체에 요소로 저장되지 않는다.
const set1 = new Set([1,2, 3, 3]);
console.log(set1); // Set(3) {1, 2, 3}
const set2 = new Set('Indigo');
console.log(set2); // Set(4) {"i", "n", "d", "i", "g", "o"}

// 중복을 허용하지 않는 set 객체의 특성을 활용하여 배열에서 중복된 요소를 제거할 수 있다.
// 배열의 중복요소 제거
const uniq = array => array.filter((v, i, self) => self.indexOf(v) === i);
console.log(uniq([2, 1, 2, 3, 4, 3, 4])); // [2, 1, 3, 4]

// set 의 중복요소 제거
const uniq2 = array => [...new Set(array)];
console.log(uniq2([2, 1, 2, 3, 4, 3, 4])); // [2, 1, 3, 4]

/**
 * 요소 개수 확인
 * Set 객체의 요소 개수를 확인할 때는 Set.prototype.size 프로퍼티를 사용한다.
 */
const {size} = new Set([1, 2, 3, 3]);
console.log(size); // 3

// size 프로퍼티는 setter 함수 없이 getter 함수만 존재하는 접근자 프로퍼티다. 따라서 size 프로퍼티에 숫자를 할당하여 Set 객체의 요소 개수를 변경할 수 없다.
const set = new Set();
set.size = 10; // 무시

// 요소 추가
set.add(1);

// 일치 비교자 연산을 사용하면 NaN 과 Nan 을 다르다고 평가하지만 set 은 같다고 판단한다. 또한, 자바스크립트의 모든 값을 요소로 저장할 수 있다.

// 요소 존재 여부 확인
set.has(1);

//요소 삭제
set.delete(1);

// 요소 일괄 삭제
set.clear();

// 요소 순회. Set.prototype.forEach 메서드 사용 (현재 순회중 요소 값, 현재 순회중인 요소값, set 객체 자체) 첫번 째 인수와 두번 째 인수가 같다.

// 집합 연산
// 교집합
Set.prototype.intersection = function(set) {
    const result = new Set();

    for(const val of set) {
        if(this.has(val))
            result.add(val);
    }

    return result;
}

const setA = new Set([1, 2, 3, 4, 5]);
const setB = new Set([2, 4]);

setA.intersection(setB); // Set(2) {2, 4}
setB.intersection(setA); // Set(2) {2, 4}

// 밑과 같은 방법도 가능하다.
Set.prototype.intersection = function(set) {
    return new Set([...this].filter(v => set.has(v)));
}

// 합집합
Set.prototype.union = function(set) {
    const result = new Set(this);
    for(const val of set) {
        result.add(val);
    }
    return result;
}

Set.prototype.union = function(set) {
    return new Set([...this, ...set]);
}

// 차집합
Set.prototype.difference = function(set) {
    const result = new Set(this);
    for(const val of set) {
        result.delete(cal);
    }
    return result;
}

Set.prototype.difference = function(set) {
    return new Set([...this].filter(v => !set.has(v)));
}

// 부분 집합과 상위 집합
Set.prototype.isSuperset = function(subset) {
    for(const val of subset) {
        if(!this.has(val))
            return false;
    }
    return true;
}

Set.prototype.isSuperSet = function(subset) {
    const supersetArr = [...this];
    return [...subset].every(v => supersetArr.has(v));
}