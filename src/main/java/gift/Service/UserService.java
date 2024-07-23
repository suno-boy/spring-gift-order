package gift.Service;

import gift.DTO.UserDTO;
import gift.Entity.UserEntity;
import gift.Mapper.UserServiceMapper;
import gift.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceMapper userServiceMapper;

    public List<UserDTO> findAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll();
        return userServiceMapper.convertToUserDTOs(userEntities);
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
}
