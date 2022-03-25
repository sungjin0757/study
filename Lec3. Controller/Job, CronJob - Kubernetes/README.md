## Job, CronJob - Kubernetes
***

>이 글은 김태민님의 대세는 쿠버네티스 강의를 참고하여 정리하였습니다!
>
>출처 : https://www.inflearn.com/course/%EC%BF%A0%EB%B2%84%EB%84%A4%ED%8B%B0%EC%8A%A4-%EA%B8%B0%EC%B4%88/dashboard
***

### 🔍 테스트해 볼 내용
1. <span style="color:lightpink; font-weight:bold;">Job</span> 구성
2. <span style="color:lightpink; font-weight:bold;">CronJob</span> 구성

 >도커 이미지는 김태민님께서 만들어두신 이미지를 사용합니다.

 ***

 ### 🚀 Job
 <span style="color:lightpink; font-weight:bold;">Job</span>이라는 것이 무엇인지 알아봅시다.

 <span style="color:lightpink; font-weight:bold;">Pod</span>가 만들어지는 경우는 다음과 같습니다.
 1. 직접 만드는 경우
 2. <span style="color:lightpink; font-weight:bold;">ReplicaSet</span>을 사용해서 만드는 경우
 3. <span style="color:lightpink; font-weight:bold;">Job</span>을 통해서 만드는 경우

모두 같은 <span style="color:lightpink; font-weight:bold;">Pod</span>들이지만 어떤 경우에 의해 만들어졌냐에 따라 다르게 됩니다.

<span style="color:lightpink; font-weight:bold;">Node</span>가 다운 될 때, <span style="color:lightpink; font-weight:bold;">Controller</span>에 의해 만들어진 <span style="color:lightpink; font-weight:bold;">Pod</span>는 다른 <span style="color:lightpink; font-weight:bold;">Node</span>에 재생성되게 됩니다.

<span style="color:lightpink; font-weight:bold;">ReplicaSet</span>에의해 <span style="color:lightpink; font-weight:bold;">Recreate</span>된 <span style="color:lightpink; font-weight:bold;">Pod</span>가 구동하지 않을 경우 <span style="color:lightpink; font-weight:bold;">Restart</span>까지 해줍니다.
즉, 서비스가 중단되지 않는 것을 목표로서 동작하는 것 입니다.
<span style="color:lightpink; font-weight:bold;">Restart</span>는 <span style="color:lightpink; font-weight:bold;">Pod</span>는 그대로 유지시키고, <span style="color:lightpink; font-weight:bold;">Container</span>만 재구동 시키는 것을 의미합니다.

<span style="color:lightpink; font-weight:bold;">Job</span>에 의해 만들어진 <span style="color:lightpink; font-weight:bold;">Pod</span>는 프로세스가 일을 하지 않으면 <span style="color:lightpink; font-weight:bold;">Finish</span>시킵니다.
<span style="color:lightpink; font-weight:bold;">Finish</span>는 <span style="color:lightpink; font-weight:bold;">Pod</span>를 지우는 것이 아닌, 노드의 자원을 사용하지 않겠다는 것을 뜻합니다.
<span style="color:lightpink; font-weight:bold;">Finish</span>된 <span style="color:lightpink; font-weight:bold;">Pod</span>에 접속하여 <span style="color:lightpink; font-weight:bold;">Log</span> 등을 확인할 수 있고 원하는 경우 직접 삭제할 수 있습니다.

***

### 🚀 CronJob

<span style="color:lightpink; font-weight:bold;">CronJob</span>은 <span style="color:lightpink; font-weight:bold;">Job</span>들을 주기적으로 시간에 따라 생성을 하는 역할입니다.

<span style="color:lightpink; font-weight:bold;">Job</span>을 하나 단위로 쓰지는 않고 <span style="color:lightpink; font-weight:bold;">CronJob</span>을 만들어서 특정시간에 반복적으로 사용하기 위해서 만듭니다.

ex) 예약메일, 주기적인 Update확인.

***
### <span style="color:lightpink; font-weight:bold;">이상으로 마치겠습니다. 🙋🏻‍♂️</span>