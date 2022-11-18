import http from "http";
import * as fs from 'fs';
import { User } from './model';
import {v4 as uuid} from "uuid";

const users: User[] = [];

export const restServer = () => {
    http.createServer(async (req, res) => {
        try {
            console.log(req.method, req.url);
            if(req.method === 'GET') {
                return getMethod(req, res);
            } else if(req.method === 'post') {
                return postMethod(req, res);
            } else if(req.method === 'put') {
                return putMethod(req, res);
            } else if(req.method === 'delete') {
                return deleteMethod(req, res);
            }
        } catch(err) {
            console.error(err);
            res.writeHead(500);
            res.end(err);
        }
    }).listen(8000, () => {
        console.log("Server Is Listening On 8000 Port!");
    });
}

async function getMethod(req: http.IncomingMessage, res: http.ServerResponse<http.IncomingMessage>): Promise<http.ServerResponse<http.IncomingMessage>> {
    if(req.url === '/') {
        return getVoid(res);
    } else if(req.url === '/about') {
        return getAbout(res);
    } else if(req.url === '/users') {
        return getUser(res);
    }
    return getError(req, res);
}

async function getVoid(res: http.ServerResponse<http.IncomingMessage>): Promise<http.ServerResponse<http.IncomingMessage>> {
    const data = fs.readFileSync('/Users/admin/nodejs-project/http-module/static/rest/restFront.html');
    normalHtmlWriteHead(res);
    return res.end(data);
}

async function getAbout(res: http.ServerResponse<http.IncomingMessage>): Promise<http.ServerResponse<http.IncomingMessage>> {
    const data = fs.readFileSync('/Users/admin/nodejs-project/http-module/static/rest/about.html');
    normalHtmlWriteHead(res);
    return res.end(data);
}

async function getUser(res: http.ServerResponse<http.IncomingMessage>): Promise<http.ServerResponse<http.IncomingMessage>> {
    res.writeHead(200, {'Content-Type': 'text/plain; charset=utf-8'});
    return res.end(JSON.stringify(users));
}

async function getError(req: http.IncomingMessage, res: http.ServerResponse<http.IncomingMessage>): Promise<http.ServerResponse<http.IncomingMessage>> {
    const data = fs.readFile(`.${req.url}`,  (err, data) => {
        if(err) {
            console.error(err);
            return;
        }
        console.log(data);
    });   
    return res.end(data);
}

function normalHtmlWriteHead(res: http.ServerResponse<http.IncomingMessage>): void {
    res.writeHead(200, {'Content-Type': 'text/html; charset=utf-8'});
}


async function postMethod(req: http.IncomingMessage, res: http.ServerResponse<http.IncomingMessage>): Promise<http.IncomingMessage | null> {
    if(req.url === 'post') {
        const method = postUser(req, res);
        return method;
    }
    return null;
}

async function postUser(req: http.IncomingMessage, res: http.ServerResponse<http.IncomingMessage>): Promise<http.IncomingMessage> {
    let body: User = {
        key: "",
        username: ""
    };
    body.key = uuid();
    req.on('data', (data) => {
        body.username = data;
    });
    return req.on('end', () => {
        console.log('Post 본문 : ', body);
        users.push(body);
        res.writeHead(201);
        res.end('등록 성공');
    })
}

async function putMethod(req: http.IncomingMessage, res: http.ServerResponse<http.IncomingMessage>): Promise<http.IncomingMessage | null> {
    if(req.url?.startsWith('/user/')) {
        return putUser(req, res);
    }
    return null;
}

async function putUser(req: http.IncomingMessage, res: http.ServerResponse<http.IncomingMessage>): Promise<http.IncomingMessage | null> {
    const uuid: string | undefined = req.url?.split('/')[2];
    if(uuid === undefined) {
        throw new Error("Error Occured");
    }
    const idx: number = findUserIdx(uuid);

    return req.on('data', (data) => {
        users[idx].username = data;
        return res.end(JSON.stringify(users));
    });
}

async function deleteMethod(req: http.IncomingMessage, res: http.ServerResponse<http.IncomingMessage>): Promise<http.ServerResponse<http.IncomingMessage> | null> {
    if(req.url?.startsWith('/user/')) {
        return deleteUser(req, res);
    }
    return null;
}

async function deleteUser(req: http.IncomingMessage, res: http.ServerResponse<http.IncomingMessage>): Promise<http.ServerResponse<http.IncomingMessage>| null> {
    const uuid: string | undefined = req.url?.split('/')[2];
    if(uuid === undefined) {
        throw new Error("Error Occured");
    }
    const idx: number = findUserIdx(uuid);
    users.splice(idx, 1);
    return res.end(JSON.stringify(users));
}

function findUserIdx(uuid: string): number {
    return users.findIndex((u: User) => u.key === uuid);
}