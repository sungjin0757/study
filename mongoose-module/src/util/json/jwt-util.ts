import jwt from "jsonwebtoken";
import JwtProperty from "./jwtProperty";

export default class JwtUtil {
    static generateToken(payload: JwtProperty): string {
        return jwt.sign(
            payload,
            process.env.JWT_SECRET!,
            {expiresIn: "1h"}
        )
    }

    static parseToken(token: string): string {
        const base64_payload = token.split('.')[1];
        const payload = Buffer.from(base64_payload, 'base64');
        const result = JSON.parse(payload.toString());
        return result;
    }

    static async getTokenChk(token: string): Promise<boolean> {
        try {
            const tokenVal = await jwt.verify(token, process.env.JWT_SECRET!);

            return true;
        } catch(err) {
            console.error(err);
            console.error("Token Verify Error");
            return false;
        }
    }
}