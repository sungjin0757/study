## Pod - Container, Label, Node Schedule
***

>이 글은 김태민님의 대세는 쿠버네티스 강의를 참고하여 정리하였습니다!
>
>출처 : https://www.inflearn.com/course/%EC%BF%A0%EB%B2%84%EB%84%A4%ED%8B%B0%EC%8A%A4-%EA%B8%B0%EC%B4%88/dashboard
***

### 🔍 테스트 해 볼 내용
1. <span style="color:lightpink; font-weight:bold;">Pod</span>가 어떻게 구성되어 있는지 알아 봅시다.
2. <span style="color:lightpink; font-weight:bold;">Pod</span> 에 <span style="color:lightpink; font-weight:bold;">label</span>을 달아 봅시다.
3. <span style="color:lightpink; font-weight:bold;">Node Schedule</span>에 대하여 간략히 알 아봅시다. 

>도커 이미지는 김태민님께서 만들어두신 이미지를 사용합니다.

***

### 🚀 Pod - Container

먼저, <span style="color:lightpink; font-weight:bold;">Pod</span>는 기본적으로 한 개 이상의 <span style="color:lightpink; font-weight:bold;">컨테이너</span>들로 구성괴어 있습니다.

<span style="color:lightpink; font-weight:bold;">Pod</span>안의 <span style="color:lightpink; font-weight:bold;">컨테이너</span>들은 서비스가 서로 연결될 수 있도록 <span style="color:lightpink; font-weight:bold;">포트</span>를 가지고 있습니다.

여기서, 한 <span style="color:lightpink; font-weight:bold;">컨테이너</span>는 하나 이상의 <span style="color:lightpink; font-weight:bold;">포트</span>를 가질 수 있지만, 한 <span style="color:lightpink; font-weight:bold;">포트</span>를 여러 <span style="color:lightpink; font-weight:bold;">컨테이너</span>가 공유하지는 못하게 됩니다.

<span style="color:lightpink; font-weight:bold;">Pod</span>는 또한 생성될 때 고유의 <span style="color:lightpink; font-weight:bold;">IP</span>가 생성됩니다.

<span style="color:lightpink; font-weight:bold;">Pod</span>를 <span style="color:lightpink; font-weight:bold;">IP</span>를 통해서 접근할 경우 <span style="color:lightpink; font-weight:bold;">쿠버네티스 클러스터</span> 내에서만 접근이 가능하고, 외부에서는 접근이 불가능하게 됩니다.

**한번 직접해 보도록 하겠습니다.**

`vi pod.yaml`

```json
apiVersion: v1
kind: Pod
metadata:
  name: pod-1
spec:
  containers:
  - name: container1
    image: kubetm/p8000
    ports:
    - containerPort: 8000
  - name: container2
    image: kubetm/p8080
    ports:
    - containerPort: 8080
```

`kubectl create -f ./pod.yaml`

`kubectl apply -f ./pod.yaml`

<img width="70%" alt="스크린샷 2022-02-25 오후 5 08 27" src="https://user-images.githubusercontent.com/56334761/155678733-178f08c3-7f7d-494d-b9c5-3e62a99d4966.png">

이제 Pod로 접근하여 포트에 따른 컨테이너 이름을 출력하여 봅시다. (도커 이미지에 따른 기능입니다.)

**Pod IP확인**

`kubectl get pods -o wide`

**Pod 접속**

`kubectl exec -it pod-1 /bin/bash`

**curl 명령어로 Container Port 확인**

`curl 10.244.0.5:8000`

`curl 10.244.0.5:8080`


<img width="70%" alt="스크린샷 2022-02-25 오후 5 26 12" src="https://user-images.githubusercontent.com/56334761/155681172-0992702f-3e50-47b4-b833-e69f623de0d1.png">

Pod내의 컨테이너들이 잘 만들어 진 것을 확인하였습니다.

**지금 부터는 Pod내의 컨테이너들의 포트가 같을 때 에러가 발생하는지 살펴 봅시다.**
- 명령어는 위의 내용들과 거의 동일 합니다.

**pod2.yaml**

```json
apiVersion: v1
kind: Pod
metadata:
  name: pod-2
spec:
  containers:
  - name: container1
    image: kubetm/p8000
    ports:
    - containerPort: 8000
  - name: container2
    image: kubetm/p8000
    ports:
    - containerPort: 8000
```

