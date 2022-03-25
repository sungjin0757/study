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

**이제 Job을 만들어 보도록합시다**

**Job1.yaml**

```json
apiVersion: batch/v1
kind: Job
metadata:
  name: job-1
spec:
  template:
    spec:
      restartPolicy: Never
      containers:
      - name: container
        image: kubetm/init
        command: ["sh", "-c", "echo 'job start';sleep 20; echo 'job end'"]
      terminationGracePeriodSeconds: 0
```

**20초가 지나면 Job을 종료시키겠다는 것을 뜻합니다.**

<img width="70%" alt="스크린샷 2022-03-26 오전 2 25 58" src="https://user-images.githubusercontent.com/56334761/160171179-ed3b1a2a-32c8-4fc1-98c7-827832a1a000.png">

<img width="70%" alt="스크린샷 2022-03-26 오전 2 27 00" src="https://user-images.githubusercontent.com/56334761/160171359-3f739c64-5f7a-4ece-92c3-e7f366e435d0.png">

**job이 만들어지면서 Pod또한 만들어진 것을 볼 수 있습니다.**

<img width="70%" alt="스크린샷 2022-03-26 오전 2 30 23" src="https://user-images.githubusercontent.com/56334761/160171960-2895470b-e9ad-490f-8e4d-86584f8009f8.png">

**Sat, 26 Mar 2022 02:29:22 에 Pod가 Running중인 것을 확인할 수 있습니다.**

**20초가 지나고 Pod의 상태를 확인해봅시다.**

<img width="865" alt="스크린샷 2022-03-26 오전 2 33 34" src="https://user-images.githubusercontent.com/56334761/160172487-7efb0f58-bc5d-42e2-a38d-9cb0d6e67cf3.png">

**Pod가 종료된 것을 확인하였습니다.**

**이제, Pod에 접속해서 Log를 확인해보도록합시다!**

`kubectl logs -f pod/job-1-g5sfh`

<img width="70%" alt="스크린샷 2022-03-26 오전 2 36 17" src="https://user-images.githubusercontent.com/56334761/160172812-9c5cba10-d406-4df1-81f9-515b19d05704.png">
***

### 🚀 CronJob

<span style="color:lightpink; font-weight:bold;">CronJob</span>은 <span style="color:lightpink; font-weight:bold;">Job</span>들을 주기적으로 시간에 따라 생성을 하는 역할입니다.

<span style="color:lightpink; font-weight:bold;">Job</span>을 하나 단위로 쓰지는 않고 <span style="color:lightpink; font-weight:bold;">CronJob</span>을 만들어서 특정시간에 반복적으로 사용하기 위해서 만듭니다.

ex) 예약메일, 주기적인 Update확인.

**1분마다 Job을 만드는 CronJob을 생성해보도록 합시다.**

**cj.yaml**

```json
apiVersion: batch/v1
kind: CronJob
metadata:
  name: cj
spec:
  schedule: "*/1 * * * *"
  jobTemplate:
    spec:
      template:
        spec:
          restartPolicy: Never
          containers:
          - name: container
            image: kubetm/init
            command: ["sh", "-c", "echo 'job start';sleep 20; echo 'job end'"]
          terminationGracePeriodSeconds: 0
```

<img width="70%" alt="스크린샷 2022-03-26 오전 2 36 17" src="https://user-images.githubusercontent.com/56334761/160172812-9c5cba10-d406-4df1-81f9-515b19d05704.png">

<img width="70%" alt="스크린샷 2022-03-26 오전 2 44 09" src="https://user-images.githubusercontent.com/56334761/160174049-0f910c09-42f0-4506-b07b-c2b025c352a0.png">

**시간대를 주목하시면 1분마다 Job을 생성하는 것을 볼 수 있습니다!**

***
### <span style="color:lightpink; font-weight:bold;">이상으로 마치겠습니다. 🙋🏻‍♂️</span>