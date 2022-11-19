import http from "http";

export const cookie1 = () => {
    http.createServer((req, res) => {
        res.writeHead(200, {'Set-Cookie': 'cookie=test'});
        res.end("Hello Cookie");
    }).listen(8000, () => {
        console.log("Servier Is Listening On 8000 port!");
    });
}