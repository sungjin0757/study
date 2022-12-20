import {UserModel, IUser} from "../model/user-model"
import {PostModel, IPost} from "../model/post-model"
import { PostDto } from '../dto/post-dto';
import mongoose from 'mongoose';

type User = typeof UserModel;
type Post = typeof PostModel;

export default class PostService {
    private userRepoistory!: User;
    private postRepository!: Post;
    private static INSTANCE = new PostService();

    constructor() {
    }

    static getInstance(userRepository: User, postRepository: Post): PostService {
        this.INSTANCE.userRepoistory = userRepository;
        this.INSTANCE.postRepository = postRepository;
        return this.INSTANCE;
    }

    async savePost(postDto: PostDto, userId: mongoose.Types.ObjectId): Promise<IPost> {
        const findUser = await this.findUser(userId);
        postDto.user = findUser;
        const post: IPost = await this.postRepository.create(postDto)
                                                    .catch(() => {
                                                        throw new Error("Error Occured!");
                                                    });
        return post;
    }

    async findPost(postId: string): Promise<IPost> {
        const findPost: IPost | null = await this.postRepository.findById(postId).populate('user');
        if(!findPost) {
            throw new Error("Not Found Post!");
        }
        return findPost;
    }

    async deletePost(postId: string, userId: mongoose.Types.ObjectId): Promise<any> {
        await this.validateTokenUser(postId, userId);
        const check = await this.postRepository.deleteOne({_id: postId})
                            .catch((err) => {
                                console.log(err.message);
                                throw new Error(err);
                            });
        console.log(check);
        return check;
    }

    async updatePost(postDto: PostDto, postId: string, userId: mongoose.Types.ObjectId): Promise<string> {
        await this.validateTokenUser(postId, userId);

        await this.postRepository.updateOne({_id: postId}, postDto)
                                                .catch((err) => {
                                                    console.log(err.message);
                                                    throw new Error(err.message);
                                                });
        return "Update Success";
    }

    private async findUser(userId: mongoose.Types.ObjectId): Promise<IUser> {
        const findUser: IUser | null = await this.userRepoistory.findById(userId);

        if(!findUser) {
            throw new Error("Not Found User!");
        }

        return findUser;
    }

    private async validateTokenUser(postId: string, userId: mongoose.Types.ObjectId): Promise<void> {
        console.log(123);
        console.log(userId);
        const findPost: IPost | null = await this.postRepository.findById(postId).populate('user');
        console.log(232323);
        if(!findPost) {
            throw new Error("Not Found Post!");
        }

        console.log(findPost.user._id);
    
        if(!findPost.user._id?.equals(userId)) {
            throw new Error("You Have Not Authorized");
        }

        return;
    }
}