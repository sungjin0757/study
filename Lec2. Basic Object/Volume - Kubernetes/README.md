## Volume - EmptyDir, Host Path, PVC
***
>이 글은 김태민님의 대세는 쿠버네티스 강의를 참고하여 정리하였습니다!
>
>출처 : https://www.inflearn.com/course/%EC%BF%A0%EB%B2%84%EB%84%A4%ED%8B%B0%EC%8A%A4-%EA%B8%B0%EC%B4%88/dashboard
***

### 🔍 테스트해 볼 내용

**Volume Object의 Option에 대해서 배워볼게요!**

1. <span style="color:lightpink; font-weight:bold;">EmptyDir</span> 구성
2. <span style="color:lightpink; font-weight:bold;">HostPath</span> 구성
3. <span style="color:lightpink; font-weight:bold;">PVC</span> 구성

***

### 🚀 EmptyDir
<span style="color:lightpink; font-weight:bold;">Container</span> 들 간 데이터를 공유하기 위해서 <span style="color:lightpink; font-weight:bold;">Pod</span>안에 일종의 저장소인 <span style="color:lightpink; font-weight:bold;">Volume</span>을 사용하는 것입니다.

이 <span style="color:lightpink; font-weight:bold;">Volume</span>이 생성될 때는 항상 <span style="color:lightpink; font-weight:bold;">Volume</span>안에 내용이 비어있기 때문에 <span style="color:lightpink; font-weight:bold;">EmptyDir</span>이라는 이름이 붙여집니다.

이 <span style="color:lightpink; font-weight:bold;">Volume</span>을 생성하고 <span style="color:lightpink; font-weight:bold;">Container</span>들이 <span style="color:lightpink; font-weight:bold;">mount</span>시켜 놓으면 자신의 <span style="color:lightpink; font-weight:bold;">Local</span>에 있는 것처럼 사용하기 때문에 두 <span style="color:lightpink; font-weight:bold;">Container</span>가 서로 파일을 주고 받을 필요 없이 편하게 사용 가능합니다.

단점은, <span style="color:lightpink; font-weight:bold;">Pod</span>가 문제가 생기어서 <span style="color:lightpink; font-weight:bold;">Pod</span>가 재생성되게 되면 <span style="color:lightpink; font-weight:bold;">Volume</span>이 삭제됩니다.

즉, 일시적인 사용목적인 데이터만을 저장하는 것이 바람직합니다.

**이제, 한번 실습해보도록 합시다!** 

**pod1.yaml**

```json
apiVersion: v1
kind: Pod
metadata: 
  name: pod-volume-1
spec:
  containers:
  - name: container1
    image: kubetm/init
    volumeMounts:
    - name: empty-dir
      mountPath: /mount1
  - name: container2
    image: kubetm/init
    volumeMounts:
    - name: empty-dir
      mountPath: /mount2
  volumes:
  - name: empty-dir
    emptyDir: {}  
```

`kubectl create -f ./pod1.yaml`

`kubectl apply -f ./pod1.yaml`

<img width="70%" alt="스크린샷 2022-03-03 오후 9 16 42" src="https://user-images.githubusercontent.com/56334761/156563114-9f59703f-3bf7-40cc-93ee-4d3c088f5b9b.png">

**이제 생성된 pod에 각각의 컨테이너에 접근해보도록 합시다!**

`kubectl exec pod-volume-1 -c container1 -it /bin/bash`

`kubectl exec pod-volume-2 -c container1 -it /bin/bash`

<img width="70%" alt="스크린샷 2022-03-03 오후 9 21 18" src="https://user-images.githubusercontent.com/56334761/156563763-db47837b-d7db-4ba0-b424-f1d0e59ae807.png">

**이렇게 잘 접근된 것을 볼 수 있습니다!**

**이제 컨테이너1에서 mount된 볼륨에 접근하여 파일을 생성해보도록 하겠습니다**

`ls` 를 통해 각각의 컨테이너들이 volume을 잘 마운트하고 있는지 `mount | grep mount1`으로 mount된 폴더인지 확인해 봅시다!

