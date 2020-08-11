package com.jojoldu.blogcode.querydsl.domain.ad;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jojoldu@gmail.com on 11/08/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@RequiredArgsConstructor
@Service
public class AdItemService {

    private final AdItemQueryRepository queryRepository;
    private final AdBondRepository adBondRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public void createAdBond1 (LocalDate startDate, LocalDate endDate, List<String> orderTypes) {
        List<AdBond> adBonds = queryRepository.createAdBond(startDate, endDate, orderTypes);
        adBondRepository.saveAll(adBonds);
    }

    @Transactional
    public void createAdBond2 (LocalDate startDate, LocalDate endDate, List<String> orderTypes) {
        List<AdBondDto> adBondDtos = queryRepository.createAdBondDto(startDate, endDate, orderTypes);
        List<AdBond> adBonds = adBondDtos.stream()
                .map(this::toAdBond)
                .collect(Collectors.toList());

        adBondRepository.saveAll(adBonds);
    }

    private AdBond toAdBond(AdBondDto dto) {
        Customer customer = customerRepository.findById(dto.getCustomerId()).get();
        return dto.toEntity(customer);
    }

    @Transactional
    public void createAdBond3 (LocalDate startDate, LocalDate endDate, List<String> orderTypes) {
        List<AdBondDto> adBondDtos = queryRepository.createAdBondDto(startDate, endDate, orderTypes);
        List<AdBond> adBonds = adBondDtos.stream()
                .map(AdBondDto::toEntity)
                .collect(Collectors.toList());

        adBondRepository.saveAll(adBonds);
    }
}
