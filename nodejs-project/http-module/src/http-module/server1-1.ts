import http from "http";

export const server1_1 = () => {
    const server = http.createServer((req, res) => {
        res.writeHead(200, {'Content-Type': 'text/html; charset=utf-8'});
        res.write("<h1>Hello Node!</h1>");
        res.end("<p>Hello Server!</p>");
    })
    .listen(8000, () => {
        console.log("Server Is Running On 8000 port!");
    });

    server.on('error', (error) => {
        console.error(error);
    });
}