import http from "http";

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


export const cookie2 = () => {
    http.createServer(async (req, res) => {
        const cookies = parseCookie(req.headers.cookie);
        console.log(cookies);
        console.log(req.url, req.method);

        if(req.url?.startsWith('/login') && req.method === 'POST') {
            console.log(1);
            let name = "";
            await req.on('data', (data) => {
                name = JSON.parse(data).name;
                console.log(name);
            });
            const expires = new Date(); 
            expires.setMinutes(expires.getMinutes() + 5);
            res.writeHead(302, {
                'Set-Cookie': `name=${encodeURIComponent(name)}; Expires=${expires.toUTCString()}; HttpOnly;`
            });
            res.end(`${name}님 로그인 되셨어요`);
        } else if(cookies.name) {
            res.writeHead(200, {
                'Content-Type': 'text/html; charset=utf-8;'
            });
            res.end(`${cookies.name}님 하이요`);
        }
    }).listen(8000, () => {
        console.log("Server Is Running On 8000 Port");
    });
}