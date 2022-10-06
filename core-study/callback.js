/**
 * 오류 우선 콜백
 * 노드가 점점 인기를 얻어가던 시기에 "오류 우선 콜백" 이라는 패턴이 생겼다. 콜백을 사용하면 예외 처리가 어려워지므로, 콜백과 관련된 에러를 처리할 방법의 표준이 필요했다.
 * 이에 따라 나타난 패턴이 콜백의 첫 번째 매개변수에 에러 객체를 쓰자는 것이었다. 에러가 null이나 undefined이면 에러가 없는 것이다.
 * 예를 들어, 노드에서 파일 콘텐츠를 읽는다고 할 때, 오류 우선 콜백을 사용한다면 다음과 같은 코드를 쓰게 된다.
 */
var fs = require(`fs`);
var fname = `test.txt`;
fs.readFile(fname, (err, data) => {
    if(err)
        return console.error(`error occured for reading file ${fname} : ${err.massage}`);
    console.log(`${fname} contents : ${data}`);
})

/**
 * 콜백에서 가장 먼저 하는 일은 err이 true인지 확인하는 것이다. true라면 파일을 읽는데 문제가 있다는 뜻이므로 콘솔에 오류를 보고하고 즉시 빠져나온다.
 * 오류 우선 콜백을 사용할 때, 가장 많이 벌어지는 실수는 아마 이부분일 것이다.
 * 에러 객체를 체크 해야 한다는 사실을 기억하고, 로그를 남기기도 하지만, 빠져 나와야한다는 사실을 잊는 사람이 많다.
 * 콜백을 사용하는 함수는 대게 콜백이 성공적이라고 사정하고 만들어진다.
 * 그런데 콜백이 실패했으니, 빠져나가지 않으면 오류를 예약하는 것이나 다름없다.
 * 프로미스를 사용하지 않으면 "오류 우선 콜백"은 노드 개발의 표준이나 다름 없다. 콜백을 사용하는 인터페이스를 만들 때는 오류 우선 콜뱅르 사용하자.
 */

/**
 * 콜백 지옥
 * 콜백을 사용해 비동기적으로 실행할 수 있긴 하지만, 현실적인 단점이 있다. 한번에 여러가지를 기다려야 한다면 콜백을 관리하기가 상당히 어려워진다.
 * 노드앱을 만든다고 가정하자. 이 앱은 세가지 파일의 콘텐츠를 읽고, 60초가 지난 다음 이들을 결합해 네번째 파일에 기록한다.
 */
var fs = require(`fs`);
fs.readFile(`a.txt`, (err, dataA) => {
    if(err)
        console.error(err);
    fs.readFile(`b.txt`, (err, dataB) => {
        if(err)
            console.error(err);
        fs.readFile(`c.txt`, (err, dataC) => {
            if(err)
                console.error(err);
            setTimeout(() => {
                fs.writeFile(`d.txt`, dataA + dataB + dataC, (err) => {
                    if(err)
                        console.error(err);
                }, 60 * 1000);
            })
        })
    })
})

/**
 * 위와 같은 코드를 콜백 지옥이라 부른다. 위 코드에서는 단순히 에러를 기록하기만 했지만, 아래코드와 같이 예외를 일으키려 했다면 더더욱 골치 아파진다.
 */
var fs = require(`fs`);
function readSketchFile() {
    try {
        fs.readFile(`does_not_exist.txt`, (err, data) => {
            if(err)
                throw err;
        })
    } catch(err) {
        console.log(`warning: minor issue occured, program continuing`);
    }
}

readSketchFile();

/**
 * 위 코드는 얼핏 타당해보이고, 예외처리도 수행하는 방어적인 코드처럼 보인다. 그러나 위 코드는 동작하지 않는다. 예외 처리가 의도대로 동작하지 않는 이유는 try ...catch 블록은 같은 함수 안에서만 동작하기 때문이다.
 * try ...catch 블록은 readSketchFile함수 안에 있지만, 정작 예외는 fs.readFile()이 콜백으로 호출하는 익명함수 안에서 일어났다.
 * 또한 콜백이 우연히 두 번 호출 되거나, 아예 호출되지 않는 경우를 방지하는 안전장치도 없다.
 * 콜백이 정확히 한번만 호출될 것을 가정하고 코드를 작성한다는 것을 보장하지 않는다.
 * 이런 문제를 해결하기 위해 프로미스가 등장한다.
 */