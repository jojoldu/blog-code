package com.blogcode;

import com.blogcode.domain.Payment;
import com.blogcode.domain.Sales;
import com.blogcode.dto.PaymentDto;
import com.blogcode.dto.SalesConverter;
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
						.price(20000)
						.method(Payment.Method.MOBILE)
						.build(),
				Payment.builder()
						.ownerId(1L)
						.payDate(LocalDate.of(2017, 3, 23))
						.calculateCode("C001")
						.price(10000)
						.method(Payment.Method.CREDIT_CARD)
						.build(),
				Payment.builder()
						.ownerId(1L)
						.payDate(LocalDate.of(2017, 3, 23))
						.calculateCode("C001")
						.price(5000)
						.method(Payment.Method.CASH)
						.build(),
				Payment.builder()
						.ownerId(1L)
						.payDate(LocalDate.of(2017, 3, 23))
						.calculateCode("C002")
						.price(20000)
						.method(Payment.Method.MOBILE)
						.build(),
				Payment.builder()
						.ownerId(2L)
						.payDate(LocalDate.of(2017, 3, 23))
						.calculateCode("C001")
						.price(15000)
						.method(Payment.Method.CREDIT_CARD)
						.build(),
				Payment.builder()
						.ownerId(1L)
						.payDate(LocalDate.of(2017, 4, 1))
						.calculateCode("C002")
						.price(30000)
						.method(Payment.Method.MOBILE)
						.build()
		);
	}

	@Test
	public void test_payment분류() {
		List<Sales> salesList = SalesConverter.createSalesList(PaymentDto.classify(payments));
		Sales sales = salesList.get(0);

		assertThat(sales.getTotalAmount(), is(35000));
		assertThat(sales.getMobileAmount(), is(20000));
		assertThat(sales.getCreditCardAmount(), is(10000));
		assertThat(sales.getCashAmount(), is(5000));



	}

}
