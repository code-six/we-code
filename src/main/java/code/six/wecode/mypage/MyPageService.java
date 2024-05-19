package code.six.wecode.mypage;

import code.six.wecode.DataNotFoundException;
import code.six.wecode.user.SiteUser;
import code.six.wecode.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 사용자 이름 유효성 검증
    public boolean validateUsername(String username) {
        if (!StringUtils.hasText(username) || username.length() < 6 || username.length() > 15) {
            return false;
        }
        return Pattern.matches("^[A-Za-z0-9]+$", username);
    }

    // 사용자 이름 사용 가능 여부 검증
    public boolean isUsernameAvailable(String username) {
        return userRepository.findByusername(username).isEmpty();
    }

    // 비밀번호 유효성 검증
    public boolean validatePassword(String password) {
        return StringUtils.hasText(password) && password.length() >= 10 &&
                Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]+$", password);
    }

    // 이메일 유효성 검증
    public boolean validateEmail(String email) {
        return Pattern.matches("^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", email);
    }

    // 닉네임 업데이트 메소드
    public SiteUser updateNickname(String username, String newNickname) {
        if (!validateUsername(newNickname) || !isUsernameAvailable(newNickname)) {
            throw new IllegalArgumentException("유효하지 않거나 이미 존재하는 사용자 이름입니다.");
        }
        SiteUser user = userRepository.findByusername(username)
                .orElseThrow(() -> new DataNotFoundException("유저를 찾을 수 없습니다."));
        user.setUsername(newNickname);
        return userRepository.save(user);
    }

    // 이메일 업데이트 메소드
    public SiteUser updateEmail(String username, String newEmail) {
        if (!validateEmail(newEmail)) {
            throw new IllegalArgumentException("잘못된 이메일 형식입니다.");
        }
        SiteUser user = userRepository.findByusername(username)
                .orElseThrow(() -> new DataNotFoundException("유저를 찾을 수 없습니다."));
        user.setEmail(newEmail);
        return userRepository.save(user);
    }

    // 패스워드 업데이트 메소드
    public SiteUser updatePassword(String username, String currentPassword, String newPassword) {
        if (!validatePassword(newPassword)) {
            throw new IllegalArgumentException("비밀번호가 요구 조건에 맞지 않습니다.");
        }
        SiteUser user = userRepository.findByusername(username)
                .orElseThrow(() -> new DataNotFoundException("유저를 찾을 수 없습니다."));
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }
}
