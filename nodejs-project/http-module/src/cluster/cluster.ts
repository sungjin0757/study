import cluster from "cluster";
import http from "http";
import os from "os";

const numCpus = os.cpus().length;

export const cluster_test = () => {
    if(cluster.isMaster) {
        console.log(`마스터 프로세스 아이디 : ${process.pid}`);
        makeProcess();
        postProces();
    } else {
        http.createServer((req, res) => {
            res.writeHead(200, {'Content-Type': 'text/html; charset=utf-8;'});
            res.end("Hello Cluster!");
            setTimeout(() => {
                process.exit(1);
            }, 1000);
        })
        .listen(8000, () => {
            console.log("Server Is Running On 8000 port");
        })
    }
    console.log(`${process.pid} 번 실행 `);
}

function makeProcess(): void {
    for(let i = 0; i < numCpus; i++) {
        cluster.fork();
    }
}

function postProces(): void {
    cluster.on('exit', (worker: Worker, code: number, signal: string) => {
        console.log(`${process.pid} 가 종료되었습니다.`);
        console.log('code', code, 'signal', signal);
    })
}