package gift.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import gift.DTO.KakaoUserDTO;
import gift.Entity.KakaoUserEntity;
import gift.Mapper.KakaoUserMapper;

import java.util.Map;

@Service
public class KakaoAuthService {

    @Autowired
    private KakaoUserService kakaoUserService;

    @Autowired
    private KakaoOAuthService kakaoOAuthService;

    @Autowired
    private KakaoUserMapper kakaoUserMapper;

    public KakaoUserDTO handleKakaoLogin(String code) {
        Map<String, String> tokens = kakaoOAuthService.getTokens(code);
        String accessToken = tokens.get("access_token");
        String refreshToken = tokens.get("refresh_token");
        KakaoUserDTO kakaoUserDTO = kakaoOAuthService.getUserInfo(accessToken);

        return kakaoUserService.saveOrUpdateUser(kakaoUserDTO, accessToken, refreshToken);
    }

    public String refreshAccessToken(String kakaoId) {
        KakaoUserEntity userEntity = kakaoUserService.findByKakaoId(kakaoId);
        if (userEntity != null) {
            String refreshToken = userEntity.getRefreshToken();
            Map<String, String> tokens = kakaoOAuthService.refreshAccessToken(refreshToken);
            String newAccessToken = tokens.get("access_token");
            userEntity.setAccessToken(newAccessToken);
            kakaoUserService.saveOrUpdateUser(kakaoUserMapper.toDTO(userEntity), newAccessToken, refreshToken);
            return newAccessToken;
        }
        throw new RuntimeException("유저를 찾을 수가 없습니다.");
    }
}
