package com.example.nationalpetition.config.feignClient;

import com.example.nationalpetition.NationalPetitionApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackageClasses = NationalPetitionApplication.class)
@Configuration
public class FeignClientConfig {

}
