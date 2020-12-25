package ba.codecta.game.security;

import io.smallrye.jwt.build.Jwt;

import java.util.Arrays;
import java.util.HashSet;

public class JwtGenerator {
    public static String generateJwtToken(Integer gameId, Integer heroId, String heroName, Integer userId){
        return Jwt.issuer("http://localhost:8080")
                        .upn(heroName)
                        .groups(new HashSet<>(Arrays.asList("Hero", "User")))
                        .claim("gameId", gameId)
                        .claim("heroId", heroId)
                        .claim("userId", userId)
                        .sign();
    }

    public static String generateJwtTokenForUser(Integer userId, String email){
        return Jwt.issuer("http://localhost:8080")
                .upn(email)
                .groups(new HashSet<>(Arrays.asList("User")))
                .claim("userId", userId)
                .sign();
    }
}
