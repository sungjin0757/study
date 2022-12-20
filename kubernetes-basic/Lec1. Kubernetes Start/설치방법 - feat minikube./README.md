## Kubernetes 설치 - feat minikube.

***
### 🚀 쿠버네티스 환경 구성 방법

일반적으로 쿠버네티스 실행 환경을 구성하기 위해서는 Master Node 1대와 Work Node 여러 대를 이용하여 클러스터 환경을 구성합니다.

Cluster 환경을 구성하기 위해서는 일반적으로는 다음과 같은 방법을 많이 사용하게 됩니다.

1. Amazon Web Service - EKS
2. Google Cloud Platform - GKE
3. Azure - AKS
4. Local환경에서 VM을 여러 대 사용하는 방법.

위의 1,2,3번의 방식은 어느정도의 과금을 사용하여야 합니다.

4번의 방식을 사용하기에는 제가 사용하고 있는 PC가 M1 Mac이기 때문에, 가상 머신을 띄우는 방법이 여의치 않았습니다.

따라서 로컬에서 쿠버네티스를 테스트해볼 수 있는 **단일 노드 클러스터**인 <span style="color:lightpink; font-weight:bold;">minikube</span>를 설치해 보겠습니다.

***
### 🚀  minikube

minikube는 K8S 가상 Runtime 환경 제공을 위해 <span style="color:lightpink; font-weight:bold;">컨테이너 엔진</span>이 필요합니다. 

따라서, <span style="color:lightpink; font-weight:bold;">Docker</span>와 같은 컨테이너 엔진이 먼저 설치 되어 있어야 합니다!

> minikube 설치 바로가기 : https://minikube.sigs.k8s.io/docs/start/

저는 현재 Mac OS를 사용하고 있기 때문에, Homebrew를 통해 더욱 간단하게 다운로드 받을 수 있었습니다.

`brew install minikube`

<img width="70%" alt="1" src="https://user-images.githubusercontent.com/56334761/155329242-780b1449-2eef-43ad-a37a-ecd74a826037.png">

설치가 완료되었다면, minikube로 클러스터를 시작해 보도록 하겠습니다.

`minikube start`

<img width="70%" alt="스크린샷 2022-02-23 오후 10 37 29" src="https://user-images.githubusercontent.com/56334761/155330316-313510ee-a962-48c7-8af1-fbea0e892247.png">

정상적인 설치가 되었는지 확인 해보기 위해,

1. minikube 버전 조회
2. minikube의 상태 정보를 조회
3. 대시보드 실행
4. 클러스터 정상 작동 여부

를 해보도록 하겠습니다.

`minikube version`

<img width="70%" alt="스크린샷 2022-02-23 오후 10 45 08" src="https://user-images.githubusercontent.com/56334761/155331532-ccda66e7-f8b2-4a04-9b1e-3457379cc258.png">

**1.25.1버전이 조회되는 것을 볼 수 있습니다.**

`minikube status`

<img width="70%" alt="스크린샷 2022-02-23 오후 10 46 48" src="https://user-images.githubusercontent.com/56334761/155331783-0fde75b3-9f37-4d27-83d9-1ec85888c217.png">

**정상 실행 중임을 확인할 수 있습니다.**

`minikube dashboard`

<img width="70%" alt="스크린샷 2022-02-23 오후 10 48 26" src="https://user-images.githubusercontent.com/56334761/155332084-41097033-ef12-4d86-963e-1ef96f89bbc7.png">

해당 <span style="color:lightpink; font-weight:bold;"> URL</span>을 타고 들어가면 이와 같은 Dashboard를 확인할 수 있습니다.
![스크린샷 2022-02-23 오후 10 50 07](https://user-images.githubusercontent.com/56334761/155332383-01f7c3bb-ee13-46fa-9dc0-4e50d1cfe9f2.png)

`kubectl cluster-info`

<img width="70%" alt="스크린샷 2022-02-23 오후 10 51 30" src="https://user-images.githubusercontent.com/56334761/155332622-b1b40fc5-4368-4172-a528-d5eb98966266.png">

**클러스터가 정상작동하고 있음을 확인할 수 있습니다.**

***

### <span style="color:lightpink; font-weight:bold;"> 🙋🏻‍♂️ 쿠버네티스 실행환경을 위한 minikube 설치 방법에 대하여 알아 보았습니다. 감사합니다!</span> ###