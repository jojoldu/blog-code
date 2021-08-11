# 객체지향 (Object Oriented) 디자인 (Design)

> 여기서 이야기하는 디자인은 **코드 설계**와 동일하게 봐도 무방하다.

## 디자인이 왜 중요한가?

요즘의 웹 애플리케이션 개발에서는 디자인에 대한 지식이 없더라도, 원하는 바대로 작동하는 웹 애플리케이션을 만들 수 있다.  
  
특히나 JS, 파이썬, PHP 등 의 언어들은 문법이 너무나 친절하여 **자신의 생각을 순차적으로 정리만 할 수 있다면** 누구나 원하는 웹 애플리케이션을 만들 수 있다.  
  
작은 규모의 웹 애플리케이션에서는 이렇게 디자인을 전혀 고려하지 않고, 기능 구현에만 신경써도 문제는 없다.  
아니, 아예 **좋지 못한 디자인이라해도 문제가 되지 않는다**.  
객체간의 복잡한 관계, 계층화 되지 않은 구조 등 모듈화가 전혀 없어도 **개발자의 머릿속에 모든 것들을 담아두고 개발을 할 수 있기 때문**이다.  

> 반대로 얘기하면 **특정 누군가만 손댈수 있고, 그 사람의 머릿속에만 모든 애플리케이션 구조가 담겨져 있다면** 안좋은 디자인의 신호이다.  

하지만, 모든 웹 애플리케이션은 **성공을 하고나면 100% 새로운 기능을 요구받게 된다**.  
  
이런 변화가 시작되고, 점점 규모가 커지면 그때부터 잘못된 디자인의 애플리케이션은 변화의 장애물이 된다.  
하나의 객체를 수정하면 그 객체와 협업하는 다른 객체를 수정하고, 수정된 다른 객체와 협업하는 또다른 객체를 수정한다.  
끝도 없이 계속해서 협업 객체들을 수정하는 일이 시작된다.  
결국 그 피해는 애플리케이션 전반에 전파되고, 종국엔 모든 코드를 수정하게 된다.  
  
즉, 안좋은 디자인의 작은 애플리케이션이 갖는 문제는 이 애플리케이션이 발전해서 **안좋은 디자인의 큰 애플리케이션**이 되는데 있다.  

## 객체지향 디자인


OO (Object Oriented) 애플리케이션은 각 객체간의 상호작용으로 이루어져있다.  
이 객체간의 상호작용은 **서로 주고 받는 메세지 속에 있다**.  



