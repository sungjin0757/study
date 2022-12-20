import mongoose, { Schema } from 'mongoose';
import { IUser } from './user-model';

export interface IPost {
    _id?: mongoose.Types.ObjectId;
    title: string;
    content: string;
    user: IUser;
}

const postSchema: Schema<IPost> = new Schema({
    title: {
        type: String,
        required: true
    },
    content: {
        type: String,
        required: true
    },
    user: {
        type: mongoose.Types.ObjectId,
        required: true,
        ref: 'User'
    }
}, {timestamps: true});

export const PostModel = mongoose.model<IPost>('Post', postSchema);