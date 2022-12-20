import express from 'express';
import UserService from '../service/user-service';
import { AppConfig } from '../container/AppConfig';
import { UserDto, UserLoginDto } from '../dto/user-dto';
import { IUser } from '../model/user-model';

const userRouter = express.Router();
const userService: UserService = AppConfig.userService();

userRouter.get('/', async (req, res, next) => {
    let findUsers;
    try{
        findUsers = await userService.findAll();
    } catch(err) {
        console.error(err);
        res.status(404).json({err});
    }
    res.status(200)
    .json({findUsers});
});

userRouter.get('/:userId', async (req, res, next) => {
    const userId: string = req.params.userId;
    let findUser: IUser | null = null;
    try {
        findUser = await userService.findOneByUserId(userId);
    } catch(err) {
        console.error(err);
        res.status(404).json({err});
    }
    
    res.status(200)
        .json({findUser});
});

userRouter.post('/signup', async (req, res, next) => {
    const userDto: UserDto = req.body;
    let user;
    try{
        user = await userService.signUp(userDto);
    } catch(err) {
        console.error(err);
        res.status(404).json({err});
    }

    res.status(201)
    .json({user});
});

userRouter.post('/login', async (req, res, next) => {
    const userLoginDto: UserLoginDto = req.body;
    let jwt = undefined;
    try{
        jwt = await userService.login(userLoginDto);
    } catch(err) {
        console.error(err);
        res.status(404).json({err});
    }
    
    res.status(200)
        .json({jwt});
});


export default userRouter;