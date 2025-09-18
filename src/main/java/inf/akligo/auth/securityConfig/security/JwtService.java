package inf.akligo.auth.securityConfig.security;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.Function;
import java.security.Key;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
;



@Service
public class JwtService{
    private long jwtExpiration = 12233;
    private String secretKey= "qwerty";

    public String generateToken(UserDetails userdetails){
        return generateToken(new HashMap<>(), userdetails);
    }
    private  String generateToken (HashMap<String,Object> claims, UserDetails userdetails ){

        return buildToken(claims, userdetails, jwtExpiration);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userdetails,
            long jwtExpiration
    ){
        var authorities = userdetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userdetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .claim("authorities", authorities)
                .signWith(getSignInKey())
                .compact();

    }

    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(String token, UserDetails userdetails){
        final String username = extractUsername(token);
        return (username.equals(userdetails.getUsername())) && !isTokenExpired(token);

    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extratClaim(token, Claims::getExpiration);
    }

    public String extractUsername(String token){
        return extratClaim(token, Claims:: getSubject);
    }
    public <T> T extratClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token){
        return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

    }


}
