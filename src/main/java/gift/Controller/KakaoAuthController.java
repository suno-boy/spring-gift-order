package gift.Controller;

import gift.DTO.KakaoUserDTO;
import gift.Service.KakaoAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoAuthController {

    @Autowired
    private KakaoAuthService kakaoAuthService;

    @GetMapping("/auth/kakao/callback")
    public ResponseEntity<KakaoUserDTO> kakaoCallback(@RequestParam String code) {
        KakaoUserDTO userDTO = kakaoAuthService.handleKakaoLogin(code);
        return ResponseEntity.ok(userDTO);
    }
}
