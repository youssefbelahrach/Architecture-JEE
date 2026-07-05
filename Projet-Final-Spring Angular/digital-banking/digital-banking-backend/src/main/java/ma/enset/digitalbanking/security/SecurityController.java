package ma.enset.digitalbanking.security;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class SecurityController {

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    /** Renvoie les informations de l'utilisateur authentifié (test du token). */
    @GetMapping("/profile")
    public Authentication authenticated(Authentication authentication) {
        return authentication;
    }

    /**
     * Authentifie l'utilisateur et renvoie un JWT signé.
     * Corps attendu : application/x-www-form-urlencoded (username, password).
     */
    @PostMapping("/login")
    public Map<String, String> login(@RequestParam String username, @RequestParam String password) {

        // 1) Vérifie les identifiants via l'AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        // 2) Construit la chaîne des rôles (ex : "USER ADMIN")
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        // 3) Prépare les claims du token
        Instant instant = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(instant)
                .expiresAt(instant.plus(30, ChronoUnit.MINUTES)) // durée de validité
                .subject(username)
                .issuer("digital-banking")
                .claim("scope", scope)
                .build();

        // 4) Signe le token en HS512
        JwtEncoderParameters params = JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS512).build(), claims);
        String jwt = jwtEncoder.encode(params).getTokenValue();

        return Map.of("access-token", jwt);
    }
}
