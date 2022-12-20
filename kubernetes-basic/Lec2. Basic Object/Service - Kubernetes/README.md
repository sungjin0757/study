## Service - Cluster IP, NodePort, Load Balancer
***
>이 글은 김태민님의 대세는 쿠버네티스 강의를 참고하여 정리하였습니다!
>
>출처 : https://www.inflearn.com/course/%EC%BF%A0%EB%B2%84%EB%84%A4%ED%8B%B0%EC%8A%A4-%EA%B8%B0%EC%B4%88/dashboard
***

### 🔍 테스트 해볼 내용
**Service Object의 Option에 대해서 배워 볼게요!**
1. <span style="color:lightpink; font-weight:bold;">Cluster Ip</span> 구성
2. <span style="color:lightpink; font-weight:bold;">NodePort</span> 구성
3. <span style="color:lightpink; font-weight:bold;">Load Balancer</span> 구성

 >도커 이미지는 김태민님께서 만들어두신 이미지를 사용합니다.
***

### 🚀 Service

먼저 <span style="color:lightpink; font-weight:bold;">Pod</span>만을 사용하지 않고 <span style="color:lightpink; font-weight:bold;">Service</span>를 사용하는 이유를 알아 봅시다.

<span style="color:lightpink; font-weight:bold;">Pod</span>라는 것은 <span style="color:lightpink; font-weight:bold;">Kubernetes</span>환경에서 다양한 요인으로 인해 언제든지 고장날 수 있습니다.

그러면 추후에 알아볼 <span style="color:lightpink; font-weight:bold;">Controller</span>등에 의해서 <span style="color:lightpink; font-weight:bold;">Pod</span>를 재생성하게 되는데요.

재생성된 <span style="color:lightpink; font-weight:bold;">Pod</span>는 <span style="color:lightpink; font-weight:bold;">Ip</span>의 할당이 달라지게 됩니다. 즉, 신뢰성이 떨어지게 되는 것이죠.

그에 반해서 <span style="color:lightpink; font-weight:bold;">Service</span>는 사용자가 직접 삭제하지 않는 한 지워지지 않습니다.

그러므로, <span style="color:lightpink; font-weight:bold;">Service</span>와 함께 구성된 <span style="color:lightpink; font-weight:bold;">Pod</span>는 더욱 신뢰성을 가지며, 클라이언트는 <span style="color:lightpink; font-weight:bold;">Service</span>로 접근하여 <span style="color:lightpink; font-weight:bold;">Pod</span>에 접근 가능하도록 만들어줍니다.

**지금부터, 실습해볼 것들은 외부또는 클러스터 내에서 클라이언트들이 Service Object에 접근 가능하도록하는 Option에 대해서 배워봅시다!**

***

### 🚀 Cluster IP

이름에서도 보이다시피, 외부에서는 접근 불가하고 <span style="color:lightpink; font-weight:bold;">Cluster</span>내에서만 접근 가능한 <span style="color:lightpink; font-weight:bold;">Object</span>입니다.

<span style="color:lightpink; font-weight:bold;">Pod</span>를 여러 개 연결 시킬 수 있으며, 여러 개의 <span style="color:lightpink; font-weight:bold;">Pod</span>를 연결 했을 때, 서비스가 <span style="color:lightpink; font-weight:bold;">Traffic</span>을 분산 시켜서 <span style="color:lightpink; font-weight:bold;">Pod</span>에 전달하게 됩니다.

**이제, Pod와 Service를 만들어 작동시켜 봅시다!**

**Pod1.yaml**

```json
apiVersion: v1
kind: Pod
metadata:
  name: pod-1
  labels:
     app: pod
spec:
  nodeSelector:
    kubernetes.io/hostname: minikube
  containers:
  - name: container
    image: kubetm/app
    ports:
    - containerPort: 8080
    
```

**Service1.yaml**

```json
apiVersion: v1
kind: Service
metadata:
  name: svc-1
spec:
  selector:
    app: pod
  ports:
  - port: 9000
    targetPort: 8080

```

`kubectl create -f ./pod1.yaml`

`kubectl apply -f ./pod1.yaml`

`kubectl create -f ./service1.yaml`

`kubectl apply -f ./service1.yaml`

<img width="70%" alt="스크린샷 2022-02-28 오후 8 41 42" src="https://user-images.githubusercontent.com/56334761/155977764-63f89980-da55-4f15-8bfb-c05f06eb655d.png">


**이제 pod와 service가 잘 생성되었는지 확인해 봅시다.**

`kubectl get nodes -o wide`

`kubectl get services -o wide`

<img width="70%" alt="스크린샷 2022-02-28 오후 8 47 03" src="https://user-images.githubusercontent.com/56334761/155978420-9b1e3b70-f042-4edc-bce2-ffbeb40092ad.png">

**현재 pod의 IP와 service의 IP를 잘 기억해 놓읍시다.**

**이제 cluster에 접근하여 service와 pod가 잘 연결되어 있는지 확인 하여 봅시다!**

**👍 curl명령어를 통하여 service의 IP로 접근하면 pod의 hostname이 출력되게 끔 하였습니다.**

`minikube ssh`

`curl 10.99.234.249:9000/hostname`

<img width="70%" alt="스크린샷 2022-02-28 오후 8 56 08" src="https://user-images.githubusercontent.com/56334761/155980033-d6e3f39e-3ec5-4631-8caa-7ce2c25d48f5.png">

