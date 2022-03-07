## Namespace, ResourceQuota, LimitRange - Kubernetes

***

>이 글은 김태민님의 대세는 쿠버네티스 강의를 참고하여 정리하였습니다!
>
>출처 : https://www.inflearn.com/course/%EC%BF%A0%EB%B2%84%EB%84%A4%ED%8B%B0%EC%8A%A4-%EA%B8%B0%EC%B4%88/dashboard
***

### 🔍 테스트해 볼 내용
1. <span style="color:lightpink; font-weight:bold;">Namespace</span> 구성
2. <span style="color:lightpink; font-weight:bold;">ResourceQuota</span> 구성
3. <span style="color:lightpink; font-weight:bold;">LimitRange</span> 구성

 >도커 이미지는 김태민님께서 만들어두신 이미지를 사용합니다.

 ***
 ### 🤔 Why Use?

먼저 이와 같은 기술들은 공통된 한 공간에서 독립적인 공간으로 분리하기 위해서 만듭니다. 예를 들어, 한 <span style="color:lightpink; font-weight:bold;">Node</span>에는 여러 공간의 <span style="color:lightpink; font-weight:bold;">Namespace</span>가 존재할 수 있으며 <span style="color:lightpink; font-weight:bold;">Namespace</span>안의 <span style="color:lightpink; font-weight:bold;">Pod</span>들은 다른 <span style="color:lightpink; font-weight:bold;">Namespace</span>의 존재 자체를 모르게 되죠.. 물론, Namespace로만 분리되지 않는 것이 있습니다.

또한, 여러 <span style="color:lightpink; font-weight:bold;">Namespace</span>에는 여러 개의 <span style="color:lightpink; font-weight:bold;">Pod</span>를 만들 수 있습니다. 각 <span style="color:lightpink; font-weight:bold;">Pod</span>는 <span style="color:lightpink; font-weight:bold;">Cpu</span>와 <span style="color:lightpink; font-weight:bold;">Memory</span>같은 공유 자원을 사용하게 되는데, 한 <span style="color:lightpink; font-weight:bold;">Pod</span> 혹은 한 <span style="color:lightpink; font-weight:bold;">Namespace</span>에서 공유자원을 독차지하면 안되겠죠!

**그래서 이번에 해볼것은 어떤 방식으로 자원을 할당할 수 있을지 실험해보려고 합니다!**

***

### 🚀 Namespace

<span style="color:lightpink; font-weight:bold;">Namespace</span>에서 같은 타입의 <span style="color:lightpink; font-weight:bold;">Object</span>들은 이름이 중복될 수 없습니다. <span style="color:lightpink; font-weight:bold;">Pod</span>마다 이름이 달라야하는 것과 같은 이유입니다.

즉, <span style="color:lightpink; font-weight:bold;">Object</span>마다 별도의 <span style="color:lightpink; font-weight:bold;">uuid</span>가 존재한다고 하여도, 같은 <span style="color:lightpink; font-weight:bold;">Object</span>끼리는 이름 또한 <span style="color:lightpink; font-weight:bold;">uuid</span>의 역할을 한다고 볼 수 있습니다.

또한, <span style="color:lightpink; font-weight:bold;">Namespace</span>의 대표적인 특징은 타 <span style="color:lightpink; font-weight:bold;">Namespace</span>와 분리가 된다는 것입니다.

**지금부터, Namespace를 구성하고 Pod를 만들어봅시다!**

**ns1.yaml**

```json
apiVersion: v1
kind: Namespace
metadata:
  name: ns-1
```

`kubectl create -f ./ns1.yaml`

`kubectl apply -f ./ns1.yaml`

<img width="70%" alt="스크린샷 2022-03-07 오후 9 38 08" src="https://user-images.githubusercontent.com/56334761/157036030-204696e2-5312-4b95-b828-2b4a302ffb2f.png">

<img width="70%" alt="스크린샷 2022-03-07 오후 9 38 08" src="https://user-images.githubusercontent.com/56334761/157036263-1be36bf5-32ab-4d43-8b25-f762d8b08d21.png">

**Namespace가 만들어진 것을 확인하였습니다!**

**pod1.yaml**

```json
apiVersion: v1
kind: Pod
metadata:
  name: pod-1
  namespace: ns-1
  labels:
    app: pod
spec:
  containers:
  - name: container
    image: tmkube/app
    ports:
    - containerPort: 8080
```

`kubectl create -f ./pod1.yaml`

`kubectl apply -f ./pod1.yaml`

<img width="70%" alt="스크린샷 2022-03-07 오후 9 43 34" src="https://user-images.githubusercontent.com/56334761/157036958-80d04bc1-ca06-4a17-9e57-47991ca2fa49.png">

**만들어둔 Namespace에 Pod가 만들어진 것을 확인하였습니다!**

지금부터는 
1. Service를 만들어봅시다.
2. Namespace를 하나 더 만들어 봅시다.
3. 어떤 것들이 분리되는지 대략 알아봅시다.
4. 분리되지 않는 것들을 알아봅시다.

이 순대로 실험을 진행해 보겠습니다.

**service1.yaml**

```json
apiVersion: v1
kind: Service
metadata:
  name: svc-1
  namespace: ns-1
spec:
  selector:
    app: pod
  ports:
  - port: 9000
    targetPort: 8080
```

`kubectl create -f ./service1.yaml`

