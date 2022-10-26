package com.example.concurrencytest;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConService {

    private final ConRepository conRepository;

    public List<ConDto> getDtoList() {
        return conRepository.findAll()
            .stream()
            .map((conEntity) -> conEntity.toDto())
            .collect(Collectors.toList());
    }

    public ConDto getDtoById(Long id) {
        return conRepository.findById(id)
            .orElseThrow(IllegalArgumentException::new)
            .toDto();
    }

    @Transactional
    public ConDto generateCon(ConDto conDto) {
        return conRepository.save(conDto.toEntity()).toDto();
    }


    @Transactional
    public ConDto modifyAge(Long id, Integer age) {
        ConEntity conEntity = conRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        return conEntity.setAge(age).toDto();
    }
}
