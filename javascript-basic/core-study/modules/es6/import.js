// 사용하려는 객체만 선택하여 가져오기
import {f1} from './export.js'
f1(); // This Is f1() In ES6 DIR

// 모든 객체를 가져오되 원하는 이름을 붙여서 사용
import * as mod from './export.js'
mod.f1(); // This Is f1() In ES6 DIR
mod.f2(); // This Is f2() In ES6 DIR

// default 객체 가져오기. 정확한 객체의 명칭을 사용하지 않아도 됨.
import obj from './export.js'
obj.f3(); // This Is f3() In ES6 DIR
obj.f4(); // This Is f4() In ES6 DIR