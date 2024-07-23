package gift.Service;

import gift.Entity.UserEntity;
import gift.Repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import gift.Exception.UnauthorizedException;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    public Page<UserEntity> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public ResponseEntity<UserEntity> findUserByIdResponse(Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public Optional<UserEntity> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public UserEntity saveUser(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public String generateToken(UserEntity userEntity) {
        Claims claims = createClaims(userEntity);
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims != null && claims.getSubject() != null && !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public Optional<UserEntity> getUserFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            if (claims != null) {
                Long userId = claims.get("userId", Long.class);
                return userRepository.findById(userId);
            }
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid token", e);
        }
        return Optional.empty();
    }

    private Claims createClaims(UserEntity userEntity) {
        Claims claims = Jwts.claims().setSubject(userEntity.getEmail());
        claims.put("userId", userEntity.getId());
        return claims;
    }

    private Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid token", e);
        }
    }
}
