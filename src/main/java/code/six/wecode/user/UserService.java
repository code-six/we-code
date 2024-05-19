package code.six.wecode.user;

import code.six.wecode.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	/* 새로운 SiteUser 객체를 생성하고 비밀번호를 암호화하여 DB에 저장한다 */
	public SiteUser create(String username, String email, String password) {
		SiteUser user = new SiteUser();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));
		this.userRepository.save(user);
		return user;
	}

	/* 사용자명을 통해 SiteUser를 가져온다 */
	public SiteUser getUser(String username) {
		Optional<SiteUser> siteUser = this.userRepository.findByusername(username);
		if (siteUser.isPresent()) {
			return siteUser.get();
		} else {
			throw new DataNotFoundException("siteuser not found");
		}
	}

	/* 사용자 ID 존재 여부 */
	public boolean isUsernameExists(String username) {
		return userRepository.existsByUsername(username);
	}

	public void updateUserProfileImage(Long id, String profileImage) {
		SiteUser user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		user.setProfileImage(profileImage);
		userRepository.save(user);
	}
}
