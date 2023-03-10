## Pod - Container, Label, Node Schedule
***




### π νμ€νΈ ν΄ λ³Ό λ΄μ©
1. <span style="color:lightpink; font-weight:bold;">Pod</span>κ° μ΄λ»κ² κ΅¬μ±λμ΄ μλμ§ μμ λ΄μλ€.
2. <span style="color:lightpink; font-weight:bold;">Pod</span> μ <span style="color:lightpink; font-weight:bold;">label</span>μ λ¬μ λ΄μλ€.
3. <span style="color:lightpink; font-weight:bold;">Node Schedule</span>μ λνμ¬ κ°λ΅ν μ μλ΄μλ€. 

>λμ»€ μ΄λ―Έμ§λ κΉνλ―Όλκ»μ λ§λ€μ΄λμ  μ΄λ―Έμ§λ₯Ό μ¬μ©ν©λλ€.

***

### π Pod - Container

λ¨Όμ , <span style="color:lightpink; font-weight:bold;">Pod</span>λ κΈ°λ³Έμ μΌλ‘ ν κ° μ΄μμ <span style="color:lightpink; font-weight:bold;">μ»¨νμ΄λ</span>λ€λ‘ κ΅¬μ±κ΄΄μ΄ μμ΅λλ€.

<span style="color:lightpink; font-weight:bold;">Pod</span>μμ <span style="color:lightpink; font-weight:bold;">μ»¨νμ΄λ</span>λ€μ μλΉμ€κ° μλ‘ μ°κ²°λ  μ μλλ‘ <span style="color:lightpink; font-weight:bold;">ν¬νΈ</span>λ₯Ό κ°μ§κ³  μμ΅λλ€.

μ¬κΈ°μ, ν <span style="color:lightpink; font-weight:bold;">μ»¨νμ΄λ</span>λ νλ μ΄μμ <span style="color:lightpink; font-weight:bold;">ν¬νΈ</span>λ₯Ό κ°μ§ μ μμ§λ§, ν <span style="color:lightpink; font-weight:bold;">ν¬νΈ</span>λ₯Ό μ¬λ¬ <span style="color:lightpink; font-weight:bold;">μ»¨νμ΄λ</span>κ° κ³΅μ νμ§λ λͺ»νκ² λ©λλ€.

<span style="color:lightpink; font-weight:bold;">Pod</span>λ λν μμ±λ  λ κ³ μ μ <span style="color:lightpink; font-weight:bold;">IP</span>κ° μμ±λ©λλ€.

<span style="color:lightpink; font-weight:bold;">Pod</span>λ₯Ό <span style="color:lightpink; font-weight:bold;">IP</span>λ₯Ό ν΅ν΄μ μ κ·Όν  κ²½μ° <span style="color:lightpink; font-weight:bold;">μΏ λ²λ€ν°μ€ ν΄λ¬μ€ν°</span> λ΄μμλ§ μ κ·Όμ΄ κ°λ₯νκ³ , μΈλΆμμλ μ κ·Όμ΄ λΆκ°λ₯νκ² λ©λλ€.

**νλ² μ§μ ν΄ λ³΄λλ‘ νκ² μ΅λλ€.**

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

<img width="70%" alt="αα³αα³αα΅α«αα£αΊ 2022-02-25 αα©αα? 5 08 27" src="https://user-images.githubusercontent.com/56334761/155678733-178f08c3-7f7d-494d-b9c5-3e62a99d4966.png">

μ΄μ  Podλ‘ μ κ·Όνμ¬ ν¬νΈμ λ°λ₯Έ μ»¨νμ΄λ μ΄λ¦μ μΆλ ₯νμ¬ λ΄μλ€. (λμ»€ μ΄λ―Έμ§μ λ°λ₯Έ κΈ°λ₯μλλ€.)

**Pod IPνμΈ**

`kubectl get pods -o wide`

**Pod μ μ**

`kubectl exec -it pod-1 /bin/bash`

**curl λͺλ Ήμ΄λ‘ Container Port νμΈ**

`curl 10.244.0.5:8000`

`curl 10.244.0.5:8080`


<img width="70%" alt="αα³αα³αα΅α«αα£αΊ 2022-02-25 αα©αα? 5 26 12" src="https://user-images.githubusercontent.com/56334761/155681172-0992702f-3e50-47b4-b833-e69f623de0d1.png">

Podλ΄μ μ»¨νμ΄λλ€μ΄ μ λ§λ€μ΄ μ§ κ²μ νμΈνμμ΅λλ€.

**μ§κΈ λΆν°λ Podλ΄μ μ»¨νμ΄λλ€μ ν¬νΈκ° κ°μ λ μλ¬κ° λ°μνλμ§ μ΄ν΄ λ΄μλ€.**
- λͺλ Ήμ΄λ μμ λ΄μ©λ€κ³Ό κ±°μ λμΌ ν©λλ€.

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

