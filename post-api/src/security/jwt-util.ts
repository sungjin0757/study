import jwt from "jsonwebtoken";
import mongoose from "mongoose";

export class JwtUtil {
    static generateToken(userId: mongoose.Types.ObjectId) {
        return jwt.sign({userId}, 
            process.env.JWT_SECRET!, {expiresIn: '1h'});
    }

    static parseToken(token: string): mongoose.Types.ObjectId {
        const base64_payload = token.split('.')[1];
        const payload = Buffer.from(base64_payload, 'base64');
        const result = JSON.parse(payload.toString());
        return result.userId;
    }

    static async getTokenChk(token: string): Promise<mongoose.Types.ObjectId | null> {
        try {
            const tokenVal = await jwt.verify(token, process.env.JWT_SECRET!);
            return this.parseToken(token);
        } catch(err) {
            console.error(err);
            console.error("Token Verify Error");
            return null;
        }
    }
}