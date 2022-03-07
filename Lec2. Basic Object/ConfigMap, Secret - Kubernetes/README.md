## ConfigMap, Secret 
***

>이 글은 김태민님의 대세는 쿠버네티스 강의를 참고하여 정리하였습니다!
>
>출처 : https://www.inflearn.com/course/%EC%BF%A0%EB%B2%84%EB%84%A4%ED%8B%B0%EC%8A%A4-%EA%B8%B0%EC%B4%88/dashboard
***

### 🔍 테스트해 볼 내용

1. <span style="color:lightpink; font-weight:bold;">ConfigMap</span>과 <span style="color:lightpink; font-weight:bold;">Secret</span>의 <span style="color:lightpink; font-weight:bold;">상수</span> 값을 <span style="color:lightpink; font-weight:bold;">Pod의 환경변수</span>로 설정하기
2. <span style="color:lightpink; font-weight:bold;">File</span> 형식의 <span style="color:lightpink; font-weight:bold;">ConfigMap</span>과 <span style="color:lightpink; font-weight:bold;">Secret</span>값을 <span style="color:lightpink; font-weight:bold;">Pod의 환경변수</span>로 설정하기
3. <span style="color:lightpink; font-weight:bold;">File</span> 형식의 <span style="color:lightpink; font-weight:bold;">ConfigMap</span>과 <span style="color:lightpink; font-weight:bold;">Secret</span>값을 Pod의 환경변수가 아닌 <span style="color:lightpink; font-weight:bold;">Volume으로 Mount</span>하기. 


 >도커 이미지는 김태민님께서 만들어두신 이미지를 사용합니다.
***

### 🚀 Env - Literal

먼저, <span style="color:lightpink; font-weight:bold;">ConfigMap</span>과 <span style="color:lightpink; font-weight:bold;">Secret 오브젝트</span>를 사용하는 이유를 생각해봅시다.

생각해 보면, <span style="color:lightpink; font-weight:bold;">서비스</span>를 배포하는데 <span style="color:lightpink; font-weight:bold;">개발환경과 상용환경</span>의 차이가 있을 것입니다.
그 이유는, 대부분 <span style="color:lightpink; font-weight:bold;">설정정보에서의 차이 & 보안접근에서의 차이</span>입니다.

따라서, <span style="color:lightpink; font-weight:bold;">상용환경과 개발환경</span>에서 배포를 한다면, 값이 바뀌어야한다는 것을 의미합니다.

이러한 값은 <span style="color:lightpink; font-weight:bold;">Container</span>안에 있는 이미지의 값이기 때문에 값을 바꾼다는 것은 <span style="color:lightpink; font-weight:bold;">개발환경과 상용환경</span>에 <span style="color:lightpink; font-weight:bold;">Image</span>를 각각 관리해야한다는 것을 뜻합니다.

굉장히 불합리적인 일이 되겠죠. 그러므로, 변하는 값들을 외부에서 결정이 가능하도록 도움을 주는 <span style="color:lightpink; font-weight:bold;">Object</span>인 <span style="color:lightpink; font-weight:bold;">ConfigMap</span>과 <span style="color:lightpink; font-weight:bold;">Secret</span>을 사용하는 것입니다.

이제, 저희가 먼저 테스트해볼 것은 분리해야 하는 일반적인 상수들을 모아서 <span style="color:lightpink; font-weight:bold;">ConfigMap</span>으로 만들고 Key와 같은 보안적인 관리가 필요한 것을 모아서 <span style="color:lightpink; font-weight:bold;">Secret</span>으로 만들고 <span style="color:lightpink; font-weight:bold;">Pod 생성시 환경변수</span>로서 연결하는 것입니다.

이러한 테스트가 성공하게 되면, 데이터만 바꿔주면서 똑간은 <span style="color:lightpink; font-weight:bold;">Image</span>로 <span style="color:lightpink; font-weight:bold;">개발환경과 상용환경</span>에서 사용이 가능하게 됩니다.

