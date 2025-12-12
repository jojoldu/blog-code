# 객체지향 프로그래밍과 함수형 프로그래밍

최근에 마플 CTO 유인동님의 신간인 [멀티패러다임 프로그래밍](https://github.com/marpple/multi-paradigm-programming) (4월 출간예정, 한빛미디어) 을 운이 좋게 미리 읽었다.  
OOP와 FP에 대한 인동님의 해석을 흥미롭게 읽으면서, 

## 객체지향 프로그래밍

```ts
class Order {
  private _status: 'CREATED' | 'PAID' | 'SHIPPED' | 'REFUNDED' = 'CREATED';

  constructor(
    public readonly id: string,
    private readonly items: { price: number; quantity: number }[]
  ) {}

  // 외부에는 읽기 전용으로만 상태를 노출합니다
  get status() {
    return this._status;
  }

  get totalAmount(): number {
    return this.items.reduce((sum, item) => sum + item.price * item.quantity, 0);
  }

  // 행동(Method): 결제 처리
  // 외부에서는 'pay'라는 메시지만 보내면 됩니다. 내부 상태 검증은 객체가 책임집니다.
  pay(paymentMethod: string): void {
    if (this._status !== 'CREATED') {
      throw new Error('이미 처리된 주문입니다.');
    }
    console.log(`주문 ${this.id}가 ${paymentMethod}로 결제되었습니다.`);
    this._status = 'PAID';
  }

  ship(address: string): void {
    if (this._status !== 'PAID') {
      throw new Error('결제가 완료된 주문만 배송할 수 있습니다.');
    }
    console.log(`주문 ${this.id}가 ${address}로 배송 시작되었습니다.`);
    this._status = 'SHIPPED';
  }

  refund(): void {
    if (this._status === 'CREATED' || this._status === 'REFUNDED') {
      throw new Error('환불할 수 없는 상태입니다.');
    }
    console.log(`주문 ${this.id}가 환불 처리되었습니다.`);
    this._status = 'REFUNDED';
  }
}

// [사용 예시]
const order = new Order('ORD-001', [{ price: 1000, quantity: 2 }]);

// 잘못된 순서로 호출해도 객체가 스스로 방어합니다
order.ship('Seoul');  // ❌ Error: 결제가 완료된 주문만 배송할 수 있습니다.
order.pay('CreditCard');
order.ship('Seoul');  // ✅ 성공
```


## 함수형 프로그래밍

```ts
/**
 * [FP] 데이터는 순수한 값(Value)입니다.
 * 행위를 포함하지 않고, 무엇인지(What)만 표현합니다.
 */
type OrderStatus = 
  | { tag: 'CREATED' }
  | { tag: 'PAID'; paymentMethod: string }
  | { tag: 'SHIPPED'; address: string }
  | { tag: 'REFUNDED' };

type Order = {
  readonly id: string;
  readonly items: ReadonlyArray<{ price: number; quantity: number }>;
  readonly status: OrderStatus;
};

// [순수 함수들] 입력 → 출력. 외부 상태를 변경하지 않습니다.

const createOrder = (id: string, items: Order['items']): Order => ({
  id,
  items,
  status: { tag: 'CREATED' }
});

const getTotalAmount = (order: Order): number =>
  order.items.reduce((sum, item) => sum + item.price * item.quantity, 0);

// 기존 Order를 변경하지 않고, 새로운 Order를 반환합니다 (불변성)
const payOrder = (order: Order, method: string): Order => {
  if (order.status.tag !== 'CREATED') {
    throw new Error(`결제 불가: 현재 상태는 ${order.status.tag}입니다.`);
  }
  return { ...order, status: { tag: 'PAID', paymentMethod: method } };
};

const shipOrder = (order: Order, address: string): Order => {
  if (order.status.tag !== 'PAID') {
    throw new Error('배송 불가: 결제가 필요합니다.');
  }
  return { ...order, status: { tag: 'SHIPPED', address } };
};

const refundOrder = (order: Order): Order => {
  if (order.status.tag === 'CREATED' || order.status.tag === 'REFUNDED') {
    throw new Error('환불 불가: 현재 상태에서는 환불할 수 없습니다.');
  }
  return { ...order, status: { tag: 'REFUNDED' } };
};

// [사용 예시] 데이터가 함수라는 파이프를 통과하며 변환됩니다
const pipe = <T>(...fns: Array<(arg: T) => T>) => (initial: T): T =>
  fns.reduce((v, fn) => fn(v), initial);

const processOrder = pipe(
  (order: Order) => payOrder(order, 'CreditCard'),
  (order: Order) => shipOrder(order, 'Seoul')
);

const initialOrder = createOrder('ORD-001', [{ price: 1000, quantity: 2 }]);
const completedOrder = processOrder(initialOrder);

console.log(initialOrder.status);   // { tag: 'CREATED' } - 원본 불변
console.log(completedOrder.status); // { tag: 'SHIPPED', address: 'Seoul' }
```

### 결론

#### [비교 분석] 복잡성을 다루는 태도의 차이

| 특징 | 객체지향 (OOP) | 함수형 (FP) |
| :--- | :--- | :--- |
| **세계관** | **시뮬레이션 (Simulation)** | **데이터 처리 (Data Processing)** |
| **접근법** | "누가(Object) 무엇을 책임질 것인가?" | "데이터가 어떤 과정을 거쳐 변환되는가?" |
| **상태 관리** | 상태를 객체 내부에 숨기고 보호함 (은닉) | 상태 변경을 피하고 새로운 상태를 만듦 (불변) |
| **코드 재사용** | 상속, 다형성, 인터페이스 구현 | 함수 합성, 고차 함수 (Map, Reduce 등) |


OOP와 FP 중 무엇이 더 우월한지 논쟁하는 것은 의미가 없다. 이 둘은 상태와 복잡성을 다루는 방식이 다를 뿐이다.

OOP가 빛나는 순간
- 개체의 **정체성(identity)**이 중요할 때
- 상태가 시간에 따라 복잡하게 변하고, 그 변화를 캡슐화하고 싶을 때
- 예: UI 컴포넌트, 게임 캐릭터, 디바이스 드라이버
- "주문은 스스로 자신의 상태를 관리할 책임이 있다."

FP가 빛나는 순간
- 데이터의 변환 과정을 명확히 추적하고 싶을 때
- 테스트와 디버깅이 중요할 때 (순수 함수는 입력만 같으면 항상 같은 출력)
- 예: 데이터 파이프라인, 정산 로직, API 응답 변환
- "주문 상태는 일련의 함수를 통과한 결과값이다."

Rich Hickey의 Simple Made Easy 관점에서 보면:

- OOP는 관련된 것들을 한 곳에 모아(Encapsulation) 인지적으로 다루기 쉽게(Easy) 만든다.
- FP는 데이터와 로직을 분리하고 얽힘을 제거하여 구조적으로 단순하게(Simple) 유지한다.

따라서 실용적인 접근은 이 둘을 상호보완적으로 사용하는 것이다:

"도메인의 핵심 비즈니스 로직은 순수 함수로 작성하여 테스트와 추론을 쉽게 하고,
시스템의 경계와 인터페이스는 객체로 감싸 사용성과 캡슐화를 확보한다."