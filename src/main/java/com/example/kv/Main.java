package com.example.kv;

public class Main {
    public static void main(String[] args) {
        String peername = "http://kv-store-1.kv-store:8080";
        String hostname = "kv-store-1";
        System.out.println(peername.contains(hostname));
    }
}
