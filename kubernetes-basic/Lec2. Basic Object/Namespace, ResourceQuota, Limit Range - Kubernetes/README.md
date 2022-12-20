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

***

### 🚀 ResourceQuota

<span style="color:lightpink; font-weight:bold;">Namespace</span>의 <span style="color:lightpink; font-weight:bold;">Pod</span>들이 사용하는 <span style="color:lightpink; font-weight:bold;">공유 자원</span>에 대한 자원 한계를 달아 놓는 것입니다.

사용 방법은 <span style="color:lightpink; font-weight:bold;">Pod</span>를 만들 때, <span style="color:lightpink; font-weight:bold;">Memory</span>의 <span style="color:lightpink; font-weight:bold;">requests</span>와 <span style="color:lightpink; font-weight:bold;">limits</span>를 정해 놓으면 됩니다.

**이제 실습한번 해봅시다!**

**ns3.yaml**

```json
apiVersion: v1
kind: Namespace
metadata:
  name: ns-3
```

**rq.yaml**

```json
apiVersion: v1
kind: ResourceQuota
metadata:
  name: rq-1
  namespace: ns-3
spec:
  hard:
    requests.memory: 1Gi
    limits.memory: 1Gi
```

**ResourceQuota가 잘 만들어 졌는지 확인해봅시다!**

`kubectl describe resourcequotas --namespace=ns-3`

<img width="70%" alt="스크린샷 2022-03-15 오후 7 04 43" src="https://user-images.githubusercontent.com/56334761/158354324-066eb9b4-afc0-4add-82cd-6ecb360abf0c.png">

**이제 Pod를 생성해보도록 합시다!**
**pod3.yaml**

```json
apiVersion: v1
kind: Pod
metadata:
  name: pod-3
  namespace: ns-3
spec:
  containers:
  - name: container
    image: kubetm/app
```

<img width="70%" alt="스크린샷 2022-03-15 오후 7 07 38" src="https://user-images.githubusercontent.com/56334761/158354823-df13d712-30e1-40c8-a93b-9ca23eddc9a8.png">

**오류가 뜨게 됩니다.**

**이렇게 오류가 뜨는 이유는 namespace상에 resourcequota가 정의 되어 있는데 limits와 requests가 명시되어 있지 않기 때문입니다.**

**pod3의 내용을 수정하도록 합시다**

```json
apiVersion: v1
kind: Pod
metadata:
  name: pod-3
  namespace: ns-3
spec:
  containers:
  - name: container
    image: kubetm/app
    resources:
      requests:
        memory: 0.5Gi
      limits:
        memory: 0.5Gi
```

<img width="70%" alt="스크린샷 2022-03-15 오후 7 14 17" src="https://user-images.githubusercontent.com/56334761/158355888-8091c46f-2af7-4057-8af5-ed3df78a961a.png">

**Pod가 잘 생성된 것을 확인 하였으며, 메모리 사용량 또한 올라간 것을 확인할 수 있습니다!**

**그럼, 이제 memory의 사용량이 현재 ResourceQuota를 넘는 Pod를 만들어 봅시다!**


**pod4.yaml**

```json
apiVersion: v1
kind: Pod
metadata:
  name: pod-4
  namespace: ns-3
spec:
  containers:
  - name: container
    image: kubetm/app
    resources:
      requests:
        memory: 0.8Gi
      limits:
        memory: 0.8Gi
```

<img width="571" alt="스크린샷 2022-03-15 오후 7 18 26" src="https://user-images.githubusercontent.com/56334761/158356634-615ab807-8514-4f9d-82ea-76b9f390038f.png">

**한계 사용량을 뛰어넘어 pod가 생성되지 않는 것을 확인하였습니다!**

**👍 ResourceQuota는 Pod의 수도 제한 할 수 있습니다**

**rq2.yaml**

```json
apiVersion: v1
kind: ResourceQuota
metadata:
  name: rq-2
  namespace: ns-3
spec:
  hard:
    pods: 1
```

<img width="70%" alt="스크린샷 2022-03-15 오후 7 28 48" src="https://user-images.githubusercontent.com/56334761/158358342-3364eda8-bc5c-4b75-9c14-c0a8f43f53de.png">

**이제 pod를 하나 더 만들어 보도록 합시다!**

**pod5.yaml**

