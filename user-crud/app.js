import {mongoUri} from './config/key.js';
import express from 'express';
import mongoose from 'mongoose';
import { router } from './routes/user-routes.js';

const app = express();

app.use(express.json());
app.use("/users", router);

mongoose.connect(mongoUri)
    .then(() => {
        app.listen(5050, () => console.log("Connected And Listening on Port 5050"))
    })
    .catch((err) => {
        console.log(err.message);
    });
