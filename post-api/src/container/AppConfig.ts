import { PostModel } from "../model/post-model";
import { UserModel } from "../model/user-model";
import UserService from '../service/user-service';
import PostService from '../service/post-service';

type UserRepository = typeof UserModel;
type PostRepository = typeof PostModel;

export class AppConfig {
    static userRepository(): UserRepository {
        return UserModel;
    }
    static postRepository(): PostRepository {
        return PostModel;
    }

    static userService(): UserService {
        return UserService.getInstance(this.userRepository());
    }

    static postService(): PostService {
        return PostService.getInstance(this.userRepository(), this.postRepository());
    }
}