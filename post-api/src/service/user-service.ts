import { IUser, UserModel } from '../model/user-model';
import bcrypt from "bcryptjs";
import { UserDto, UserLoginDto } from '../dto/user-dto';
import { JwtUtil } from '../security/jwt-util';

type UserRepository = typeof UserModel;

export default class UserService {
    private userRepoisitory!: UserRepository;
    private static INSTANCE = new UserService();

    constructor() {
    }

    static getInstance(userRpeository: UserRepository): UserService {
        this.INSTANCE.userRepoisitory = userRpeository;
        return this.INSTANCE;
    }

    async signUp(userDto: UserDto): Promise<IUser> {
        await this.validateDuplicateUser(userDto.userId);

        this.changePasswordToEncoding(userDto);
        const user = await this.userRepoisitory.create(userDto);
        return user;
    }

    async login(userLoginDto: UserLoginDto): Promise<string> {
        const findUser: IUser | null = await this.findOneByUserId(userLoginDto.userId);
        if(!findUser) {
            throw new Error("Check UserId Or Password");
        }

        if(!this.isCorrectPassword(userLoginDto.password, findUser.password)) {
            throw new Error("Check UserId Or Password");
        }

        const jwt: string = JwtUtil.generateToken(findUser._id!);
        return jwt;
    }

    async findAll(): Promise<IUser[]> {
        return await this.userRepoisitory.find({});
    }

    async findOneByUserId(userId: string): Promise<IUser | null> {
        return await this.userRepoisitory.findOne({userId});
    }

    private async validateDuplicateUser(userId: string): Promise<void> {
        const findUser: IUser | null = await this.findOneByUserId(userId);
        
        if(findUser) {
            throw new Error("Duplicated User!");
        }
    }

    private changePasswordToEncoding(userDto: UserDto): void {
        userDto.password = this.encodePassword(userDto.password);
    }

    private encodePassword(password: string): string {
        return bcrypt.hashSync(password);
    }

    private isCorrectPassword(password: string, hashPassword: string) {
        return bcrypt.compareSync(password, hashPassword);
    }
}