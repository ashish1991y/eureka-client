package com.ashish.client.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
public class DiscoveryClientInstanceProvider {

    @Autowired
    private DiscoveryClient discoveryClient;

    public List<ServiceInstance> getIntance(String serviceId) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
        return instances;
    }
}