**ConfigMap을 만들어 봅시다.**

**ConfigMap1.yaml**

```json
apiVersion: v1
kind: ConfigMap
metadata:
  name: cm1
data:
  SSH: 'false'
  User: dev
```

`kubectl create -f ./ConfigMap1.yaml`

`kubectl apply -f ./ConfigMap1.yaml`

<img width="70%" alt="스크린샷 2022-03-06 오전 3 07 32" src="https://user-images.githubusercontent.com/56334761/156895154-1797e028-30ac-4a81-802d-e815477e2b5e.png">

**Secret을 만들어 봅시다.**

**Secret1.yaml**

```json
apiVersion: v1
kind: Secret
metadata:
  name: sec1
data:
  key: MTIzNA==
```

**MTIzNA==는 1234를 Base64로 인코딩한 것입니다.**

`kubectl create -f ./Secret1.yaml`

`kubectl apply -f ./Secret1.yaml`

<img width="70%" alt="스크린샷 2022-03-06 오전 3 11 42" src="https://user-images.githubusercontent.com/56334761/156895277-72c49f37-a106-4dec-b270-30403f583b1e.png">

**이제 Pod를 만들어보도록 합시다**

**pod1.yaml**

```json
apiVersion: v1
kind: Pod
metadata:
  name: pod1
spec:
  containers:
  - name: container
    image: kubetm/init
    envFrom:
    - configMapRef:
        name: cm1
    - secretRef:
        name: sec1
```

`kubectl create -f ./Secret1.yaml`

`kubectl apply -f ./Secret1.yaml`

<img width="70%" alt="스크린샷 2022-03-06 오전 3 15 05" src="https://user-images.githubusercontent.com/56334761/156895392-62fce5d2-b956-4ca2-9e8c-89c397763ae2.png">

**이제는 Pod안에 접속하여 환경 변수의 값을 알아보도록 합시다**

`kubectl exec pod1 -it /bin/bash`

`env`

<img width="70%" alt="스크린샷 2022-03-06 오전 3 17 12" src="https://user-images.githubusercontent.com/56334761/156895471-1590c10d-f9cb-4a48-9301-faa3b320d5da.png">

**환경변수로서 ConfigMap과 Secret의 값이 잘 설정 된 것을 볼 수 있습니다!**

***

### 🚀 Env - File

위에서는 <span style="color:lightpink; font-weight:bold;">ConfigMap과 Secret</span>의 값으로 상수를 정해주었습니다.

이번에는, <span style="color:lightpink; font-weight:bold;">상수</span>대신 <span style="color:lightpink; font-weight:bold;">파일</span>을 통으로 값으로서 설정해보도록 하겠습니다.

이렇게 설정하게 되면, <span style="color:lightpink; font-weight:bold;">파일 이름</span>이 <span style="color:lightpink; font-weight:bold;">Key</span>가 되고 <span style="color:lightpink; font-weight:bold;">내용</span>이 <span style="color:lightpink; font-weight:bold;">Volume</span>이 됩니다.

**ConfigMap 파일을 만들고 Object를 생성해봅시다.**

`echo "ConfigMap" >> config-file.txt`

`kubectl create configmap cm-file --from-file=./config-file.txt`

<img width="70%" alt="스크린샷 2022-03-06 오전 3 27 40" src="https://user-images.githubusercontent.com/56334761/156895747-22fd6c39-67f8-4bae-8bef-27dbca33d449.png">

**Secret 파일을 만들고 Object를 생성해봅시다.**

`echo "Secret" >> secret-file.txt`

`kubectl create secret generic sec-file --from-file=./secret-file.txt`

<img width="70%" alt="스크린샷 2022-03-06 오전 3 29 27" src="https://user-images.githubusercontent.com/56334761/156895795-dd55fcfb-7f3b-40d9-aa8c-9e0cf3a58823.png">

**pod를 만들어 만들어둔 파일들을 연결해봅시다**

**pod2.yaml**

