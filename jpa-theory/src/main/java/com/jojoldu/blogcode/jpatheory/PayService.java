package com.jojoldu.blogcode.jpatheory;

import com.jojoldu.blogcode.jpatheory.domain.Pay;
import com.jojoldu.blogcode.jpatheory.domain.PayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

@Slf4j
@RequiredArgsConstructor
@Service
public class PayService {

    private final PayRepository payRepository;
    private final EntityManagerFactory entityManagerFactory;

    public void updateNative(Long id, String tradeNo) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        Pay pay = em.find(Pay.class, id);
        tx.begin();
        pay.changeTradeNo(tradeNo);
        tx.commit();
    }

    @Transactional
    public void update(Long id, String tradeNo) {
        Pay pay = payRepository.getOne(id);
        pay.changeTradeNo(tradeNo);
    }
}
