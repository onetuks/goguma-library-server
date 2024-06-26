package com.onetuks.gogumalibraryserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.onetuks")
public class GogumaLibraryServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(GogumaLibraryServerApplication.class, args);
  }
}
