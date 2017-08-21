package cn.devit.demo.cucumber.spring.service;

import org.springframework.stereotype.Service;

@Service
public class Dao {

    public String getCardById(String input) {
        return input + "X";
    }

    public void deleteAll() {
        System.out.println("all deleted.");
    }
}