<img width="70%" alt="스크린샷 2022-03-03 오후 9 23 59" src="https://user-images.githubusercontent.com/56334761/156564275-8a23d785-ec40-4857-b761-c196d08960a1.png">

<img width="70%" alt="스크린샷 2022-03-03 오후 9 28 27" src="https://user-images.githubusercontent.com/56334761/156564767-f16abefa-1ae1-425f-928b-643cc26b2314.png">

**잘 마운트된 것을 확인할 수 있습니다!**

지금부터는,
1. container1에서 mount폴더에 file만들기.
2. container2에서 container1에서 만든 폴더가 잘 저장되어있나 확인하기.
3. Pod삭제했을 때, Volume도 같이 삭제 되는지!

<img width="70%" alt="스크린샷 2022-03-03 오후 9 30 57" src="https://user-images.githubusercontent.com/56334761/156565153-39bab82d-6081-4e78-9ace-18b097da7702.png">

**먼저, 이렇게 잘 생성된 것을 확인하였습니다!**

**이제는 container2에서 확인해보도록 합시다.**

<img width="70%" alt="스크린샷 2022-03-03 오후 9 32 55" src="https://user-images.githubusercontent.com/56334761/156565456-070af2b6-e9a1-4c62-96f6-a6d889f16ee3.png">

**container2에서 또한 container1에서 만든 파일을 사용할 수 있다는 것을 확인하였습니다.**

**이제는 Pod를 한번 지웠다 다시 생성하여 봅시다.**

`kubectl delete pod pod-volume-1`

<img width="70%" alt="스크린샷 2022-03-03 오후 9 35 31" src="https://user-images.githubusercontent.com/56334761/156565816-9d4dfb15-2f67-4f19-98b7-df0360a6d668.png">

<img width="70%" alt="스크린샷 2022-03-03 오후 9 36 51" src="https://user-images.githubusercontent.com/56334761/156566062-b84ffb8d-6455-42b5-831e-cec6e95b0369.png">

**마지막으로, Pod를 지웠다가 재생성하였을때, Volume또한 재생성되었다는 것을 확인하였습니다!**

***

### 🚀 HostPath

<span style="color:lightpink; font-weight:bold;">EmptyDir</span>과 다르게 <span style="color:lightpink; font-weight:bold;">Pod</span>들이 올라가져 있는 각 <span style="color:lightpink; font-weight:bold;">Node</span>의 <span style="color:lightpink; font-weight:bold;">Path</span>를 <span style="color:lightpink; font-weight:bold;">Volume</span>으로 사용합니다.
또한, <span style="color:lightpink; font-weight:bold;">Volume</span>은 <span style="color:lightpink; font-weight:bold;">Pod</span>밖에 존재하게 됩니다.

즉, <span style="color:lightpink; font-weight:bold;">Pod</span>가 삭제되었더라도 <span style="color:lightpink; font-weight:bold;">Path</span>를 각각의 <span style="color:lightpink; font-weight:bold;">Pod</span>들이 공유해서 사용하기 때문에, <span style="color:lightpink; font-weight:bold;">Pod</span>들이 죽어도 데이터는 사라지지 않게 됩니다.

데이터가 영속성이 있다는 점에서 훌륭해 보일 수도 있지만, <span style="color:lightpink; font-weight:bold;">Pod</span>입장에서는 삭제되었다가 재생성 되었을 때, 같은 <span style="color:lightpink; font-weight:bold;">Node</span>에 똑같이 생성될 것이라는 보장이 없습니다.

결론적으로, 이러한 <span style="color:lightpink; font-weight:bold;">Volume</span>을 사용할 떄는, <span style="color:lightpink; font-weight:bold;">Node</span> 자기 자신을 위해서 유지해야 할 시스템 파일, 설정 파일들을 유지시킬 때 주로 사용됩니다.

주의해야할 점은 <span style="color:lightpink; font-weight:bold;">Host</span>에 있는 <span style="color:lightpink; font-weight:bold;">Path</span>는 <span style="color:lightpink; font-weight:bold;">Pod</span>가 만들어지기 전에 만들어져야 합니다.

**이제 직접 실습해 보도록 합시다!**

**pod2.yaml**

