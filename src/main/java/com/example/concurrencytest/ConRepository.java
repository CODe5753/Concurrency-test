package com.example.concurrencytest;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConRepository extends JpaRepository<ConEntity, Long> {

}