**이렇게 Cluster IP에 관한 실습은 마치겠습니다.**

***

### 🚀 Node Port

이 <span style="color:lightpink; font-weight:bold;">Option</span>에서도 기본적으로 <span style="color:lightpink; font-weight:bold;">Cluster IP</span>가 할당 됩니다.

하지만 차이점은 <span style="color:lightpink; font-weight:bold;">Cluster IP</span>는 <span style="color:lightpink; font-weight:bold;">Pod</span>들과 <span style="color:lightpink; font-weight:bold;">Service</span>의 연결이었다면, 이 <span style="color:lightpink; font-weight:bold;">Option</span>에서는 <span style="color:lightpink; font-weight:bold;">Node</span>단위로 <span style="color:lightpink; font-weight:bold;">Service</span>와의 연결이라는 점입니다.

<span style="color:lightpink; font-weight:bold;">Kubernetes 클러스터</span>에 연결되어있는 모든 <span style="color:lightpink; font-weight:bold;">Node</span>에 똑같은 <span style="color:lightpink; font-weight:bold;">Port</span>가 할당되어서 <span style="color:lightpink; font-weight:bold;">해당 Node , 해당 Port</span>로 접속하게 되면, <span style="color:lightpink; font-weight:bold;">Service</span>로 접근이 되고, <span style="color:lightpink; font-weight:bold;">Service</span>는 자신에게 연결되어 있는 <span style="color:lightpink; font-weight:bold;">Pod</span>에게 <span style="color:lightpink; font-weight:bold;">Traffic</apsn>을 전달하는 방법입니다.

즉, <span style="color:lightpink; font-weight:bold;">Service</span>는 어떤 노드에게 온 <span style="color:lightpink; font-weight:bold;">Traffic</span>이든 상관 없이 <span style="color:lightpink; font-weight:bold;">Pod</span>들에게 <span style="color:lightpink; font-weight:bold;">Traffic</span>을 전달할 수 있게 되는 것입니다.

**이제 직접 실습을해 보도록 합시다!**

Pod는 기존 생성된 것을 사용하겠습니다.

**Service2.yaml**

```json
apiVersion: v1
kind: Service
metadata:
  name: svc-2
spec:
  selector:
    app: pod
  ports:
  - port: 9000
    targetPort: 8080
    nodePort: 30000
  type: NodePort
  externalTrafficPolicy: Local
```

**externalTrafficPolicy가 Local로 설정되어 있으면, 접근한 Node의 Pod에만 Traffic을 전달하게 됩니다.**

`kubectl create -f ./service2.yaml`

`kubectl apply -f ./service2.yaml`

<img width="70%" alt="스크린샷 2022-02-28 오후 9 17 40" src="https://user-images.githubusercontent.com/56334761/155982246-9025cc5b-7c98-4dfd-869b-479fab4e2d7b.png">

**svc-2가 생성된 것을 확인할 수 있습니다!**

**여기서 9000번 포트는 ClusterIP 를 의미하고 30000번 포트는 NodePort입니다.**

**이제 해당 node의 IP를 알아보도록 합시다.**

`kubectl get node -o wide`

<img width="70%" alt="스크린샷 2022-02-28 오후 9 20 18" src="https://user-images.githubusercontent.com/56334761/155982607-22e546de-1800-4b6c-99eb-aeb6ca7e5534.png">

**먼저 클러스터 내로 접근하여 node로 접근해보도록 합시다!**

`minikube ssh`

`curl {clusterIP}:9000/hostname`

`curl {nodeIP}:30000/hostname`

<img width="427" alt="스크린샷 2022-02-28 오후 9 30 52" src="https://user-images.githubusercontent.com/56334761/155984022-8749f56c-c10d-44d2-afe3-c3583aab8e1b.png">

**기본적인 Cluster IP와 Node Port둘 다 잘 접근되는 것을 볼 수 있습니다!**

**이렇게 NodePort에 관한 실습은 마치겠습니다.**

***

### 🚀 Load Balancer

<span style="color:lightpink; font-weight:bold;">NodePort</span>의 성격을 그대로 가집니다.
각각의 <span style="color:lightpink; font-weight:bold;">Node</span>에 <span style="color:lightpink; font-weight:bold;">Traffic</span>을 분산시켜주는 역할을 하며, <span style="color:lightpink; font-weight:bold;">Load Balancer</span>에 접근하는 외부 접속 <span style="color:lightpink; font-weight:bold;">IP</span>는 별도로 할당합니다.

실제 운영환경에서는, <span style="color:lightpink; font-weight:bold;">NodePort</span>가 아닌 <span style="color:lightpink; font-weight:bold;">Load Balancer</span>를 사용해야합니다. 그러한 이유는 <span style="color:lightpink; font-weight:bold;">Node</span>의 직접적인 <span style="color:lightpink; font-weight:bold;">IP</span>를 노출하게 되면 보안 상 위험이 생길 수 있기 때문입니다!

<span style="color:lightpink; font-weight:bold;">Load Balancer</span>는 실습을 하기 위해서는 추가 모듈이 따로 필요하다고 합니다.

실습은 따로 진행하지는 않도록 하겠습니다.

***
### <span style="color:lightpink; font-weight:bold;">이상으로 마치겠습니다. 🙋🏻‍♂️</span>