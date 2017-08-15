package com.blogcode.security.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by jojoldu@gmail.com on 2017. 8. 15.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@NoArgsConstructor
@Getter
@Entity
public class UserRoleMap {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_USER_MAP"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "user_role_id", foreignKey = @ForeignKey(name = "FK_USER_ROLE_MAP"))
    private UserRole userRole;

    public UserRoleMap(User user, UserRole userRole) {
        this.user = user;
        this.userRole = userRole;
    }
}
