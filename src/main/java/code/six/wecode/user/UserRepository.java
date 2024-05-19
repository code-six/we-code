package code.six.wecode.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/* 기본적인 CRUD 작업을 제공한다 */
public interface UserRepository extends JpaRepository<SiteUser, Long> {
	/* 사용자명을 통해 사용자를 찾는 메서드 */
	Optional<SiteUser> findByusername(String username);
	boolean existsByUsername(String username); /* 사용자 ID 존재 여부 */
}
