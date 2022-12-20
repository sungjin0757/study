import express, { NextFunction, Request, Response } from "express";
import mongoose from 'mongoose';
import { AppConfig } from '../container/AppConfig';
import { PostDto } from '../dto/post-dto';
import { IPost } from '../model/post-model';
import { JwtFilter } from '../security/jwt-filter';

const postRouter = express.Router();
const postService = AppConfig.postService();

postRouter.post('/save', async (req: Request, res: Response, next: NextFunction) => {
    await JwtFilter.verifyToken(req, res,next);

} ,async (req: Request, res: Response, next: NextFunction) => {
    const userId: mongoose.Types.ObjectId = req.token;
    const postDto: PostDto = req.body;
    console.log(userId);
    let post: IPost | null = null;
    try{
        post = await postService.savePost(postDto, userId);
    } catch(err) {
        console.error(err);
        res.status(404).json({err});
    }

    return res.status(201).json({
        post
    });
})

postRouter.get('/:postId',  async (req: Request, res: Response, next: NextFunction) => {
    const postId: string = req.params.postId;
    let post: IPost | null = null;
    try {
        post = await postService.findPost(postId);
    } catch(err) {
        console.error(err);
        res.status(404).json({err});
    }

    res.status(200).json({post});
})

postRouter.patch('/:postId', async (req: Request, res: Response, next: NextFunction) => {
    await JwtFilter.verifyToken(req, res,next);
} , async (req: Request, res: Response, next: NextFunction) => {
    const postId: string = req.params.postId;
    const postDto: PostDto = req.body;
    console.log(postDto);
    const userId: mongoose.Types.ObjectId = req.token;
    let message: string | null = null;
    try {
        message = await postService.updatePost(postDto, postId, userId);
    } catch(err) {
        console.error(err);
        res.status(404).json({err});
    }

    res.status(200).json({message});
})

postRouter.delete('/:postId', async (req: Request, res: Response, next: NextFunction) => {
    await JwtFilter.verifyToken(req, res,next);
} , async (req: Request, res: Response, next: NextFunction) => {
    const postId: string = req.params.postId;
    const userId: mongoose.Types.ObjectId = req.token;
    let result: any = null;
    try {
        result = await postService.deletePost(postId, userId);
    } catch(err) {
        console.error(err);
        res.status(404).json({err});
    }

    res.status(200).json({result});
})
export default postRouter;