```json
apiVersion: v1
kind: Pod
metadata:
  name: pod-volume-3
spec:
  containers:
  - name: container
    image: kubetm/init
    volumeMounts:
    - name: host-path
      mountPath: /mount
  volumes:
  - name: host-path
    hostPath:
      path: /test-node
      type: DirectoryOrCreate
```

`kubectl create -f ./pod2.yaml`

`kubectl apply -f ./pod2.yaml`

<img width="70%" alt="스크린샷 2022-03-03 오후 9 48 10" src="https://user-images.githubusercontent.com/56334761/156567745-48fec76e-0f6d-4c9f-86d9-98cb0327892a.png">

지금부터는, 
1. pod 접근
2. mount된 폴더에 파일 만들기
3. node에서 pod가 만든 파일 확인하기
4. pod 재생성 후, 파일이 잘 유지되고 있나 확인하기.

**먼저 pod에 접근해보도록 하겠습니다.**

<img width="70%" alt="스크린샷 2022-03-03 오후 9 52 06" src="https://user-images.githubusercontent.com/56334761/156568402-e79df0d3-729c-471e-9eb5-055820a304bc.png">

**mount폴더 또한 만든 것을 확인할 수 있습니다.**

**이제, mount폴더에 파일을 만들도록 하겠습니다.**

<img width="70%" alt="스크린샷 2022-03-03 오후 9 53 44" src="https://user-images.githubusercontent.com/56334761/156568607-aa91305f-f09e-48ba-8443-7131b6402b99.png">

**지금부터는, node에 접근하여 pod에서 만든 파일을 확인해보도록 하겠습니다.**

`minikube ssh`

<img width="70%" alt="스크린샷 2022-03-03 오후 9 55 20" src="https://user-images.githubusercontent.com/56334761/156568932-6690bc5f-81a0-4b4b-a97a-b54055e8fcf2.png">

**Node에서 또한 Volume에 잘 마운트된 것을 확인하였고, Pod에서 만든 파일 또한 확인하였습니다.**

**이제, 현재 pod를 재생성한 다음에도, 똑같은 파일이 유지되고 있는지 확인해 봅시다.**

<img width="70%" alt="스크린샷 2022-03-03 오후 10 03 29" src="https://user-images.githubusercontent.com/56334761/156570126-cfd5aec7-8622-417b-8eae-4f5d6a34ea38.png">

**마지막으로, Pod를 지웠다가 재생성하였을때, Volume이 유지되고 있는 것을 확인하였습니다!**

***

### 🚀 PVC - Persistent Volume Claim

<span style="color:lightpink; font-weight:bold;">Pod</span>에 영속성 있는 <span style="color:lightpink; font-weight:bold;">Volume</span>을 제공하기 위한 개념입니다.

<span style="color:lightpink; font-weight:bold;">Volume</span>의 형태를 <span style="color:lightpink; font-weight:bold;">Local</span>에서 뿐 아니라 외부의 <span style="color:lightpink; font-weight:bold;">Git, AWS</span>등에 연결할 수 있습니다.
이러한 <span style="color:lightpink; font-weight:bold;">Volume</span>을 <span style="color:lightpink; font-weight:bold;">PV - Persistent Volume</span>이라 부르게 됩니다.

<span style="color:lightpink; font-weight:bold;">Pod</span>는 바로 <span style="color:lightpink; font-weight:bold;">PV</span>에 연결하는 것이 아닌, <span style="color:lightpink; font-weight:bold;">PVC</span>를 통해 <span style="color:lightpink; font-weight:bold;">PV</span>와 연결하게 됩니다.

<span style="color:lightpink; font-weight:bold;">PVC</span>를 통해 연결하는 이유는 <span style="color:lightpink; font-weight:bold;">Volume</span>들의 종류는 많고, <span style="color:lightpink; font-weight:bold;">Volume</span>들을 연결하기 위한 설정들도 각각 틀리기 때문입니다. 

**PVC/PV는 실습에 제약이 따르므로 여기서 마치도록 하겠습니다!**

***
### <span style="color:lightpink; font-weight:bold;">이상으로 마치겠습니다. 🙋🏻‍♂️</span>