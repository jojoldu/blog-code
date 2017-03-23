package com.blogcode;

import com.blogcode.domain.Payment;
import com.blogcode.domain.Sales;
import com.blogcode.dto.PaymentDto;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ApplicationTest {

	private List<Payment> payments;

	@Before
	public void setup() {
		//given
		payments = Arrays.asList(
				Payment.builder()
						.ownerId(1L)
						.payDate(LocalDate.of(2017, 3, 23))
						.calculateCode("C001")
						.price(10000)
						.paymentMethod(Payment.Method.CREDIT_CARD)
						.build(),
				Payment.builder()
						.ownerId(1L)
						.payDate(LocalDate.of(2017, 3, 23))
						.calculateCode("C001")
						.price(20000)
						.paymentMethod(Payment.Method.MOBILE)
						.build(),
				Payment.builder()
						.ownerId(1L)
						.payDate(LocalDate.of(2017, 3, 23))
						.calculateCode("C001")
						.price(5000)
						.paymentMethod(Payment.Method.CASH)
						.build(),
				Payment.builder()
						.ownerId(1L)
						.payDate(LocalDate.of(2017, 3, 23))
						.calculateCode("C002")
						.price(20000)
						.paymentMethod(Payment.Method.MOBILE)
						.build(),
				Payment.builder()
						.ownerId(2L)
						.payDate(LocalDate.of(2017, 3, 23))
						.calculateCode("C001")
						.price(15000)
						.paymentMethod(Payment.Method.CREDIT_CARD)
						.build(),
				Payment.builder()
						.ownerId(1L)
						.payDate(LocalDate.of(2017, 4, 1))
						.calculateCode("C002")
						.price(30000)
						.paymentMethod(Payment.Method.MOBILE)
						.build()
		);
	}

	@Test
	public void test_payment분류() {
		Stream<PaymentDto> paymentDtos = PaymentDto.toStream(payments);
		Sales sales = new Sales();

		assertThat(sales.getTotalAmount(), is(100000));
		assertThat(sales.getMobileAmount(), is(100000));
		assertThat(sales.getCreditCardAmount(), is(100000));
		assertThat(sales.getCashAmount(), is(100000));



	}

}
