import mongoose, { Model, Schema } from "mongoose";
import { IUser } from 'model/User';

export interface IComment {
    comment?: string;
    commenter?: IUser;
    createdAt?: Date;
};

interface ICommentDocument extends IComment, Document {};
interface ICommentModel extends Model<ICommentDocument> {};

const commentSchema: Schema<ICommentDocument> = new Schema({
    comment: {
        type: String,
        required: true
    },
    commenter: {
        type: mongoose.Types.ObjectId,
        required: true,
        ref: 'User'
    },
    createdAt: {
        type: Date,
        default: Date.now
    }
});

export default mongoose.model<ICommentDocument, ICommentModel>(`Comment`, commentSchema);