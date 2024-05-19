package code.six.wecode.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {

	private String username;

	private String password1;

//	@NotEmpty(message = "비밀번호 확인은 필수 항목입니다.")
	private String password2;

//	@NotEmpty(message = "이메일은 필수 항목입니다.")
//	@Email(message = "올바른 형식의 이메일 주소를 입력해주세요.")
	private String email;
}
