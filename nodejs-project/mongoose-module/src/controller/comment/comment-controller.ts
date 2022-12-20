import Comment, {IComment} from "../../model/Comment";
import { Request, Response } from 'express';
import { NextFunction } from 'express';
import User, {IUser} from '../../model/User';

type comment = typeof Comment;
type user = typeof User;

export default class CommentController {
    private commentRepository: comment;
    private userRepository: user;

    constructor(commentRepository: comment, userRepository: user) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    async getCommentById(req: Request, res: Response, next: NextFunction): Promise<Response> {
        let findComment: IComment | null;
        const id: string = req.params.id;

        findComment = await this.commentRepository.findById(id).populate('commenter');
        if(!findComment) {
            return res.status(404).json({
                message: "Not Found Comment"
            });
        }
        console.log(4343);
        return res.status(200).json({findComment});    
    }

    async saveComment(req: Request, res: Response, next: NextFunction): Promise<Response> {
        const commentParam: IComment = req.body
        const userId: string = req.params.id;
        let comment: IComment;
        let findUser: IUser | null;

        findUser = await this.userRepository.findById(userId);
        if(!findUser) {
            return res.status(404).json({
                message: "Not Fount User"
            });
        }

        commentParam.commenter = findUser;
        comment = await this.commentRepository.create(commentParam);

        return res.status(201).json({
            comment
        });
    }
}