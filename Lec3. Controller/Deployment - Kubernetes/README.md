## Deployment - Kubernetes
***

>이 글은 김태민님의 대세는 쿠버네티스 강의를 참고하여 정리하였습니다!
>
>출처 : https://www.inflearn.com/course/%EC%BF%A0%EB%B2%84%EB%84%A4%ED%8B%B0%EC%8A%A4-%EA%B8%B0%EC%B4%88/dashboard
***

### 🔍 테스트해 볼 내용
1. <span style="color:lightpink; font-weight:bold;">Recreate</span> 구성
2. <span style="color:lightpink; font-weight:bold;">Rolling Update</span> 구성

 >도커 이미지는 김태민님께서 만들어두신 이미지를 사용합니다.

 ***
먼저, <span style="color:lightpink; font-weight:bold;">Deployment Controller</span>를 사용하는 이유를 알아봅시다.

한 서비스가 운영중일 때, 서비스의 버전을 <span style="color:lightpink; font-weight:bold;">업그레이드</span>시키고 싶을 때 사용하는 <span style="color:lightpink; font-weight:bold;">Controller</span>입니다.

버전을 업그레이드할 때도 다양한 방법이 있는데, <span style="color:lightpink; font-weight:bold;">Recreate, Rolling Updaten</span> 방법에 대해서 배워봅시다.

 ### 🚀 Recreate

<span style="color:lightpink; font-weight:bold;">Deployment</span>를 만들게 되면 <span style="color:lightpink; font-weight:bold;">V1의 Pod</span>들이 만들어지게 됩니다.

업그레이드를 하게되면 먼저 <span style="color:lightpink; font-weight:bold;">V1의 Pod</span>들을 삭제시킨다음, 순차적으로 <span style="color:lightpink; font-weight:bold;">V2의 Pod</span>들을 생성하면 됩니다.

단점은, <span style="color:lightpink; font-weight:bold;">V1의 Pod</span>들을 먼저 삭제시키는 과정이 필요하므로, 서비스에 <span style="color:lightpink; font-weight:bold;">Down Time</span>이 생길수도 있습니다.

**이제 한번 해보도록 합시다!**

**dp1.yaml**

```json
apiVersion: apps/v1
kind: Deployment
metadata:
  name: dp-1
spec:
  selector:
    matchLabels:
      type: app
  replicas: 2
  strategy:
    type: Recreate
  revisionHistoryLimit: 1
  template:
    metadata:
      labels:
        type: app
    spec:
      containers:
      - name: container
        image: kubetm/app:v1
      terminationGracePeriodSeconds: 10
```

<img width="70%" alt="스크린샷 2022-03-22 오후 3 41 05" src="https://user-images.githubusercontent.com/56334761/159422780-32934643-34d4-43c1-82ad-db0d18336e96.png">

**이제, Pod및 생성된 ReplicaSet을 확인해보도록 합시다!**

<img width="70%" alt="스크린샷 2022-03-22 오후 3 43 42" src="https://user-images.githubusercontent.com/56334761/159423050-ed3eacc4-3fb1-4485-b6fa-cc21172fb2ee.png">

이렇게 Pod와 ReplicaSet이 생성된 것을 확인 할 수 있습니다.

이제, Pod들에 접속할 Service를 하나 만들어 봅시다.

**svc1.yaml**

```json
apiVersion: v1
kind: Service
metadata:
  name: svc-1
spec:
  selector:
    type: app
  ports:
  - port: 8080
    protocol: TCP
    targetPort: 8080
```

<img width="70%" alt="스크린샷 2022-03-22 오후 3 47 00" src="https://user-images.githubusercontent.com/56334761/159423481-442814c8-7653-4caf-9d6d-7efa11923a46.png">

<img width="70%" alt="스크린샷 2022-03-22 오후 3 49 38" src="https://user-images.githubusercontent.com/56334761/159423885-77660517-ee1c-4ed3-bc0f-38a07b8e350f.png">

지금 부터 해볼 것은,
Service에 접속하여 무한루프로 Pod의 Version을 출력하는 명령어를 날리도록 하겠습니다.

그 다음, 무한 루프를 돌면서 버전을 업데이트 해보도록 합시다. 

`while true; do curl 10.109.168.121:8080/version; sleep 1; done`


<img width="442" alt="스크린샷 2022-03-22 오후 3 50 56" src="https://user-images.githubusercontent.com/56334761/159424048-ec37421c-368b-4060-9ec1-9672951b21b5.png">

이제 Version을 바꿔보도록 합시다.

`kubectl edit deployment dp-1 -o yaml`

<img width="444" alt="스크린샷 2022-03-22 오후 3 52 32" src="https://user-images.githubusercontent.com/56334761/159424289-6f0eec00-318f-4e54-bc89-2467d3fa3ee3.png">

<img width="439" alt="스크린샷 2022-03-22 오후 3 53 06" src="https://user-images.githubusercontent.com/56334761/159424349-6e0e995b-259d-487d-8a2e-4a7e4b311d1a.png">

이렇게 Server가 잠시 끊겼다가 버젼이 업그레이드 되는 것을 볼 수 있습니다!

***

### 🚀 Rolling Update

업그레이드를 실행하게 되면 먼저 <span style="color:lightpink; font-weight:bold;">V2 Pod</span>를 하나 만들어 줍니다.

그 다음 <span style="color:lightpink; font-weight:bold;">V1 Pod</span>를 하나 삭제하게 됩니다.
이런 과정을 순차적으로 진행하게 됩니다.

이 방식은 배포 중산에 <span style="color:lightpink; font-weight:bold;">Downtime</span>이 없습니다!

한번 실습해보도록 합시다!

**dp2.yaml**

```json
apiVersion: apps/v1
kind: Deployment
metadata:
  name dp-2
spec:
  selector:
    matchLabels:
      type: app2
  replicas: 2
  strategy:
    type: RollingUpdate
  minReadySeconds: 10
  template:
    metadata:
      labels:
        type: app2
    spec:
      containers:
      - name: container
        image: kubetm/app:v1
      terminationGracePeriodSeconds: 0
```

<img width="70%" alt="스크린샷 2022-03-22 오후 4 00 37" src="https://user-images.githubusercontent.com/56334761/159425470-d0a6f64b-3b3e-4e5f-a5ce-47a9d9b4da9c.png">

이제 Service를 만듭시다.

**svc2.yaml**

```json
apiVersion: v1
kind: Service
metadata:
  name: svc-2
spec:
  selector:
    type: app2
  ports:
  - port: 8080
    protocol: TCP
    targetPort: 8080
```

<img width="70%" alt="스크린샷 2022-03-22 오후 4 02 19" src="https://user-images.githubusercontent.com/56334761/159425704-b2031f77-ae35-4bdf-a5b9-11e682de752e.png">

이제,Recreate에서 했던거와 같이 똑같이 실습해보도록 합시다.

`while true; do curl 10.102.205.50:8080/version; sleep 1; done`

`kubectl edit deployment dp-2 -o yaml`

<img width="70%" alt="스크린샷 2022-03-22 오후 4 05 22" src="https://user-images.githubusercontent.com/56334761/159426224-bfe26066-c9b8-4827-b65c-e728c4b478e8.png">

이런식으로, V2가 생성 되며 Downtime없이 순차적으로 V1이 삭제되는 것을 볼 수 있습니다!

***

### <span style="color:lightpink; font-weight:bold;">이상으로 마치겠습니다. 🙋🏻‍♂️</span>