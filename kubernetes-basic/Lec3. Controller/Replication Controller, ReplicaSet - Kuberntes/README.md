## Replication Controller, ReplicaSet - Kubernetes
***

>이 글은 김태민님의 대세는 쿠버네티스 강의를 참고하여 정리하였습니다!
>
>출처 : https://www.inflearn.com/course/%EC%BF%A0%EB%B2%84%EB%84%A4%ED%8B%B0%EC%8A%A4-%EA%B8%B0%EC%B4%88/dashboard
***

### 🔍 테스트해 볼 내용
1. <span style="color:lightpink; font-weight:bold;">Template, Replicas</span> 구성
2. <span style="color:lightpink; font-weight:bold;">Selector</span> 구성

 >도커 이미지는 김태민님께서 만들어두신 이미지를 사용합니다.

 ***

 ### 🚀 Controller

 먼저, <span style="color:lightpink; font-weight:bold;">Controller</span>를 사용하는 이유에 대하여 알아봅시다.

 가장 중요한 이유는 <span style="color:lightpink; font-weight:bold;">Service</span>를 관리하고 운영하는데 도움을 주기 때문입니다.

 - <span style="color:lightpink; font-weight:bold;">Auto Scaling</span>
   - <span style="color:lightpink; font-weight:bold;">Pod</span>가 장애로 다운될 시에, 이 <span style="color:lightpink; font-weight:bold;">Pod</span>를 다른 노드에 생성
- <span style="color:lightpink; font-weight:bold;">Software Update</span>
  - 여러 <span style="color:lightpink; font-weight:bold;">Pod</span>의 버전을 업그레이드 할 때 <span style="color:lightpink; font-weight:bold;">Controller</span>를 통해 쉽게 할 수 있고, 도중 문제가 생기면 롤백 가능
- <span style="color:lightpink; font-weight:bold;">Job</span>
  - 일시적인 작업을 해야하는 경우 <span style="color:lightpink; font-weight:bold;">Controller</span>가 필요한 순간에만 <span style="color:lightpink; font-weight:bold;">Pod</span>를 만들어서 해당 작업을 이행하고 삭제.

**👍 추가로, Replication Controller 는 ReplicaSet의 노후화된 버전이라고 합니다!**
***

### 🚀 Template, Replicas

<span style="color:lightpink; font-weight:bold;">Template</span>
- <span style="color:lightpink; font-weight:bold;">Controller</span>와 <span style="color:lightpink; font-weight:bold;">Pod</span>는 <span style="color:lightpink; font-weight:bold;">Label</span>과 <span style="color:lightpink; font-weight:bold;">Selector</span>로 연결
- <span style="color:lightpink; font-weight:bold;">Controller</span>를 만들 때 <span style="color:lightpink; font-weight:bold;">Template</span>으로 <span style="color:lightpink; font-weight:bold;">Pod</span>를 지정
- <span style="color:lightpink; font-weight:bold;">Pod</span>가 다운되면 <span style="color:lightpink; font-weight:bold;">Template</span>을 통해 <span style="color:lightpink; font-weight:bold;">Pod</span>를 새로 만들 수 있읍.

<span style="color:lightpink; font-weight:bold;">Replicas</span>
- <span style="color:lightpink; font-weight:bold;">Controller</span> 생성 시, 숫자를 정할 수 있음.
- 정해진 숫자만큼 <span style="color:lightpink; font-weight:bold;">Pod</span>의 수를 유지.

**이제, 실습해보도록 합시다!**

**pod1.yaml**

```json
apiVersion: v1
kind: Pod
metadata:
  name: pod-1
  labels:
    type: web
spec:
  containers:
  - name: container
    image: kubetm/app:v1
  terminationGracePeriodSeconds: 0
```

<img width="70%" alt="스크린샷 2022-03-16 오후 5 45 03" src="https://user-images.githubusercontent.com/56334761/158551119-3842c281-5ef1-42c3-b6f3-e8f7dfbde007.png">

**rs1.yaml**

```json
apiVersion: apps/v1
kind: ReplicaSet
metadata:
  name: replica1
spec:
  replicas: 1
  selector:
    matchLabels:
      type: web
  template:
    metadata:
      name: pod1
      labels:
        type: web
    spec:
      containers:
      - name: container
        image: kubetm/app:v1
      terminationGracePeriodSeconds: 0
```

