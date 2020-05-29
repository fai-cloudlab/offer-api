package com.cloudlab.offer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fujitsu.cloudlab.commons.exception.ApiErrorHandler;
import com.fujitsu.cloudlab.commons.http.RestConfig;

@SpringBootApplication
@ImportAutoConfiguration({RestConfig.class, ApiErrorHandler.class})
public class OfferApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(OfferApiApplication.class, args);
  }
}
