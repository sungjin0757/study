import mongoose, { Document, Model, Schema } from "mongoose";

export interface IUser {
    name?: string;
    password?: string;
    age?: number;
    married?: boolean;
    createdAt?: Date;
};

interface IUserDocument extends IUser, Document {}

interface IUserModel extends Model<IUserDocument>{}

const userSchema: Schema<IUserDocument> = new Schema({
    name: {
        type: String,
        required: true
    },
    password: {
        type: String,
        required: true
    },
    age: {
        type: Number,
        required: true
    },
    married: {
        type: Boolean,
        required: true
    },
    createdAt: {
        type: Date,
        default: Date.now
    }
});


export default mongoose.model<IUserDocument, IUserModel>("User", userSchema);