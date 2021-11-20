package com.example.springandreactauth.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class jwtUtils {

    private final long  JWT_TOKEN_VALIDITY = 5*60*60;
    private String SECRET_KEY = "jwt_Key";

    // claim is like payload
    public String getUsername(String token){
        return getClaim(token, Claims::getSubject);
    }

    public Date getExpiration(String token){
        return getClaim(token,Claims::getExpiration);
    }

    public <T> T getClaim(String token , Function<Claims,T> claimsResolver){
        final Claims claims = getAllClaims(token);
        return  claimsResolver.apply(claims);
    }

    private Claims getAllClaims(String token){
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token){
        return getExpiration(token).before(new Date());
    }

     public  String generateToken(UserDetails userDetails){
        Map<String , Object> claims = new HashMap<>();
        return createToken(claims,userDetails.getUsername());
    }

    private String createToken(Map<String , Object> claims,String subject){
        return  Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY*1000))
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY).compact();

    }

    public Boolean validateToken(String token,UserDetails userDetails){
        final String username =getUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));

    }




}
