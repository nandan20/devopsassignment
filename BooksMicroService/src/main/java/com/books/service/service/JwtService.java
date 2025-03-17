package com.books.service.service;


import com.books.service.entity.Users;
import com.books.service.util.JwtUtil;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;

@Service
public class JwtService implements UserDetailsService {

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private AuthenticationManager authenticationManager;

  
    private final RestTemplate restTemplate;
    
   
    @Value("${user.service.url}")
    private String userServiceUrl;
    
    public JwtService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

   
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	String url = userServiceUrl + "/users/byname?userName=" + username;
        ResponseEntity<Users> userObject = restTemplate.getForEntity(url, Users.class);
        Users user = userObject.getBody();
        if (user != null) {
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    getAuthority(user)
            );
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    private Set getAuthority(Users user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRole().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        });
        return authorities;
    }

    private void authenticate(String userName, String userPassword) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
    
    public UserDetails loadUserByUsername(String username, String jwtToken) throws UsernameNotFoundException {
    	String url = userServiceUrl + "/users/byname?userName=" + username;
    	 HttpHeaders headers = new HttpHeaders();
         headers.set("Authorization", "Bearer " + jwtToken);

         // Create an HttpEntity with the headers
         HttpEntity<String> entity = new HttpEntity<>(headers);

         // Make the GET request with the headers
         ResponseEntity<Users> response = restTemplate.exchange(
             url,
             HttpMethod.GET,
             entity,
             Users.class
         );
        Users user = response.getBody();
        if (user != null) {
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    getAuthority(user)
            );
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}