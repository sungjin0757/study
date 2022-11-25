import { NextFunction, Request, Response } from "express";
import User from "../../model/User";
import { IUser } from '../../model/User';
import bcrypt from "bcryptjs";
import JwtUtil from '../../util/json/jwt-util';

type user = typeof User;

export default class UserController {
    private userRepository: user;

    constructor(userRepository: user) {
        console.log(this);
        this.userRepository = userRepository;
    }

    async getAllUsers(req: Request, res: Response, next: NextFunction): Promise<Response> {
        const findUser = await this.userRepository.find();
        if(!findUser) {
            return res.status(500)
        }
        return res.status(200).json({findUser});
    }

    async getUserByName(req: Request, res: Response, next: NextFunction): Promise<Response> {
        const name: string = req.params.name;
        let findUser;
        try {
            findUser = await this.userRepository.findOne({name});
        } catch(err) {
            next(err);
        }
        if(!findUser) {
            return res.status(404).json({
                message: `Not Found User`
            });
        }
        return res.status(200).json({findUser});
    }

    async getUserById(req: Request, res: Response, next: NextFunction): Promise<Response> {
        const id: string = req.params.id;
        let findUser: IUser | null;
        findUser = await this.userRepository.findById(id);
        if(!findUser) {
            return res.status(404).json({
                message: "Not Found User"
            });
        }

        return res.status(200).json({
            findUser
        });
    }

    async saveUser(req: Request, res: Response, next: NextFunction): Promise<Response> {
        const userParam: IUser = req.body;
        let user;
        
        if(!userParam.age || userParam.married === null || userParam.married === undefined || !userParam.name || !userParam.password) {
            return res.status(500)
                    .json({
                        message: "Invalid Data"
                    });
        }
        userParam.password = this.makePasswordToHash(userParam.password);
        
        try {
            user = await this.userRepository.create(userParam);
        } catch(err) {
            next(err);
        }

        return res.status(201)
                .json({
                    user
                });
    }

    async login(req: Request, res: Response, next: NextFunction): Promise<Response> {
        const {name, password} = req.body;
        const findUser: IUser | null = await this.userRepository.findOne({name});
        if(!findUser) {
            return res.status(404)
                    .json({
                        message: "Not Found User"
                    });
        }

        if(!this.isCorrectPassword(password, findUser.password!)) {
            return res.status(404)
                    .json({
                        message: "Invalid Password"
                    });
        }

        const jwt: string = JwtUtil.generateToken({id: findUser.name!});
        
        return res.status(200)
            .json({jwt});
    }

    makePasswordToHash(password: string): string {
        return bcrypt.hashSync(password);
    }

    isCorrectPassword(password: string, hashPassword: string): boolean {
        return bcrypt.compareSync(password, hashPassword);
    }
}