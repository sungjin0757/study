import express from "express";
import dotenv from "dotenv";
import mongoose from "mongoose";
import userRouter from './routes/user-routes';
import commentRouter from './routes/comment-routes';

dotenv.config();
const app = express();
const mongo_url: string | undefined = process.env.MONGO_URL 

app.use(express.json());
app.use(express.urlencoded({extended: false}));
app.use(`/users`, userRouter);
app.use(`/comments`, commentRouter);


mongoose.connect(mongo_url!, {
    dbName: 'nodejs',
})
    .then(() => {
        app.listen(8000, () => {
            console.log("Server Is Running On 8000 Port!");
        });
    })
    .catch((err) => {
        console.log(err.message);
    });
