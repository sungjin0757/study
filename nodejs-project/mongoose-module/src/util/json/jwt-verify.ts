import { NextFunction, Request, Response } from "express";
import JwtUtil from './jwt-util';

export default class JwtFilter {
    static async verifyToken(req: Request, res: Response, next: NextFunction): Promise<void> {
        const token: string | undefined = req.get('authorization');
        console.log(token!);
        if(token) {
            await this.authenticateToken(token, res, next);
        } else if(!token) {
            await this.processNonAccessToken(res);
        }
    }

    private static async authenticateToken(token: string, res: Response, next: NextFunction): Promise<void> {
        const access_token: string = token.substring(7);
        if(access_token !== 'null'){
            await this.authenticateAccessToken(access_token, res, next);
        } else {
            console.warn("Access Token None");
            res.status(403).json({
                message: "Access Token None"
            })
        };
    }

    private static async authenticateAccessToken(access_token: string, res: Response, next: NextFunction): Promise<void> {
        const auth_jwt = await JwtUtil.getTokenChk(access_token);
        if(auth_jwt) {
            console.log("Token Is Available");
            next();
        } else {
            console.warn("Access Token expires");
            res.status(403).json({
                message: "Access Token Expires"
            });
        }
    }

    private static async processNonAccessToken(res: Response): Promise<void> {
        res.status(405)
            .json({
                message: "No Permission"
            });
    }
}