<img width="70%" alt="스크린샷 2022-03-16 오후 5 48 57" src="https://user-images.githubusercontent.com/56334761/158551832-8d6e9db5-a745-498c-964b-e9b3a206f5e0.png">

이제 ReplicaSet에 연결된 Pod를 확인해 봅시다.

<img width="1440" alt="스크린샷 2022-03-16 오후 5 54 32" src="https://user-images.githubusercontent.com/56334761/158552916-0469dc94-7aad-423f-a4a3-7634206e4bfe.png">

**pod1이 잘 연결 된것을 볼 수 있습니다.**

**이제, replicas의 수를 늘려보도록 하겠습니다!**

`kubectl scale rs/replica1 --replicas=2`

<img width="70%" alt="스크린샷 2022-03-16 오후 5 56 54" src="https://user-images.githubusercontent.com/56334761/158553296-86c8a057-6800-4840-8293-6d21080ecbca.png">

<img width="1440" alt="스크린샷 2022-03-16 오후 5 57 36" src="https://user-images.githubusercontent.com/56334761/158553415-60bb2b59-f1b8-4113-a3f2-7cfdc6a8ea10.png">

**새로운 Pod가 생성된 것을 확인할 수 있습니다!**

**이제 Pod의 Version을 업데이트 하기 위해 ReplicaSet의 Template의 Version을 바꾼 후, 기존의 Pod를 삭제해보도록 하겠습니다.**

`kubectl edit rs/replica1 -o yaml`

<img width="70%" alt="스크린샷 2022-03-16 오후 6 02 19" src="https://user-images.githubusercontent.com/56334761/158554355-3c5d8555-5fbf-46fa-950c-198f3be95ea1.png">

`kubectl delete pods --all`

<img width="1440" alt="스크린샷 2022-03-16 오후 6 04 07" src="https://user-images.githubusercontent.com/56334761/158554685-037b1a67-fc4b-4ca9-998e-1ce7bb34886a.png">

**Pod들의 Version이 수정된 것을 확인할 수 있습니다!**

***

### 🚀 Selector

<span style="color:lightpink; font-weight:bold;">ReplicaSet</span>의 <span style="color:lightpink; font-weight:bold;">Selector</span>에는 <span style="color:lightpink; font-weight:bold;">Key</span>와 <span style="color:lightpink; font-weight:bold;">labels</span>형태인 <span style="color:lightpink; font-weight:bold;">Selector</span> 만 아닌,

<span style="color:lightpink; font-weight:bold;">matchExpressions</span>라는 <span style="color:lightpink; font-weight:bold;">Selector</span>로 이미 존재하는 <span style="color:lightpink; font-weight:bold;">Object</span>를 좀 더 세세하게 정해줄 수 있다고 합니다.

하지만, <span style="color:lightpink; font-weight:bold;">ReplicaSet</span>은 위에서 말했다 시피 <span style="color:lightpink; font-weight:bold;">Template</span>을 따로 정해주기 때문에 <span style="color:lightpink; font-weight:bold;">Selector</span>라는 기능 자체를 잘 사용하지는 않다고 합니다.

따라서 별도의 실습없이 <span style="color:lightpink; font-weight:bold;">yaml파일</span>만 정의하는 법을 간단히 확인해봅시다!

```json
apiVersion: apps/v1
kind: ReplicaSet
metadata:
  name: replica1
spec:
  replicas: 1
  selector:
    matchLabels:
      type: web
      ver: v1
    matchExpressions:
    - {key: type, operator: In, values: [web]}
    - {key: ver, operator: Exists}
  template:
    metadata:
      labels:
        type: web
        ver: v1
        location: dev
    spec:
      containers:
      - name: container
        image: kubetm/app:v1
      terminationGracePeriodSeconds: 0
```

저런 식으로, <span style="color:lightpink; font-weight:bold;">matchExpressions</span>를 정의해주면 된다고 합니다!

***
### <span style="color:lightpink; font-weight:bold;">이상으로 마치겠습니다. 🙋🏻‍♂️</span>