import { Address } from './address';
import mongoose, { Schema } from 'mongoose';

export interface IUser {
    _id?: mongoose.Types.ObjectId; 
    userId: string;
    password: string;
    firstName: string;
    lastName: string;
    age: number;
    phoneNumber: string;
    address: Address;
}

const userSchema: Schema<IUser> = new Schema({
    userId: {
        type: String,
        required: true
    },
    password: {
        type: String,
        required: true
    },
    firstName: {
        type: String,
        required: true
    },
    lastName: {
        type: String,
        required: true
    },
    age: {
        type: Number,
        required: true
    },
    phoneNumber: {
        type: String,
        required: true
    },
    address: {
        type: Object,
        required: true
    }
}, {timestamps: true});

export const UserModel = mongoose.model<IUser>('User', userSchema);