**λμ± λ λμ λλ κ²°κ³Όλ₯Ό μν΄ λμλ³΄λμ μ μν΄ λ³΄λλ‘ νκ² μ΅λλ€!**

<img width="1440" alt="αα³αα³αα΅α«αα£αΊ 2022-02-25 αα©αα? 5 31 33" src="https://user-images.githubusercontent.com/56334761/155681975-e45f3ad0-7a60-457b-9feb-bc12c56fc093.png">


<img width="1440" alt="αα³αα³αα΅α«αα£αΊ 2022-02-25 αα©αα? 5 32 22" src="https://user-images.githubusercontent.com/56334761/155682094-1b49c330-5734-4a6a-9886-1da4df55d835.png">


**μ»¨νμ΄λ μμ±μ΄ μ μλ λͺ¨μ΅μ νμΈν  μ μμ΅λλ€!**

***

### π Label

<span style="color:lightpink; font-weight:bold;">Pod</span>λΏλ§μ΄ μλλΌ λͺ¨λ  <span style="color:lightpink; font-weight:bold;">μ€λΈμ νΈ</span>μμ λ€ μ°μλλ€. 

νμ§λ§, <span style="color:lightpink; font-weight:bold;">Pod</span>μμ κ°μ₯ λ§μ΄ μ¬μ©νλ€κ³  ν©λλ€.

<span style="color:lightpink; font-weight:bold;">Label</span>μ μ°λ μ΄μ λ λͺ©μ μ λ°λΌ <span style="color:lightpink; font-weight:bold;">μ€λΈμ νΈ</span>λ€μ λΆλ₯ν  μ μμΌλ©°, <span style="color:lightpink; font-weight:bold;">μ€λΈμ νΈ</span>λ€μ λ°λ‘ μ°κ²°νκΈ° μν΄μ μ¬μ©ν©λλ€.

κ΅¬μ±μ <span style="color:lightpink; font-weight:bold;">Keyμ Value</span>ννλ‘ λ§λ€μ΄μ§λλ€.

**μ΄μ  Pod2κ°λ₯Ό λ§λ€μ΄μ μ€μ΅ν΄ λ΄μλ€.**

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

μμ±λ Labelμ μ ννκΈ° μν Serviceμλλ€.

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

**μλΉμ€ 1κ³Ό μλΉμ€ 2λ κ°κ° typeμ΄ webκ³Ό databaseμΈ Podλ₯Ό κ°λ¦¬ν€κ³  μμ΅λλ€.**

**dashboardμ μ κ·Όνμ¬ κ°κ°μ μλΉμ€κ° κ°κ°μ Podλ₯Ό μ μ°κ²° νμλμ§ νμΈν΄ λ΄μλ€.**

<img width="1440" alt="αα³αα³αα΅α«αα£αΊ 2022-02-25 αα©αα? 5 48 45" src="https://user-images.githubusercontent.com/56334761/155684598-21f3f5e2-9fde-4f95-979e-461bed3c4a27.png">

<img width="1440" alt="αα³αα³αα΅α«αα£αΊ 2022-02-25 αα©αα? 5 48 45" src="https://user-images.githubusercontent.com/56334761/155684731-2ec2a4f9-125c-445b-8ca5-75bef8485b31.png">

μ΄λ κ² μ μ§μΉ­νκ³  μλ κ²μ λ³Ό μ μμ΅λλ€!

***

### π Node Schedule

<span style="color:lightpink; font-weight:bold;">Pod</span>λ μ¬λ¬ <span style="color:lightpink; font-weight:bold;">λΈλ</span>λ€ μ€μ ν λΈλμ μ¬λΌκ°μΌ ν©λλ€.

μ§μ  <span style="color:lightpink; font-weight:bold;">λΈλ</span>λ₯Ό μ ννλ λ°©λ², <span style="color:lightpink; font-weight:bold;">μΏ λ²λ€ν°μ€</span>κ° μλμΌλ‘ μ ννλ λ°©λ²μ΄ μ‘΄μ¬ν©λλ€.

<span style="color:lightpink; font-weight:bold;">μΏ λ²λ€ν°μ€μ μ€μΌμ₯΄λ¬</span>κ° νλ¨ νμ¬ <span style="color:lightpink; font-weight:bold;">λΈλ</span>λ₯Ό κ³ λ₯Ό μ μμ΅λλ€.
- Cpu μ¬μ©λ
- Memory μ¬μ©λ
- ...

νμ§λ§, μ κ° μ¬μ©νκ³  μλ <span style="color:lightpink; font-weight:bold;">minikube</span>νκ²½μ <span style="color:lightpink; font-weight:bold;">λ¨μΌ λΈλ ν΄λ¬μ€ν°</span>μ΄κΈ° λλ¬Έμ μ€μ΅μ μ§νν  μ μκΈ°μ μ¬κΈ°μ λ§μΉκ² μ΅λλ€!

***
### <span style="color:lightpink; font-weight:bold;">μ΄μμΌλ‘ λ§μΉκ² μ΅λλ€. ππ»ββοΈ</span>