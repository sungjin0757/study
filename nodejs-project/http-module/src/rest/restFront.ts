import axios from "axios";
import { User } from './model';

async function getUser(): Promise<void> {
    try {
        const users: User[] = await axios.get("/users");
        const list: HTMLElement | null = document.getElementById('list');
        list!.innerHTML = '';
        users.map((user: User) => {
            const userDiv:HTMLDivElement = document.createElement('div');
            const span = document.createElement('span');
            span.textContent = user.username;
            const edit = document.createElement('button');
            edit.textContent = '수정';
            edit.addEventListener('click', async() => {
                    const name: string | null = prompt("이름을 입력하세요");
                    if(!name) {
                        return alert("이름을 반드시 입력하셔야 합니다.");
                    }
                    try {
                        await axios.put(`/user/${user.key}`, {name});
                        getUser();
                    } catch(err) {
                        console.error(err);
                    }
                } 
            );
            const remove = document.createElement('button');
            remove.textContent = '삭제';
            remove.addEventListener('click',async () => {
                try{ 
                    await axios.delete(`/user/${user.key}`);
                    getUser();
                } catch(err){
                    console.error(err);
                }
            })
            addAppendChild(userDiv, span);
            addAppendChild(userDiv, edit);
            addAppendChild(userDiv, remove);
            list?.appendChild(userDiv);
            console.log(users);
        })
    } catch(err) {
        console.error(err);
    }
}

window.onload = getUser;
document.getElementById('form')?.addEventListener('submit', async (e: SubmitEvent | null) => {
    e?.preventDefault();
    let name: string = "";
    if(e?.target instanceof SubmitEvent) {
        name = e.target.username.value;
    }
    // const name = e!.target.username.value;
    if(!name) {
        return alert('이름을 입력하세요.');
    }
    try {
        await axios.post('/user', {name});
        getUser();
    } catch(err) {
        console.error(err);
    }
    e.target.username.value = '';
});

function addAppendChild(parent: HTMLDivElement, child: any): void {
    parent.appendChild(child);
}