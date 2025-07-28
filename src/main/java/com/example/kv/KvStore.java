package com.example.kv;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.*;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Component
public class KvStore {
    private final Map<String, String> store = new ConcurrentHashMap<>();
    private static final Logger logger = Logger.getLogger(KvStore.class.getName());

    @Value("${kv.storage.dir:/data}")
    private String storageDir;

    @Value("${kv.peers:}")
    private String[] peers;

    public KvStore() {}

    @PostConstruct
    public void init() {
        File file = new File(storageDir, "kv.json");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                store.putAll(new com.fasterxml.jackson.databind.ObjectMapper().readValue(reader, Map.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void persist() {
        logger.info(String.format("Persisting..."));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(storageDir, "kv.json")))) {
            new com.fasterxml.jackson.databind.ObjectMapper().writeValue(writer, store);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void put(String key, String value, boolean replicate) {
        logger.info(String.format("Putting %s with value %s %s", key, value, replicate));
        store.put(key, value);
        persist();
        if (replicate) {
            String[] filteredPeers = filteredPeers();
            logger.info(String.format("Replicating to %s", Arrays.toString(filteredPeers)));
            PeerReplicator.replicateToPeers(filteredPeers, key, value);
        }
    }

    public String get(String key) {
        logger.info(String.format("Getting %s with value", key));
        return store.get(key);
    }

    public void delete(String key) {
        store.remove(key);
        persist();
    }

   private String[] filteredPeers() {
       String hostname = System.getenv("HOSTNAME");
       return Arrays.stream(peers)
            .filter(p -> !p.contains(hostname))
            .toArray(String[]::new);
   }
}
