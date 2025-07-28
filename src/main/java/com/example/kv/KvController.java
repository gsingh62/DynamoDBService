package com.example.kv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kv")
public class KvController {

    @Autowired
    KvStore store;

    @PostMapping
    public ResponseEntity<String> put(@RequestParam String key, @RequestParam String value, @RequestHeader(value = "X-Replicated", required = false) String replicationHeader) {
        boolean replicate = replicationHeader == null;
        store.put(key, value, replicate);
        return ResponseEntity.ok("OK");
    }

    @GetMapping
    public ResponseEntity<String> get(@RequestParam String key) {
        String val = store.get(key);
        return val != null ? ResponseEntity.ok(val) : ResponseEntity.notFound().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam String key) {
        store.delete(key);
        return ResponseEntity.ok().build();
    }
}

