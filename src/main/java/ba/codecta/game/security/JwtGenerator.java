package ba.codecta.game.security;

import io.smallrye.jwt.build.Jwt;

import java.util.Arrays;
import java.util.HashSet;

public class JwtGenerator {
    public static String generateJwtToken(Integer gameId, Integer heroId, String heroName){
        return Jwt.issuer("http://localhost:8080")
                        .upn(heroName)
                        .groups(new HashSet<>(Arrays.asList("Hero")))
                        .claim("gameId", gameId)
                        .claim("heroId", heroId)
                        .sign();
    }
}
