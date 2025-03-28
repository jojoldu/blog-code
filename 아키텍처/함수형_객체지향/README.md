# 객체지향 프로그래밍과 함수형 프로그래밍

## 객체지향 프로그래밍

```ts
// 결제 수단을 표현하는 예시
enum PaymentMethod {
  CREDIT_CARD = "CREDIT_CARD",
  NAVER_PAY = "NAVER_PAY",
  KAKAO_PAY = "KAKAO_PAY",
}

// "결제"를 책임지는 객체
class Payment {
  constructor(
    private amount: number,
    private method: PaymentMethod,
    private isPaid: boolean = false
  ) {}

  public pay(): void {
    if (this.isPaid) {
      throw new Error("이미 결제된 주문입니다.");
    }
    // 실제 결제 로직 (카드 승인, PG 연동 등)
    // ...
    this.isPaid = true;
    console.log(`[Payment] ${this.method}로 ${this.amount}원 결제 완료.`);
  }

  public refund(): void {
    if (!this.isPaid) {
      throw new Error("결제가 완료되지 않아 환불할 수 없습니다.");
    }
    // 실제 환불 로직 (PG 연동, 기록 etc.)
    // ...
    this.isPaid = false;
    console.log(`[Payment] ${this.method} 환불 완료.`);
  }

  public isPaymentCompleted(): boolean {
    return this.isPaid;
  }

  public getAmount(): number {
    return this.amount;
  }
}

// "배송"을 책임지는 객체
class Shipping {
  constructor(private address: string, private shipped: boolean = false) {}

  public ship(): void {
    if (this.shipped) {
      throw new Error("이미 배송된 주문입니다.");
    }
    // 실제 배송 로직
    // ...
    this.shipped = true;
    console.log(`[Shipping] ${this.address}로 배송 완료.`);
  }

  public isShipped(): boolean {
    return this.shipped;
  }
}

// "주문"을 책임지는 객체
class Order {
  private payment: Payment | null = null;
  private shipping: Shipping | null = null;

  constructor(
    private orderId: string,
    private items: { name: string; price: number; quantity: number }[]
  ) {}

  // 결제 객체를 생성하고, 결제를 진행
  public processPayment(method: PaymentMethod): void {
    const totalAmount = this.items.reduce(
      (sum, item) => sum + item.price * item.quantity,
      0
    );
    this.payment = new Payment(totalAmount, method);
    this.payment.pay();
  }

  // 결제가 완료된 후 배송 처리
  public processShipping(address: string): void {
    if (!this.payment || !this.payment.isPaymentCompleted()) {
      throw new Error("결제가 완료되지 않았습니다.");
    }
    this.shipping = new Shipping(address);
    this.shipping.ship();
  }

  public getOrderId(): string {
    return this.orderId;
  }

  public getTotalAmount(): number {
    return this.payment ? this.payment.getAmount() : 0;
  }

  public refundOrder(): void {
    if (!this.payment) {
      throw new Error("결제 정보가 없습니다.");
    }
    if (this.shipping && this.shipping.isShipped()) {
      throw new Error("이미 배송된 주문은 환불할 수 없습니다. 반품 절차 필요.");
    }
    // 환불 진행
    this.payment.refund();
  }
}

// 사용 예시
const order = new Order("ORDER-123", [
  { name: "상품A", price: 10000, quantity: 2 },
  { name: "상품B", price: 5000, quantity: 1 },
]);

order.processPayment(PaymentMethod.CREDIT_CARD);  // [Payment] CREDIT_CARD로 25000원 결제 완료.
order.processShipping("서울시 강남구");           // [Shipping] 서울시 강남구로 배송 완료.
```


## 함수형 프로그래밍

```ts
type ComplexOrder = {
  orderId: string;
  items: { productId: string; quantity: number }[];
  isPaid: boolean;
  isShipped: boolean;
  // 실제로 더 많은 필드(재고, 할인, 쿠폰, 회원정보 등등)이 있을 수 있음
};

function validateInventory(order: ComplexOrder): ComplexOrder {
  // 재고 확인 로직 (실제로는 외부 DB 또는 API 호출)
  // FP를 지키려면 사이드이펙트를 최소화해야 하지만, 실제로는 여기서 부수효과 발생 가능
  logger.log(`재고 확인: ${order.orderId}`);
  return { ...order };
}

function payOrder(order: ComplexOrder): ComplexOrder {
  if (order.isPaid) {
    throw new Error("이미 결제된 주문입니다.");
  }
  // 결제 로직 (외부 PG 연동, 승인 등). 부수효과가 많음
  // FP 흉내를 내려면, 상태만 바뀐 새 객체를 돌려준다
  logger.log(`결제 진행: ${order.orderId}`);
  return { ...order, isPaid: true };
}

function shipOrder(order: ComplexOrder): ComplexOrder {
  if (!order.isPaid) {
    throw new Error("결제가 완료되지 않았습니다.");
  }
  logger.log(`배송 진행: ${order.orderId}`);
  return { ...order, isShipped: true };
}

function recordEvent(order: ComplexOrder, eventType: string): ComplexOrder {
  // 실제로는 DB 이벤트 기록, 모니터링, 메시지 큐 전송 등 (모두 부수효과)
  logger.log(`[Event] ${order.orderId} - ${eventType}`);
  return { ...order };
}

// 사용
let complexOrder: ComplexOrder = {
  orderId: "ORD-999",
  items: [{ productId: "PD-ABC", quantity: 2 }],
  isPaid: false,
  isShipped: false,
};

// FP 식 파이프라인
complexOrder = validateInventory(complexOrder);
complexOrder = payOrder(complexOrder);
complexOrder = recordEvent(complexOrder, "PAYMENT_COMPLETED");
complexOrder = shipOrder(complexOrder);
complexOrder = recordEvent(complexOrder, "SHIPPING_COMPLETED");
```

### 결론

FP가 사용되기에 적절한 예:
- 주로 데이터 변환 파이프라인 위주의 로직 (필터, 매핑, 집계 등)
- 외부 상태에 의존하지 않고 순수 함수로 처리할 수 있을 때
- “Simple Made Easy”에서 말하는 “단순성”을 쉽게 확보 가능

FP가 적합하지 않은 예:
- 복잡한 도메인 로직(주문, 결제, 재고, 이벤트 발행, 외부 API 호출 등)이 얽혀 있고, 상태 전이가 연속적으로 발생하는 경우
- 부수효과(Side Effect)가 많고, 여러 부분이 Entangled되어 있어 순수 함수를 지키기 어려울 때
- 이런 경우, FP만으로 문제를 해결하려 하면 오히려 복잡도가 증가하고, “누가 책임지는가?”가 모호해져 유지보수가 힘들어질 수 있음


> 참고: Rich Hickey, Simple Made Easy (한국어 번역)
> - Simple(단순): 요소가 얽혀 있지 않은 상태
> - Easy(쉬움): 접근이나 사용이 용이한 상태

함수형 프로그래밍은 “상태와 로직을 분리”하고 “함수 합성”을 통해 단순화를 추구합니다. 하지만 도메인이 너무 복잡해 여러 요소가 얽혀 있다면, FP만으로는 “단순성”을 유지하기가 쉽지 않다.  

실무에서는 FP와 OOP를 상황에 따라 혼합하거나, 도메인에 맞는 적절한 추상화를 선택해 복잡성을 줄이는 것이 핵심이다.