> 아래 예제는 [조영호님의 오브젝트](https://www.kyobobook.co.kr/product/detailViewKor.laf?barcode=9791158391409) 예제 코드를 발췌해, TypeScript로 전환했다.

```ts
export class Theater {
    private ticketSeller: TicketSeller;

    constructor(ticketSeller: TicketSeller) {
        this.ticketSeller = ticketSeller;
    }

    public enter(audience: Audience): void {
        if (audience.bag.hasInvitation()) {
            const ticket = this.ticketSeller.ticketOffice.ticket;
            audience.bag.setTicket(ticket);
        } else {
            const ticket = this.ticketSeller.ticketOffice.ticket;
            audience.bag.minusAmount(ticket.fee);
            this.ticketSeller.ticketOffice.plusAmount(ticket.fee);
            audience.bag.setTicket(ticket);
        }
    }
}
```

* 극장(`Theater`)는 관람객(`audience`)의 가방(`bag`)을 열어 그 안에 초대장이 들어있는지 살펴본다 (`hasInvitation()`)
* 가방(`bag`) 안에 초대장이 있으면, 
  * 판매원(`ticketSeller`)은 매표소(`ticketOffice`)에 보관돼있는 티켓(`ticket`)을 관람객의 가방안으로 옮긴다 (`audience.bag.setTicket(ticket)`)
* 가방(`bag`) 안에 초대장이 없다면, 
  * 관람객(`audience`)의 가방(`bag`)에서 티켓 금액만큼의 현금을 꺼내 (`audience.bag.minusAmount(ticket.fee)`) 매표소에 적립한다 (`this.ticketSeller.ticketOffice.plusAmount(ticket.fee)`)
  * 그리고 매표소에 보관돼 있는 티켓을 관람객의 가방 안으로 옮긴다 (`audience.bag.setTicket(ticket)`)

그것은 관람객(`audience`)과 판매원(`ticketSeller`)에 변화가 있을 경우, 극장(`Theater`)도 **함께 변경해야 한다는 것**이다.  
예를 들어 다음의 변화들이 필요하다고 가정해보자.

* 관람객(`audience`)이 가방(`bag`)이 아닌, 모바일 페이로 결제를 한다고 가정해보자.
  * 그럼 `Audience` 클래스에서 `Bag`을 제거해야하고,
  * `Audience`의 `Bag`에 **직접 접근하는** `Theater` 클래스의 `enter` 메소드 역시 수정해야 한다.

이렇게 `Audience`의 변화에 `Theater`까지 영향을 받는 것은 **지나치게 Audience 내부에 대해 많이 알고 있기 때문**이다.  
이렇게 각 객체의 서로에 대한 이해도를 우리는 **의존성**이라고 부른다.  
그리고 **의존성을 관리**하기 위해 코드를 어떻게 배치하느냐가 디자인 혹은 설계이다.  
  
현재의 애플리케이션은 `Theater`가 지나치게 `Audience`와 `TicketSeller` 의존하고 있다.  
(즉, 너무 세세한 구현부까지 `Theater`가 이해하고 있어야만 코드를 작성할 수 있다)  
  
이것을 해결하기 위해서는 각 객체의 세부적인 부분을 적절하게 숨기는 것이다.

* `Theater` 는 관람객이 극장에 입장하는 것만 관리하고
* `Audience` 는 가방안의 현금과 초대장을 처리하고
* `TicketSeller` 는 매표소의 티켓과 판매 요금을 처리한다.


```ts
export class Theater {
    private ticketSeller: TicketSeller;

    constructor(ticketSeller: TicketSeller) {
        this.ticketSeller = ticketSeller;
    }

    public enter(audience: Audience): void {
        this.ticketSeller.sellTo(audience);
    }
}
```

```ts
export class Audience {
    private readonly bag: Bag;

    constructor(bag: Bag) {
        this.bag = bag;
    }

    public buy(ticket: Ticket): number {
        return this.bag.hold(ticket);
    }
}
```

```ts
export class TicketSeller {
    private _ticketOffice: TicketOffice;

    constructor(ticketOffice: TicketOffice) {
        this._ticketOffice = ticketOffice;
    }

    public sellTo(audience: Audience): void {
        this.ticketOffice.sellTicketTo(audience);
    }

    get ticketOffice(): TicketOffice {
        return this._ticketOffice;
    }
}
```

```ts
export class Bag {
    private amount: number;
    private invitation: Invitation;
    private ticket: Ticket;

    constructor() {
    }

    public hold(ticket: Ticket): number {
        if (this.hasInvitation()) {
            this.setTicket(ticket);
            return 0;
        } else {
            this.setTicket(ticket);
            this.minusAmount(ticket.fee);
            return ticket.fee;
        }
    }

    setTicket(value: Ticket): void {
        this.ticket = value;
    }

    public hasInvitation(): boolean {
        return this.invitation != null;
    }

    public minusAmount(amount: number): void {
        this.amount -= amount;
    }
}
```

> 전체 코드는 [Github](https://github.com/jojoldu/blog-code/tree/master/%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98/%EA%B0%9D%EC%B2%B4%EC%A7%80%ED%96%A5%20%EB%94%94%EC%9E%90%EC%9D%B8/src) 을 참고한다.



자바를 비롯한 OOP 언어들은 데이터 (Data) 와 행위 (Method)를 객체안에 통합시켜놓는다.  
각 객체들은 메세지를 통해 서로 연결되어 서로의 행동을 실행시킨다.  
  
흔히들 클래스를 값 객체 (Value Object)로서만 사용하는데, 클래스는 단순한 데이터가 아니다.  
객체의 타입을 안다는 것은 그 객체가 어떻게 행동할지 예상할 수 있다는 것이다.

> 더 많은 예제와 다양한 OOP 주제는 [조영호님의 오브젝트](https://www.kyobobook.co.kr/product/detailViewKor.laf?barcode=9791158391409) 를 참고한다.

## 좋은 디자인

위에서 OOP 에 대해서 이야기를 했지만, OOP를 떠나서 애플리케이션의 좋은 디자인이라 하면 결국 **변화를 대응하기 위해 어떻게 코드를 배치할까**를 고민해야 하는 것이다.  
  
여기서 어려운 점은 **미래가 대비된 코드란 사실상 불가능**하다는 점이다.  
  
예언자가 아닌 이상 **어떤 기능을 추가로 요구 받을지 예측할 수 없다**.  
그렇기 때문에 우리가 할 것은 애플리케이션이 어떤일이 벌어질지 예측하는것이 아니라, **어떤 애플리케이션이든 변하다는 사실을 인지하고, 지금은 그 변경이 무엇인지 예측할 수 없다는 것을 인지하는 것**뿐이다.  
  
미래를 추측하는 디자인을 하지 않는다.  
미래를 대비해서 가능한 여러 선택지를 남겨둘 뿐이다.  
  
정리하면, 좋은 디자인이란 **변화를 손쉽게 받아들일 수 있도록 코드를 배치하는 일**이 된다.  
