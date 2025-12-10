# 객체지향 프로그래밍과 함수형 프로그래밍

최근에 마플 CTO 유인동님의 신간인 [멀티패러다임 프로그래밍](https://github.com/marpple/multi-paradigm-programming) (4월 출간예정, 한빛미디어) 을 운이 좋게 미리 읽었다.  
OOP와 FP에 대한 인동님의 해석을 흥미롭게 읽으면서, 

## 객체지향 프로그래밍

```ts
/**
 * [OOP] Order 객체는 자신의 상태(결제 여부, 배송 여부)를 스스로 책임집니다.
 * 외부에서는 Order 내부의 상태를 직접 바꿀 수 없고, 오직 메서드를 통해서만 요청할 수 있습니다.
 */
class Order {
  // 상태는 철저히 숨깁니다 (Encapsulation)
  private _status: 'CREATED' | 'PAID' | 'SHIPPED' | 'REFUNDED' = 'CREATED';

  constructor(
    public readonly id: string,
    private readonly items: { price: number; quantity: number }[]
  ) {}

  // 계산 로직이 객체 내부에 응집되어 있습니다.
  get totalAmount(): number {
    return this.items.reduce((sum, item) => sum + item.price * item.quantity, 0);
  }

  // 행동(Method): 결제 처리
  // 외부에서는 이 객체가 내부적으로 어떤 상태인지 알 필요 없이, 'pay'라는 메시지만 보내면 됩니다.
  public pay(paymentMethod: string): void {
    if (this._status !== 'CREATED') {
      throw new Error("이미 처리되었거나 취소된 주문입니다.");
    }
    
    // ... 결제 승인 로직 (외부 API 호출 등은 주입받은 서비스로 위임 가능) ...
    
    console.log(`[OOP] 주문 ${this.id}가 ${paymentMethod}로 결제되었습니다.`);
    this._status = 'PAID'; // 상태 변경
  }

  // 행동(Method): 배송 시작
  public ship(address: string): void {
    if (this._status !== 'PAID') {
      throw new Error("결제가 완료된 주문만 배송할 수 있습니다.");
    }
    
    console.log(`[OOP] 주문 ${this.id}가 ${address}로 배송 시작되었습니다.`);
    this._status = 'SHIPPED'; // 상태 변경
  }
}

// [사용 예시]
// 개발자는 Order 객체의 내부 데이터가 꼬였을까봐 걱정할 필요가 없습니다.
// 객체가 스스로 방어하고 있으니까요.
const myOrder = new Order("ORD-001", [{ price: 1000, quantity: 2 }]);

try {
  myOrder.ship("Seoul"); // Error: 결제가 완료된 주문만 배송할 수 있습니다.
} catch (e) {
  myOrder.pay("CreditCard");
  myOrder.ship("Seoul"); // 성공
}
```


## 함수형 프로그래밍

```ts
/**
 * [FP] 데이터(Model)는 멍청할수록(Anemic) 좋습니다. 
 * 메서드 없이 순수한 정보들의 집합입니다.
 */
type OrderState = 
  | { status: 'CREATED' }
  | { status: 'PAID'; paymentMethod: string }
  | { status: 'SHIPPED'; address: string };

type Order = {
  readonly id: string;
  readonly items: { price: number; quantity: number }[];
  readonly state: OrderState; // 상태를 명시적인 데이터 구조로 표현
};

// [FP] 순수 함수들: 입력을 받아서 출력을 반환할 뿐, 외부 세상을 바꾸지 않습니다.

const createOrder = (id: string, items: Order['items']): Order => ({
  id,
  items,
  state: { status: 'CREATED' }
});

// 결제 함수: 입력받은 order를 변경하지 않고, '새로운' order를 반환합니다.
const payOrder = (order: Order, method: string): Order => {
  if (order.state.status !== 'CREATED') {
    throw new Error(`결제 불가: 현재 상태는 ${order.state.status} 입니다.`);
  }
  // 기존 객체 복사(...) 후 새로운 상태 부여 -> 불변성(Immutability) 유지
  return { 
    ...order, 
    state: { status: 'PAID', paymentMethod: method } 
  };
};

const shipOrder = (order: Order, address: string): Order => {
  if (order.state.status !== 'PAID') {
     throw new Error(`배송 불가: 결제가 필요합니다.`);
  }
  return {
    ...order,
    state: { status: 'SHIPPED', address }
  };
};

// [사용 예시 - 파이프라인]
// 데이터가 함수라는 파이프를 통과하며 변해갑니다.
const initialOrder = createOrder("ORD-001", [{ price: 1000, quantity: 2 }]);

// FP에서는 아래와 같이 함수 합성을 통해 로직을 전개합니다.
// (pipe 함수가 있다고 가정하거나, 아래처럼 체이닝)

const paidOrder = payOrder(initialOrder, "CreditCard");
const shippedOrder = shipOrder(paidOrder, "Seoul");

console.log(`[FP] 완료된 주문 상태:`, shippedOrder);
// initialOrder는 여전히 'CREATED' 상태로 남아있습니다. (타임머신처럼 과거 상태 보존)
```

### 결론

#### [비교 분석] 복잡성을 다루는 태도의 차이

| 특징 | 객체지향 (OOP) | 함수형 (FP) |
| :--- | :--- | :--- |
| **세계관** | **시뮬레이션 (Simulation)** | **데이터 처리 (Data Processing)** |
| **접근법** | "누가(Object) 무엇을 책임질 것인가?" | "데이터가 어떤 과정을 거쳐 변환되는가?" |
| **상태 관리** | 상태를 객체 내부에 숨기고 보호함 (은닉) | 상태 변경을 피하고 새로운 상태를 만듦 (불변) |
| **코드 재사용** | 상속, 다형성, 인터페이스 구현 | 함수 합성, 고차 함수 (Map, Reduce 등) |


우리는 흔히 OOP와 FP 중 무엇이 더 우월한지 논쟁하곤 한다. 하지만 유인동 님의 책과 여러 예제를 통해 알 수 있는 진실은, 이 둘이 **'상태(State)와 복잡성'을 다루는 방식이 다를 뿐**이라는 점이다.

1. **OOP가 강력한 순간**: 
   * 개별 개체(Entity)의 정체성이 중요하고, 그 개체의 상태가 시간의 흐름에 따라 복잡하게 변할 때.
   * 예: UI 컴포넌트, 게임 캐릭터, 복잡한 상태 머신.
   * **"신호등은 스스로 색을 바꿀 책임을 진다."**

2. **FP가 강력한 순간**: 
   * 입력 데이터가 명확하고, 일련의 변환 과정을 거쳐 결과를 도출해야 할 때.
   * 예: 데이터 분석, 결제 정산 로직, 비동기 데이터 파이프라인.
   * **"신호등의 색은 시간과 규칙이라는 함수의 결과값일 뿐이다."**

**Simple Made Easy**의 관점에서 본다면:
* **OOP**는 관련된 데이터와 코드를 묶어(Encapsulation) 우리 뇌가 인식하기 **쉽게(Easy)** 만들어 줍니다. (친숙함)
* **FP**는 데이터와 로직을 떼어놓음(Decoupling)으로써 시스템의 요소들이 서로 얽히지 않게 **단순하게(Simple)** 유지합니다. (구조적 단순함)

따라서 모던 프로그래밍에서는 이 둘을 이분법적으로 나누기보다, **"도메인 모델의 핵심 로직은 순수 함수(FP)로 작성하여 테스트 용이성을 확보하고, 전체적인 구조와 인터페이스는 객체(OOP)로 감싸서 사용성을 높이는"** 하이브리드 전략이 가장 실용적인 해답이 될 것이다.