**더욱 더 눈에 띄는 결과를 위해 대시보드에 접속해 보도록 하겠습니다!**

<img width="1440" alt="스크린샷 2022-02-25 오후 5 31 33" src="https://user-images.githubusercontent.com/56334761/155681975-e45f3ad0-7a60-457b-9feb-bc12c56fc093.png">


<img width="1440" alt="스크린샷 2022-02-25 오후 5 32 22" src="https://user-images.githubusercontent.com/56334761/155682094-1b49c330-5734-4a6a-9886-1da4df55d835.png">


**컨테이너 생성이 잘 안된 모습을 확인할 수 있습니다!**

***

### 🚀 Label

<span style="color:lightpink; font-weight:bold;">Pod</span>뿐만이 아니라 모든 <span style="color:lightpink; font-weight:bold;">오브젝트</span>에서 다 쓰입니다. 

하지만, <span style="color:lightpink; font-weight:bold;">Pod</span>에서 가장 많이 사용한다고 합니다.

<span style="color:lightpink; font-weight:bold;">Label</span>을 쓰는 이유는 목적에 따라 <span style="color:lightpink; font-weight:bold;">오브젝트</span>들을 분류할 수 있으며, <span style="color:lightpink; font-weight:bold;">오브젝트</span>들을 따로 연결하기 위해서 사용합니다.

구성은 <span style="color:lightpink; font-weight:bold;">Key와 Value</span>형태로 만들어집니다.

**이제 Pod2개를 만들어서 실습해 봅시다.**

**pod3.yaml**

```json
apiVersion: v1
kind: Pod
metadata:
  name: pod-3
  labels:
    type: web
spec:
  containers:
  - name: container
    image: kubetm/init
```

**pod4.yaml**

```json
apiVersion: v1
kind: Pod
metadata:
  name: pod-4
  labels:
    type: database
spec:
  containers:
  - name: container
    image: kubetm/init
```

생성된 Label을 선택하기 위한 Service입니다.

**service1.yaml**
```json
apiVersion: v1
kind: Service
metadata:
  name: svc-1
spec:
  selector:
    type: web
  ports:
  - port: 8080
```

**service2.yaml**

```json
apiVersion: v1
kind: Service
metadata:
  name: svc-2
spec:
  selector:
    type: database
  ports:
  - port: 8080
```

**서비스 1과 서비스 2는 각각 type이 web과 database인 Pod를 가리키고 있습니다.**

**dashboard에 접근하여 각각의 서비스가 각각의 Pod를 잘 연결 하였는지 확인해 봅시다.**

<img width="1440" alt="스크린샷 2022-02-25 오후 5 48 45" src="https://user-images.githubusercontent.com/56334761/155684598-21f3f5e2-9fde-4f95-979e-461bed3c4a27.png">

<img width="1440" alt="스크린샷 2022-02-25 오후 5 48 45" src="https://user-images.githubusercontent.com/56334761/155684731-2ec2a4f9-125c-445b-8ca5-75bef8485b31.png">

이렇게 잘 지칭하고 있는 것을 볼 수 있습니다!

***

### 🚀 Node Schedule

<span style="color:lightpink; font-weight:bold;">Pod</span>는 여러 <span style="color:lightpink; font-weight:bold;">노드</span>들 중에 한 노드에 올라가야 합니다.

직접 <span style="color:lightpink; font-weight:bold;">노드</span>를 선택하는 방법, <span style="color:lightpink; font-weight:bold;">쿠버네티스</span>가 자동으로 선택하는 방법이 존재합니다.

<span style="color:lightpink; font-weight:bold;">쿠버네티스의 스케쥴러</span>가 판단 하여 <span style="color:lightpink; font-weight:bold;">노드</span>를 고를 수 있습니다.
- Cpu 사용량
- Memory 사용량
- ...

하지만, 제가 사용하고 있는 <span style="color:lightpink; font-weight:bold;">minikube</span>환경은 <span style="color:lightpink; font-weight:bold;">단일 노드 클러스터</span>이기 때문에 실습을 진행할 수 없기에 여기서 마치겠습니다!

***
### <span style="color:lightpink; font-weight:bold;">이상으로 마치겠습니다. 🙋🏻‍♂️</span>