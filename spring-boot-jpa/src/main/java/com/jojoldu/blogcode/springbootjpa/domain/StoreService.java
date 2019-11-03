package com.jojoldu.blogcode.springbootjpa.domain;

import com.jojoldu.blogcode.springbootjpa.querydsl.store.StoreQuerydslRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class StoreService {

    private final StoreQuerydslRepository storeQuerydslRepository;

    @Transactional(readOnly = true)
    public long find() {
        List<Store> stores = storeQuerydslRepository.findAll();
        long productSum = stores.stream()
                .map(Store::getProducts)
                .flatMap(Collection::stream)
                .mapToLong(Product::getPrice)
                .sum();

        stores.stream()
                .map(Store::getEmployees)
                .flatMap(Collection::stream)
                .map(Employee::getName)
                .collect(Collectors.toList());

        return productSum;
    }

    @SuppressWarnings("DuplicatedCode")
    @Transactional(readOnly = true)
    public long findByFetchJoin() {
        List<Store> stores = storeQuerydslRepository.findAllByFetchJoin();
        long productSum = stores.stream()
                .map(Store::getProducts)
                .flatMap(Collection::stream)
                .mapToLong(Product::getPrice)
                .sum();

        stores.stream()
                .map(Store::getEmployees)
                .flatMap(Collection::stream)
                .map(Employee::getName)
                .collect(Collectors.toList());

        return productSum;
    }

    @SuppressWarnings("DuplicatedCode")
    @Transactional(readOnly = true)
    public long findByQuerydsl() {
        List<Store> stores = storeQuerydslRepository.findAllByQuerydsl();
        long productSum = stores.stream()
                .map(Store::getProducts)
                .flatMap(Collection::stream)
                .mapToLong(Product::getPrice)
                .sum();

        stores.stream()
                .map(Store::getEmployees)
                .flatMap(Collection::stream)
                .map(Employee::getName)
                .collect(Collectors.toList());

        return productSum;
    }
}
