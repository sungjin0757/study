import { Address } from '../model/address';

export interface UserDto {
    userId: string;
    password: string;
    firstName: string;
    lastName: string;
    age: number;
    'phone-number': string;
    address: Address;
}

export interface UserLoginDto {
    userId: string;
    password: string;
}