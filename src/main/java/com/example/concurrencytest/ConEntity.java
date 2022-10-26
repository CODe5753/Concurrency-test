package com.example.concurrencytest;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class ConEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer age;

    public ConDto toDto() {
        return ConDto.builder()
            .id(id)
            .name(name)
            .age(age)
            .build();
    }

    public ConEntity setAge(Integer age) {
        this.age = age;
        return this;
    }
}
