package com.example.multiple_datasources;

import com.example.multiple_datasources.dmEntity.DmEntity;
import com.example.multiple_datasources.dmRepository.DmRepository;
import com.example.multiple_datasources.oltpEntity.OltpEntity;
import com.example.multiple_datasources.oltpRepository.OltpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final DmRepository dmRepository;
    private final OltpRepository oltpRepository;

    @PostMapping("test")
    public void test1() {
        this.dmRepository.save(new DmEntity(null, "dm"));
    }

    @GetMapping("test")
    public ResponseEntity<?> test2() {
        return ResponseEntity.ok(this.dmRepository.findByName("dm"));
    }

    @PostMapping("test2")
    public void test3() {
        this.oltpRepository.save(new OltpEntity(null, "oltp"));
    }

    @GetMapping("test2")
    public ResponseEntity<?> test4() {
        return ResponseEntity.ok(this.oltpRepository.findByName("oltp"));
    }
}
