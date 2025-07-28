package com.example.kv;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class PeerReplicator {
    public static void replicateToPeers(String[] peers, String key, String value) {
        for (String peer : peers) {
            try {
                String data = "key=" + URLEncoder.encode(key, "UTF-8") + "&value=" + URLEncoder.encode(value, "UTF-8");
                URL url = new URL(peer + "/kv");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("X-Replicated", "true");
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(data.getBytes());
                }

                try (var is = conn.getInputStream()) {
                    while(is.read() != -1) {}
                }
            } catch (Exception e) {
                System.err.println("Replication to " + peer + " failed: " + e.getMessage());
            }
        }
    }
}

