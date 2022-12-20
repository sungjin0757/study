import mongoose from 'mongoose';

declare global {
    namespace Express {
        interface Request {
            token: mongoose.Types.ObjectId;
        }
    }
}