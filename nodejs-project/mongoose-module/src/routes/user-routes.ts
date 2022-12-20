import express from "express";
import AppConfig from '../container/Appconfig';
import UserController from '../controller/user/user-controller';

const userRouter = express.Router();
const userController: UserController = new AppConfig().userController();

userRouter.get('/', (req, res, next) => {
    userController.getAllUsers(req, res, next);
});
userRouter.get('/:id', (req, res, next) => {
    userController.getUserById(req, res, next);
});
userRouter.get(':name', (req, res, next) => {
    userController.getUserByName(req, res, next);
});
userRouter.post('/', (req, res, next) => {
    userController.saveUser(req, res, next);
});
userRouter.post('/login', (req, res, next) => {
    userController.login(req, res, next);
})

export default userRouter;