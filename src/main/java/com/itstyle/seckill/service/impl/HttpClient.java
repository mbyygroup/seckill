package com.itstyle.seckill.service.impl;

import io.swagger.models.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpClient {
    public String client(String url, HttpMethod method, MultiValueMap<String,String> params){
        RestTemplate client=new RestTemplate();
        HttpHeaders headers=new HttpHeaders();
        //提交方式为表单提交
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String,String>> requestEntity=new HttpEntity<MultiValueMap<String,String>>(params,headers);
        //执行http请求
        ResponseEntity<String> response=client.exchange(url, org.springframework.http.HttpMethod.POST,requestEntity,String.class);
        return response.getBody();
    }
}
