import express, { NextFunction, Request, Response } from "express";

const app = express();
app.set('port', process.env.PORT || 3000);

app.use((req, res, next) => {
    console.log("모든 요청에 다 실행 된다.");
    next();
})

app.get('/', (req, res, next) => {
    console.log("GET / 요청에서만 실행");
    res.send("Hello Express!");
    next();
}, (req, res) => {
    throw new Error("에러는 에러 처리 미들웨어로 간다.");
});

app.use(errorHandler);


app.listen(app.get('port'), () => {
    console.log(app.get('port'), "Server Is Running");
});

function errorHandler(err: Error, req: Request, res: Response, next: NextFunction): void {
    console.error(err);
    res.status(500).send(err.message);
}