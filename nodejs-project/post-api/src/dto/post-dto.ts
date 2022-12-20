import { IUser } from '../model/user-model';

export interface PostDto {
    title: string;
    content: string;
    user: IUser;
}