function f1() {
    console.log("This Is f1() In CommonJS DIR");
}

function f2() {
    console.log("This Is f2() In CommonJS DIR");
}

function f3() {
    console.log("This Is f3() In CommonJS DIR");
}

function f4() {
    console.log("This Is f4() In CommonJS DIR");
}

exports.f1 = f1;
exports.f2 = f2;

module.exports.f3 = f3;
module.exports.f4 = f4;