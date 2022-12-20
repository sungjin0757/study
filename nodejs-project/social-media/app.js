import express from 'express';
import mongoose from 'mongoose';
import mongoURI from './config/key.js';
import blogRouter from './routes/blog-routes.js';
import userRouter from './routes/user-routes.js';

const app = express();
const port = 3000;

app.use(express.json());
app.use("/api/user", userRouter);
app.use("/api/blog", blogRouter)

mongoose.connect(mongoURI)
    .then(() => {
        app.listen(port, () => {
            console.log(`Application Listening On ${port}`);
        })
    })
    .catch((err) => {
        console.log(`Error Occured : ${err.message}`);
    });