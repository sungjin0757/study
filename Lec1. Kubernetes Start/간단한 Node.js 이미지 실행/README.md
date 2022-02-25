## 쿠버네티스 환경에서 간단한 Node.js 이미지 실행해 보기 - feat Minikube.

***
### 🔍 테스트 방법

1. <span style="color:lightpink; font-weight:bold;">Node.js</span> 코드 작성 
   - node.js가 로컬 pc내에 미리 설치되어 있어야 함.
2. <span style="color:lightpink; font-weight:bold;">Dockerfile</span> 작성 및 이미지 푸시
3. <span style="color:lightpink; font-weight:bold;">Pod</span> 내로 <span style="color:lightpink; font-weight:bold;">Docker Hub</span>에서 이미지를 갖고 온 다음 <span style="color:lightpink; font-weight:bold;">쿠버네티스</span> 환경 내에서 **Pod**와 **Service**구성

***

### 🚀 Node.js

먼저 로컬 PC에 <span style="color:lightpink; font-weight:bold;">Node.js</span>가 깔려 있는지 확인해 봅시다.

`node --version`

<img width="70%" alt="스크린샷 2022-02-24 오후 4 55 06" src="https://user-images.githubusercontent.com/56334761/155481966-1968c37b-6faf-4826-bdff-23797be26354.png">

노드 코드를 작성해 보도록 합시다.

`vi hello.js`

<img width="70%" alt="스크린샷 2022-02-24 오후 4 57 22" src="https://user-images.githubusercontent.com/56334761/155482179-815ee396-df88-4e97-9824-6abe65fb60ad.png">

이제 실행해 보도록 합시다.

`node hello.js`

<img width="1440" alt="스크린샷 2022-02-24 오후 5 00 36" src="https://user-images.githubusercontent.com/56334761/155482709-93a678d0-e3c7-486e-b3e3-cd67874e86da.png">

**잘 실행되고 있는 것을 확인할 수 있습니다!**

***
### 🚀 Docker

이제 <span style="color:lightpink; font-weight:bold;">Dockerfile</span>을 작성해 보겠습니다.

`vi Dockerfile`

<img width="70%" alt="스크린샷 2022-02-24 오후 5 04 00" src="https://user-images.githubusercontent.com/56334761/155483210-13d49f47-5e07-4bc4-9b53-418468060c8e.png">

이제 <span style="color:lightpink; font-weight:bold;">Dockerfile</span>을 빌드해보도록 하겠습니다

`docker build -t repository/hello .`

이제 빌드된 이미지를 통해 구동시켜보도록 하겠습니다.

`docker run -d -p 8100:8000 repository/hello`

<img width="1440" alt="스크린샷 2022-02-24 오후 5 09 10" src="https://user-images.githubusercontent.com/56334761/155483995-584ab4ca-a4b0-445b-a59f-c231ebefc2a6.png">


마찬 가지로 잘 구동되는 것을 확인할 수 있습니다!

이제는 <span style="color:lightpink; font-weight:bold;">Dockerhub</span>에 빌드된 이미지를 Push하도록 하겠습니다.

`docker login`

<img width="70%" alt="스크린샷 2022-02-24 오후 5 11 53" src="https://user-images.githubusercontent.com/56334761/155484464-edcd82e1-9fd1-4668-a242-6da5556ac137.png">


`docker push repository/hello`

이렇게 이미지 push까지 완료하였습니다.

***
### 🚀 Kubernetes

먼저 <span style="color:lightpink; font-weight:bold;">minikube</span>를 구동 시켜 줍니다.

`minikube start`

<img width="70%" alt="스크린샷 2022-02-24 오후 5 15 50" src="https://user-images.githubusercontent.com/56334761/155485029-8037a102-12b6-4522-ae8b-55b42d16eb7c.png">

<span style="color:lightpink; font-weight:bold;">Dockerhub</span>에 push된 이미지를 통해 <span style="color:lightpink; font-weight:bold;">Pod</span>를 구성해보도록 하겟습니다.

`vi pod.yaml`

<img width="70%" alt="스크린샷 2022-02-24 오후 5 52 41" src="https://user-images.githubusercontent.com/56334761/155491035-247494cf-970d-4987-8ab8-3e797d1e369a.png">

`kubectl create -f ./pod.yaml`

`kubectl apply -f ./pod.yaml`

<img width="70%" alt="스크린샷 2022-02-24 오후 5 55 14" src="https://user-images.githubusercontent.com/56334761/155491414-648dc54b-cbee-4f4c-bc1b-23f3181d096b.png">

<span style="color:lightpink; font-weight:bold;">pod</span>생성이 완료 되었습니다. 

잘 생성되었는지 확인 해 봅시다.

`kubectl get pod`

<img width="70%" alt="스크린샷 2022-02-24 오후 5 56 34" src="https://user-images.githubusercontent.com/56334761/155491607-a2f9265d-89f6-4baa-9143-0519015efc2d.png">

잘 생성된 것을 확인할 수 있습니다.

이제 <span style="color:lightpink; font-weight:bold;">Service</span>를 <span style="color:lightpink; font-weight:bold;">Pod</span>에 연결하여 Pod에 접근 가능하도록 구성해보겠습니다.

`vi service.yaml`

<img width="70%" alt="스크린샷 2022-02-24 오후 6 01 50" src="https://user-images.githubusercontent.com/56334761/155492488-c7451e9f-f01c-4ca4-895f-73157327c6e1.png">

`kubectl create -f ./service.yaml`

`kubectl apply -f ./service.yaml`

<img width="70%" alt="스크린샷 2022-02-24 오후 6 03 29" src="https://user-images.githubusercontent.com/56334761/155492733-acaeb090-8fd2-49be-9cae-8ae7b87723fe.png">

<span style="color:lightpink; font-weight:bold;">Service</span>또한 잘 생성된 것을 확인 할 수 있습니다.

이제 <span style="color:lightpink; font-weight:bold;">Service</span>를  통해<span style="color:lightpink; font-weight:bold;">Pod</span>에 접근해 보도록 합시다!

`minikube service hello-svc --url`

<img width="70%" alt="스크린샷 2022-02-24 오후 6 05 53" src="https://user-images.githubusercontent.com/56334761/155493162-7066b979-6a96-4c6b-92f1-e34a534b381c.png">

이제 <span style="color:lightpink; font-weight:bold;">URL</span> 에 접속해보도록 하겠습니다!

<img width="1440" alt="스크린샷 2022-02-24 오후 6 08 13" src="https://user-images.githubusercontent.com/56334761/155493590-a83d248a-266e-4e50-8292-9fde54642365.png">

잘 작동하고 있는 것을 확인하였습니다!

***
### <span style="color:lightpink; font-weight:bold;">이상으로 마치겟습니다. 🙋🏻‍♂️</span>

>이 글은 김태민님의 대세는 쿠버네티스 강의를 참고하여 정리하였습니다!
>
>출처 : https://www.inflearn.com/course/%EC%BF%A0%EB%B2%84%EB%84%A4%ED%8B%B0%EC%8A%A4-%EA%B8%B0%EC%B4%88/dashboard