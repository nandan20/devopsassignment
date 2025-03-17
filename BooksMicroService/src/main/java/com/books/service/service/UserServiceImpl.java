package com.books.service.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.books.service.entity.Users;


@Service
public class UserServiceImpl implements UserService{

	private final RestTemplate restTemplate;
    
	   
    @Value("${user.service.url}")
    private String userServiceUrl;
    
    private HttpServletRequest request;
    
    public UserServiceImpl(HttpServletRequest request, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.request = request;

    }
    
	@Override
	public Users findByUserId(Integer id) {
		 String url = userServiceUrl + "/users/"+id;
    	 HttpHeaders headers = new HttpHeaders();
         headers.set("Authorization", "Bearer " + request.getHeader("Authorization").substring(7));

         // Create an HttpEntity with the headers
         HttpEntity<String> entity = new HttpEntity<>(headers);

         // Make the GET request with the headers
         ResponseEntity<Users> response = restTemplate.exchange(
             url,
             HttpMethod.GET,
             entity,
             Users.class
         );
         return response.getBody();
	}
	
	

}
