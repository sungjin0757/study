/**
 * 읽기 좋은 코드
 */
var user = {
    id : 1,
    name : "Hong" 
};
if(user.id === 1) {
    console.log(`name = ${user.name}`); // name = Hong
}

// function logFetchUser(findId) {
//     var user = fetchUser(`/api/users/${findId}`) //http 통신 코드라 가정
//     if(isUser(user.id, findId))
//         console.log(user.name);
// }

function logFetchUser(findId) {
    var user = fetchUser(`/api/users/${findId}`, (user) => {
    if(isUser(user.id, findId))
        console.log(user.name);
    }); 
}

async function logFetchUser(findId) {
    var user = await fetchUser(`/api/users/${findId}`);
    if(isUser(user.id, findId))
        console.log(user.name);
}

function isUser(userId, findId) {
    if(userId === findId)
        return true;
    return false;
}


async function foo() {
    return 1;
}

foo().then((item) => {
    console.log(item);
});

async function bar() {
    let promise = new Promise((resolve, reject) => {
        setTimeout(() => resolve("setTimeOut Finished!"), 1000);
    });

    let result = await promise;

    console.log(result); // setTimeOut Finished!
}

class ThenableClass {
    constructor() {}

    then(resolve, reject) {
        resolve("Hello");
    }
}

async function thenableFunction() {
    let res = await new ThenableClass();
}

class AsyncMethod {
    async test() {
        return await new Promise((resolve, reject) => {
            resolve("test");
        });
    }
}

async function awaitErrorEx() {
    try {
        let user = await fetchUser(`/api/users/1`);
    } catch(err) {
        console.error(`Error Occured! : ${err.message}`);
    }
}

async function awaitErrorEx() {
    let user = await fetchUser(`/api/users/1`);
}

awaitErrorEx()
    .catch((err) => {
        console.error(`Error Occured! : ${err.message}`);
    }
);

// async function test() {
//     let results = await Promise.all([
//         fetchUser(url1),
//         fetchUser(url2),
//         fetchUser(url3)
//         ...
//     ]);
// };