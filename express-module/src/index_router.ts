import express from "express";
import userRouter from "./router/user/userRouter";
import sampleRouter from "./router/sampleRouter";

const app = express();
const port: number = 8000;

app.use(`/users`, userRouter);
app.use(`/sample`, sampleRouter);


app.listen(port, () => {
    console.log(`Server Is Running On ${port} Port!`);
})