import express, { urlencoded } from 'express';
import mongoose from 'mongoose';
import dotenv from "dotenv";
import userRouter from './router/user-router';

dotenv.config();
const app = express();
const mongoUri: string | undefined = process.env.MONGO_URI;
const port: number = 8000;

app.use(express.json());
app.use(urlencoded({extended: false}));
app.use('/api/users', userRouter);

mongoose.connect(mongoUri!, {
    dbName: 'post-api'
})
.then(() => {
    app.listen(port, () => {
        console.log(`Server Is Running On ${port} Port!`);
    });
})
.catch((err) => {
    console.log(err.message);
})