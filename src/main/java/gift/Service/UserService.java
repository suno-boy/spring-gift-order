package gift.Service;

import gift.DTO.AuthRequestDTO;
import gift.DTO.UserDTO;
import gift.Entity.UserEntity;
import gift.Mapper.UserServiceMapper;
import gift.Repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserServiceMapper userServiceMapper;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserServiceMapper userServiceMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userServiceMapper = userServiceMapper;
    }



    public String loginUser(AuthRequestDTO authRequest) {
        UserEntity userEntity = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("이메일이 잘못 입력되었습니다."));

        if (passwordEncoder.matches(authRequest.getPassword(), userEntity.getPassword())) {
            return generateToken(userEntity);
        } else {
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }
    }


    public Optional<UserDTO> findUserById(Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        return userEntity.map(userServiceMapper::convertToDTO);
    }

    public UserDTO saveUser(UserDTO userDTO) {
        UserEntity userEntity = userServiceMapper.convertToEntity(userDTO);
        UserEntity savedUserEntity = userRepository.save(userEntity);
        return userServiceMapper.convertToDTO(savedUserEntity);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        Optional<UserEntity> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            UserEntity user = existingUser.get();
            user.setEmail(userDTO.getEmail());
            UserEntity updatedUserEntity = userRepository.save(user);
            return userServiceMapper.convertToDTO(updatedUserEntity);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(UserEntity userEntity) {
        Claims claims = Jwts.claims().setSubject(userEntity.getEmail());
        claims.put("userId", userEntity.getId());
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
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject() != null && !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public Optional<UserEntity> getUserFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            Long userId = claims.get("userId", Long.class);
            return userRepository.findById(userId);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
