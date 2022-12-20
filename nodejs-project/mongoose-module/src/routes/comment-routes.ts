import express from "express";
import CommentController from '../controller/comment/comment-controller';
import AppConfig from '../container/Appconfig';
import JwtFilter from '../util/json/jwt-verify';

const commentRouter = express.Router();
const commentController: CommentController = new AppConfig().commentController();

commentRouter.get('/:id', (req, res, next) => {
    commentController.getCommentById(req, res, next);
});
commentRouter.post('/:id', (req, res, next) => {JwtFilter.verifyToken(req, res, next)}, (req, res, next) => {
    commentController.saveComment(req, res, next);
});

export default commentRouter; 