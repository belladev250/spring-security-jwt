package com.example.springandreactauth.controllers;



import com.example.springandreactauth.Services.MyUserDetailsService;
import com.example.springandreactauth.Utils.jwtUtils;
import com.example.springandreactauth.models.jwtRequest;
import com.example.springandreactauth.models.jwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private jwtUtils  jwtUtilToken;

@RequestMapping({"/hello"})
public  String hello(){
    return "hello world";
}

@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
public ResponseEntity<?> createAuthenticationToken(@RequestBody jwtRequest jwt_request)  throws Exception{
    try {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(jwt_request.getUsername(),jwt_request.getPassword())
        );
    }

    catch (BadCredentialsException e){
        throw new Exception("incorrect username or password",e);
    }


    final UserDetails userDetails = myUserDetailsService.loadUserByUsername(jwt_request.getUsername()) ;
    final String jwt = jwtUtilToken.generateToken(userDetails);

    return ResponseEntity.ok(new jwtResponse(jwt));

}


}