```json
apiVersion: v1
kind: Pod
metadata:
  name: pod-5
  namespace: ns-3
spec:
  containers:
  - name: container
    image: kubetm/app
    resources:
      requests:
        memory: 0.1Gi
      limits:
        memory: 0.1Gi
```

<img width="70%" alt="스크린샷 2022-03-15 오후 7 30 20" src="https://user-images.githubusercontent.com/56334761/158358597-e6cbcbe1-45b5-478e-9648-c3d0570b576e.png">

**잘 만들어지지 않는 것을 확인할 수 있습니다!**

***

### 🚀 LimitRange

각각의 <span style="color:lightpink; font-weight:bold;">Pod</span>마다 <span style="color:lightpink; font-weight:bold;">Namespace</span>에 들어올 수 있는지 자원을 체크하여, <span style="color:lightpink; font-weight:bold;">Pod</span>마다 세세한 조건을 둘 수 있습니다.

**이제 실습을 해보도록 합시다!**

**ns4.yaml**

```json
apiVersion: v1
kind: Namespace
metadata:
  name: ns-4
```

<img width="70%" alt="스크린샷 2022-03-15 오후 7 42 47" src="https://user-images.githubusercontent.com/56334761/158360663-8f816bd9-3ebe-4646-b821-320fed5dabd2.png">

**lr.yaml**

```json
apiVersion: v1
kind: LimitRange
metadata:
  name: lr-1
  namespace: ns-4
spec:
  limits:
  - type: Container
    min:
      memory: 0.1Gi
    max:
      memory: 0.4Gi
    maxLimitRequestRatio:
      memory: 3
    defaultRequest:
      memory: 0.1Gi
    default:
      memory: 0.2Gi
```

`kubectl describe limitranges --namespace=ns-4`

<img width="70%" alt="스크린샷 2022-03-15 오후 7 50 39" src="https://user-images.githubusercontent.com/56334761/158362015-145da163-67ce-42d4-a92e-6965b05875fe.png">

**잘 만들어 졌습니다**

이제 옵션들에 대해서 설명해 봅시다.
1. min : Pod에서 설정되는 Memory의 Limit 값이 이 min을 넘어야 한다는 것을 뜻합니다.
2. max : Pod에서 설정되는 Memory의 Limit 값이 이 max를 넘어서는 안됩니다.
3. maxLimitRequestRatio : request값과 limit값의 비율의 최대 값을 뜻합니다.
4. defaultRequest : Pod에 아무런 값을 설정 하지 않았을 때 자동으로 설정되는 request값 입니다.
5. default : Pod에 아무런 값을 설정 하지 않았을 때 자동으로 설정 되는 Limit 값 입니다.

**이제, 2개의 Pod를 만들어 실습해 보도록 하겠습니다.**
1. request와 limit을 설정하지 않은 Pod
2. maxLimitRequestRatio의 비율을 넘어선 Pod

**pod6.yaml**

```json
apiVersion: v1
kind: Pod
metadata: 
  name: pod-6
  namespace: ns-4
spec:
  containers:
  - name: container
    image: kubetm/app
```

<img width="70%" alt="스크린샷 2022-03-15 오후 7 59 31" src="https://user-images.githubusercontent.com/56334761/158363529-98228db4-9270-491d-9f5f-c83b12ed8dd2.png">

만들어진 pod의 yaml에 들어가 봅시다!

`kubectl edit pod pod-6 -o yaml`

<img width="70%" alt="스크린샷 2022-03-15 오후 8 02 33" src="https://user-images.githubusercontent.com/56334761/158364157-f9173eaf-af5b-416b-af63-9175b1962433.png">

자동으로 request, limit이 설정된 것을 확인 하였습니다.

**pod7.yaml**

```json
apiVersion: v1
kind: Pod
metadata: 
  name: pod-7
  namespace: ns-4
spec:
  containers:
  - name: container
    image: kubetm/app
    resources:
      requests:
        memory: 0.1Gi
      limits:
        memory: 0.4Gi
```

request와 limit의 비율이 4배인 pod를 생성해보도록 합시다.

<img width="70%" alt="스크린샷 2022-03-15 오후 8 07 00" src="https://user-images.githubusercontent.com/56334761/158365003-bdf1cc41-2654-498d-9c8c-d9066a452006.png">

**Pod가 생성되지 못한 모습을 볼 수 있습니다!**

**실습을 마치도록 하겠습니다.**

***
### <span style="color:lightpink; font-weight:bold;">이상으로 마치겠습니다. 🙋🏻‍♂️</span>