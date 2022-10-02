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