package com.blogcode.before;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jojoldu@gmail.com on 2017-02-04
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public interface ContractRepository extends JpaRepository<Contract, Long>{
    Contract findByCommissionType(String commissionType);
    Contract findByCommissionCutting(String commissionCutting);
}
