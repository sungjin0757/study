// export.js
export function f1() {
    console.log("This Is f1() In ES6 DIR");
}

const f2 = () => {
    console.log("This Is f2() In ES6 DIR");
};

const obj = {
    f3 : () => {
        console.log("This Is f3() In ES6 DIR");
    },
    f4 : () => {
        console.log("This Is f4() In ES6 DIR");
    }
}

export { f2 };
export default obj;