package com.brodios.store.domain;

import com.brodios.store.domain.enums.RoleName;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ορίζουμε τον ρόλο ως ENUM string για σαφήνεια: ROLE_ADMIN, ROLE_CUSTOMER
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private RoleName name;

    public Role(RoleName name) {
        this.name = name;
    }
}