```json
apiVersion: v1
kind: Pod
metadata:
  name: pod-file
spec:
  containers:
  - name: container
    image: kubetm/init
    env:
    - name: config-file
      valueFrom:
        configMapKeyRef:
          name: cm-file
          key: config-file.txt
    - name: secret-file
      valueFrom:
        secretKeyRef:
          name: sec-file
          key: secret-file.txt
```

**pod를 생성하고, 환경변수를 확인해 봅시다**

<img width="70%" alt="스크린샷 2022-03-06 오전 3 35 36" src="https://user-images.githubusercontent.com/56334761/156895981-92cd24b9-0a6f-4a5c-b890-2b2a9ca6bc30.png">

**잘 만들어진 것을 확인할 수 있습니다!**

***
### 🚀 Volume - File

>Volume이 궁금하시다면,
>
> <a href="https://github.com/sungjin0757/kebernetes-basic/tree/master/Lec2.%20Basic%20Object/Volume%20-%20Kubernetes" target="_blank">😀 Github 바로가기</a>
> 
> <a href="https://velog.io/@sungjin0757/Volume-EmptyDir-Host-Path-PVC" target="_blank">👋 Velog 바로가기</a>

위에서 <span style="color:lightpink; font-weight:bold;">ConfigMap과 Secret</span> <span style="color:lightpink; font-weight:bold;">Object</span>의 값을 파일의 내용으로 설정하였고, <span style="color:lightpink; font-weight:bold;">Container의 환경변수</span>로 연결하였습니다.

이번에는 파일을 <span style="color:lightpink; font-weight:bold;">환경변수</span>로 설정하는 것이 아닌, <span style="color:lightpink; font-weight:bold;">Volume</span>에 저장해놓고 <span style="color:lightpink; font-weight:bold;">path를 mount</span>하여 사용하여 봅시다!

**위에서 만들어놓은 ConfigMap 파일을 똑같이 사용하고 Pod만 새로 만들어 사용해 봅시다!**

**pod3.yaml**

```json
apiVersion: v1
kind: Pod
metadata:
  name: pod-mount
spec:
  containers:
  - name: container
    image: kubetm/init
    volumeMounts:
    - name: file-volume
      mountPath: /mount
  volumes:
  - name: file-volume
    configMap:
      name: cm-file
```

<img width="70%" alt="스크린샷 2022-03-06 오전 3 52 16" src="https://user-images.githubusercontent.com/56334761/156896423-3a32b781-a4f0-4866-8ae4-2b2883565b12.png">

**이렇게 volume에 대한 실습도 완료하였습니다.**

**하지만, 여기서 들 수 있는 의문점은 환경변수로 설정했을 때와 Volume으로 설정했을 때와 무엇이 다른가 일 것입니다. 한번 알아보도록 합시다!**

***
### 🚀 Difference - Env, Volume

먼저, ConfigMap의 값을 변경해보도록 합시다!

`kubectl edit configmap cm-file -o yaml`

<img width="70%" alt="스크린샷 2022-03-06 오전 3 58 02" src="https://user-images.githubusercontent.com/56334761/156896576-93081116-eca0-406a-9df9-66a19f2d233c.png">

**기존의 값에서 12345를 덧붙였습니다.**

**이제 위에서 만들었던 pod-file과 pod-mount에 접근하여 ConfigMap의 값이 어떻게 되었는지 확인해 봅시다**

<img width="1233" alt="스크린샷 2022-03-06 오전 4 03 06" src="https://user-images.githubusercontent.com/56334761/156896748-befb2fc0-4a80-41e2-9447-bd57fae03bea.png">

차이가 명확히 보입니다.

**즉, 환경변수로 설정된 값은 Pod가 삭제될 때 까지 유지가 되고, Volume을 Mount하는 것은 원본을 참조하기 때문에 값이 바뀌면 즉각적으로 대응 됩니다!**

***
### <span style="color:lightpink; font-weight:bold;">이상으로 마치겠습니다. 🙋🏻‍♂️</span>