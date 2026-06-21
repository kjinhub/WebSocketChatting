# 🗨️ WebSocket Chat App (Spring Boot + React)

이 프로젝트는 **Spring Boot(WebSocket + STOMP)** 와 **React(SockJS + STOMP.js)** 를 활용한 실시간 채팅 애플리케이션입니다.  
공인 IP를 통해 서버에 접속하면, **울산 ↔ 진주처럼 다른 지역에서도 실시간 채팅**이 가능합니다.  

---

## 🚀 프로젝트 개요

- **백엔드**: Spring Boot  
  - WebSocket + STOMP 기반 실시간 메시징 처리
  - `@MessageMapping`, `@SendTo` 사용하여 발행/구독 구조 구현
- **프론트엔드**: React (CRA 기반)  
  - SockJS로 서버 연결
  - STOMP.js로 메시지 발행/구독 관리
- **실행 환경**:  
  - Ubuntu Linux (서버 실행)  
  - 공인 IP + 포트포워딩을 통해 외부 접속 가능



## ⚙️ 주요 코드

### ✅ 백엔드 (Spring Boot)

**WebSocketConfig.java**
```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // 구독 경로
        config.setApplicationDestinationPrefixes("/app"); // 발행 경로
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").setAllowedOriginPatterns("*").withSockJS();
    }
}
ChatController.java

@Controller
public class ChatController {
    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public ChatMessage send(ChatMessage message) {
        return message;
    }
}
✅ 프론트엔드 (React)
App.js

useEffect(() => {
  const socket = new SockJS("http://<공인IP>:8080/chat");

  const client = new Client({
    webSocketFactory: () => socket,
    onConnect: () => {
      console.log("✅ WebSocket 연결 성공");
      setStompClient(client);

      client.subscribe("/topic/messages", (msg) => {
        const received = JSON.parse(msg.body);
        setMessages((prev) => [...prev, received]);
      });
    },
  });

  client.activate();
  return () => client.deactivate();
}, []);

🌐 동작 원리
연결

클라이언트(React) → http://<공인IP>:8080/chat 로 WebSocket 연결 요청

Spring Boot 서버 → 엔드포인트 /chat 에서 연결 수락

메시지 발행 (Publish)

클라이언트가 /app/send 로 메시지를 보냄

메시지 처리

서버 @MessageMapping("/send") 에서 메시지 수신

@SendTo("/topic/messages") 를 통해 모든 구독자에게 브로드캐스트

메시지 수신 (Subscribe)

클라이언트가 /topic/messages 를 구독하고 있다가 → 실시간으로 메시지 수신

📡 울산 ↔ 진주에서 채팅이 가능한 이유
웹소켓(WebSocket) 은 HTTP와 달리 연결을 계속 유지하면서 양방향으로 통신할 수 있음

따라서 울산(클라이언트 A)에서 메시지를 보내면 → 서버가 유지된 연결을 통해 진주(클라이언트 B)에게 즉시 전달

서버가 공인 IP로 열려 있기 때문에, 인터넷이 되는 어디서든 접속 가능

🛠️ 실행 방법
1. 백엔드 실행
cd server
./mvnw clean package
java -jar target/WSchatting-0.0.1-SNAPSHOT.jar
2. 프론트엔드 실행

cd client
npm install
npm start
브라우저에서 http://localhost:3000 접속 후 채팅 확인

📷 실행 화면 (예시)

[이름 입력] ➡ [메시지 입력] ➡ [보내기]
실시간으로 메시지가 채팅창에 표시됩니다.
📌 기술 스택
백엔드: Spring Boot, WebSocket, STOMP

프론트엔드: React (CRA), SockJS, @stomp/stompjs

배포 환경: Ubuntu Linux, 공인 IP

📖 참고
Spring WebSocket 공식 문서

STOMP 프로토콜

SockJS
