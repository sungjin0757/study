import express, { application, NextFunction, Request, Response } from "express";
import morgan from "morgan";
import cookieParser from "cookie-parser";
import session from "express-session";
import dotenv from "dotenv";
import path from "path";
import { upload } from './multer/sampleMulter';

dotenv.config();
const app = express();
app.set('port', process.env.PORT || 3000);
const cookieSecret: string | undefined = process.env.COOKIE_SECRET;

app.use(morgan('dev'));
app.use(`/`, express.static(path.join(__dirname, 'public')));
app.use(express.json());
app.use(express.urlencoded({extended: false}));
app.use(cookieParser(cookieSecret!));
app.use(session({
    resave: false,
    saveUninitialized: false,
    secret: cookieSecret!,
    cookie: {
        httpOnly: true,
        secure: false
    },
    name: `session-cookie`
}));

app.use((req, res, next) => {
    console.log("모든 요청에 다 실행 된다.");
    next();
})

app.get('/', (req, res, next) => {
    console.log("GET / 요청에서만 실행");
    res.send("Hello Express!");
    next();
});

app.use(errorHandler);

app.post('/upload', upload.single('image'), (req, res) => {
    console.log(req.file, req.body);
    res.send('ok');
})

app.listen(app.get('port'), () => {
    console.log(app.get('port'), "Server Is Running");
});

function errorHandler(err: Error, req: Request, res: Response, next: NextFunction): void {
    console.error(err);
    res.status(500).send(err.message);
}