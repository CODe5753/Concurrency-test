package com.example.concurrencytest;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConDto {

    private Long id;
    private String name;
    private Integer age;

    public ConEntity toEntity() {
        return ConEntity.builder()
            .name(name)
            .age(age)
            .build();
    }

    @Override
    public String toString() {
        return "ConDto{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", age=" + age +
            '}';
    }
}
