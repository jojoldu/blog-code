insert into payment(owner_id, pay_date, calculate_code, price, method) values (1, '2017-03-23', 'C001', 20000, 'MOBILE');
insert into payment(owner_id, pay_date, calculate_code, price, method) values (1, '2017-03-23', 'C001', 10000, 'CREDIT_CARD');
insert into payment(owner_id, pay_date, calculate_code, price, method) values (1, '2017-03-23', 'C001', 5000, 'CASH');
insert into payment(owner_id, pay_date, calculate_code, price, method) values (1, '2017-03-23', 'C002', 20000, 'MOBILE');
insert into payment(owner_id, pay_date, calculate_code, price, method) values (1, '2017-03-23', 'C001', 15000, 'CREDIT_CARD');
insert into payment(owner_id, pay_date, calculate_code, price, method) values (1, '2017-04-01', 'C002', 30000, 'MOBILE');

insert into sales(owner_id, pay_date, calculate_code, total_amount, mobile_amount, credit_card_amount, cash_amount) values (1, '2017-03-23', 'C001', 35000, 20000, 10000, 5000);
insert into sales(owner_id, pay_date, calculate_code, total_amount, mobile_amount, credit_card_amount, cash_amount) values (1, '2017-03-23', 'C002', 20000, 20000, 0, 0);
insert into sales(owner_id, pay_date, calculate_code, total_amount, mobile_amount, credit_card_amount, cash_amount) values (1, '2017-03-23', 'C001', 15000, 0, 15000, 0);
insert into sales(owner_id, pay_date, calculate_code, total_amount, mobile_amount, credit_card_amount, cash_amount) values (1, '2017-04-01', 'C002', 30000, 30000, 0, 0);