import User from '../model/User';
import Comment from '../model/Comment';
import UserController from '../controller/user/user-controller';
import CommentController from '../controller/comment/comment-controller';

type user = typeof User;
type comment = typeof Comment;

export default class AppConfig {
    userRepository(): user {
        return User;
    }

    commentRepository(): comment {
        return Comment;
    }

    userController(): UserController {
        return new UserController(this.userRepository());
    }

    commentController(): CommentController {
        return new CommentController(this.commentRepository(), this.userRepository());
    }
}