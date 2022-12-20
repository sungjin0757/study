import http from "http";
import url from "url";
import * as qs from "querystring";
import { Session } from './model/Session';
import {v4 as uuid} from "uuid";

const parseCookie = (cookie: string = '') => 
    cookie
        .split(';')
        .map(v => v.split('='))
        .reduce((acc: any, [k, v]: string[]) => {
            console.log(k);
            console.log(v);
            acc[k.trim()] = decodeURIComponent(v);
            return acc;
        }, {});

const session: Map<string, Session> = new Map(); 

export const cookie2 = () => {
    http.createServer(async (req, res) => {
        const cookies = parseCookie(req.headers.cookie);

        if(req.url?.startsWith('/login') && req.method === 'POST') {
            console.log(1);
            let name = "";
            await req.on('data', (data) => {
                name = JSON.parse(data).name;
            });
            const expires = new Date(); 
            expires.setMinutes(expires.getMinutes() + 5);
            const randomUUID = uuid();
            session.set(randomUUID, {
                "name": name,
                "expire": expires
            });
            res.writeHead(302, {
                'Set-Cookie': `session=${randomUUID}; Expires=${expires.toUTCString()}; HttpOnly;`
            });
            res.end(`${name}님 로그인 되셨어요`);
        } else if(!session.get(cookies.session)) {
            res.writeHead(403);
            res.end('권한 없음');
        } else if(cookies.session && session.get(cookies.session)!.expire > new Date()) {
            res.writeHead(200, {
                'Content-Type': 'text/html; charset=utf-8;'
            });
            res.end(`${cookies.name}님 하이요`);
        } else {
            res.writeHead(403);
            res.end('권한 없음');
        }
    }).listen(8000, () => {
        console.log("Server Is Running On 8000 Port");
    });
}