`kubectl apply -f ./service1.yaml`

<img width="70%" alt="스크린샷 2022-03-07 오후 9 56 25" src="https://user-images.githubusercontent.com/56334761/157038721-0cd50332-ccc4-40ee-ab08-75bb08a5a53b.png">

Service또한 만들었습니다.

**Namespace를 하나 더 만듭시다. 위의 과정에서 이름만 다르게 하면됩니다!**

<img width="70%" alt="스크린샷 2022-03-07 오후 10 00 18" src="https://user-images.githubusercontent.com/56334761/157039256-af347ffa-d07c-41c3-8b35-d9b504f25d4f.png">

전 ns-2라는 이름의 namespace를 만들었습니다.

**그렇다면, 분리되는 자원으로는 무엇이 있을지 확인해 봅시다!**

**ns-1에 같은 이름의 Pod를 하나 더 만들어 봅시다! 아까 만들어두었던 pod1.yaml을 사용하면 됩니다.**


<img width="70%" alt="스크린샷 2022-03-07 오후 10 02 39" src="https://user-images.githubusercontent.com/56334761/157039540-a3c2a265-f182-4a68-bae8-48c440f63939.png">

**처음 부분에서 말했던것과 시피 이름이 중복되면 안된다는 것을 확인할 수 있습니다!**

**이제 ns-2에 service를 하나 더 만들어봅시다! service1.yaml에 namespace만 ns-2로 바꾸면 됩니다.**

<img width="70%" alt="스크린샷 2022-03-07 오후 10 11 57" src="https://user-images.githubusercontent.com/56334761/157041067-2bec1ca4-cdbe-4e0e-bd69-afc95a3a7572.png">

**위의 그림에서 확인할 수 있는 것은 분명 똑같은 이름의 service인데 ns-2에서는 만들어진 것을 확인할 수 있습니다. 즉 Namespace는 Object를 분리시킬 수 있다는 것을 알 수 있습니다!**

**그에 대한 확인으로 대시보드를 확인해보도록 합시다!**

<img width="1440" alt="스크린샷 2022-03-07 오후 10 16 36" src="https://user-images.githubusercontent.com/56334761/157041639-496fac90-57b8-4444-b8c3-de8ad526a279.png">

**ns-1에서의 service는 Pod가 연결되었고, ns-2에서는 Pod가 연결되지 않은 것을 볼 수 있습니다**

**지금 부터는 분리되지 않는 자원에 대해서 확인 해 봅시다!**

**ns-1과 ns-2에서의 각각의 service 와 pod의 IP를 알아봅시다.**

**이제 ns-2의 pod에 접속해 보도록 하겠습니다!**

<img width="70%" alt="스크린샷 2022-03-07 오후 11 02 25" src="https://user-images.githubusercontent.com/56334761/157048850-168f0005-8bc8-4015-ac26-f861598e59d9.png">


`kubectl exec pod-1 -it /bin/bash`

curl 명령어를 통해 ns-1의 pod와 ns-2의 pod에 모두 접근해보도록 하겠습니다!

`curl 172.17.0.6:8080/hostname`

`curl 172.17.0.5:8080/hostname`

<img width="70%" alt="스크린샷 2022-03-07 오후 11 05 03" src="https://user-images.githubusercontent.com/56334761/157049171-df0ec1f2-c68e-4b08-854e-b02157e2a4da.png">

**보시는바와 같이 ns-2에서의 pod에서도 ns-1에서의 pod에 접근 가능한 것을 볼 수 있습니다. 이 말은 즉, IP 접속은 분리 되지 않는 다는 것을 볼 수 있습니다!**

**이제 각각의 namespace에 HostPath Volume Mount를 하는 pod들을 만들어 보도록 합시다!**

**ns-1 pod3.yaml**

```json
apiVersion: v1
kind: Pod
metadata:
  name: pod-2
  namespace: ns-1
spec:
  nodeSelector:
    kubernetes.io/hostname: minikube
  containers:
  - name: container
    image: tmkube/init
    volumeMounts:
    - name: host-path
      mountPath: /mount1
  volumes:
  - name: host-path
    hostPath:
      path: /node-v
      type: DirectoryOrCreate
```

**ns-2 pod4.yaml**

```json
apiVersion: v1
kind: Pod
metadata:
  name: pod-2
  namespace: ns-2
spec:
  nodeSelector:
    kubernetes.io/hostname: minikube
  containers:
  - name: container
    image: tmkube/init
    volumeMounts:
    - name: host-path
      mountPath: /mount1
  volumes:
  - name: host-path
    hostPath:
      path: /node-v
      type: DirectoryOrCreate
```

이제 ns-1의 pod에서 volume에 파일을 하나 만들고 ns-2의 pod에서 그 파일이 존재하나 확인해 봅시다.

<img width="70%" alt="스크린샷 2022-03-07 오후 11 14 48" src="https://user-images.githubusercontent.com/56334761/157050886-144df81c-ca2b-4f63-acd3-2b19bba10e88.png">

**빨간 박스는 namespace변경 부분을 의미합니다.**

**이것 또한, 보시는 바와 같이 Hostpath형태로 mount된 volume은 공유된다는 것을 볼 수 있습니다!**

정리하자면,
- **분리되는 것**
  1. Object
- **분리되지 않는 것**
  1. IP주소 접근
  2. HostPath 형태의 Volume    
