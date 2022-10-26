package com.example.concurrencytest;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/con")
public class ConController {

    private final ConService conService;

    @GetMapping
    public ResponseEntity<List<ConDto>> getConList() {
        return ResponseEntity.ok(conService.getDtoList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConDto> getConByConId(@PathVariable Long id) {
        return ResponseEntity.ok(conService.getDtoById(id));
    }

    @PostMapping
    public ResponseEntity<ConDto> generateCon(ConDto conDto) {
        return ResponseEntity.ok(conService.generateCon(conDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ConDto> modifyCon(@PathVariable Long id, @RequestParam Integer age) {
        return ResponseEntity.ok(conService.modifyAge(id, age));
    }
}
