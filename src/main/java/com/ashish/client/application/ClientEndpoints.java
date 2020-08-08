package com.ashish.client.application;

import com.ashish.client.utility.DiscoveryClientInstanceProvider;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
@RestController
@RequestMapping(value = "/v1/client")
public class ClientEndpoints {

    @Autowired
    private DiscoveryClientInstanceProvider discoveryClientInstanceProvider;

    @Bean
    public RestTemplate RestTemplate() {
        return new RestTemplate();
    }

    @Autowired
    private RestTemplate restTemplate;
 
    @GetMapping(value="/call-eureka-client")
    public String method() {
        List<ServiceInstance> instances= discoveryClientInstanceProvider.getIntance("CALCULATOR-API");
        String response = restTemplate.getForObject(instances.get(0).getUri() + "/v1/test", String.class);
        return "Instance called is : " + instances.get(0).getUri() + " <br/><br/> And Response : " + response;
    }

    @PostMapping(value="/calculate-eureka-client")
    public String methodCalculator(@RequestBody SimpleCalculatorRequestDTO simpleCalculatorRequestDTO) throws RestClientException, URISyntaxException {
        List<ServiceInstance> instances= discoveryClientInstanceProvider.getIntance("CALCULATOR-API");
        MultiValueMap<String, String> headers= new LinkedMultiValueMap<>();
        headers.set("Content-Type","application/json");
        URI uri=new URI(instances.get(0).getUri().toString()+"/v1/calculate");
        RequestEntity<SimpleCalculatorRequestDTO> requestEntity =
                new RequestEntity<SimpleCalculatorRequestDTO>(simpleCalculatorRequestDTO,headers,HttpMethod.POST,uri);
        ResponseEntity<SimpleCalculatorResponseDTO> response= restTemplate.exchange(requestEntity, SimpleCalculatorResponseDTO.class);
        return "Instance called is : " + uri + " <br/><br/> And Response : " + response.getBody().getResult();
    }
}
