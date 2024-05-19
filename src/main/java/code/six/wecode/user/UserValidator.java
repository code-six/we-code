package code.six.wecode.user;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.ValidationUtils;

@Component
public class UserValidator implements Validator {

    private final UserService userService;

    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserCreateForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserCreateForm form = (UserCreateForm) target;

        // 사용자 ID 검증
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "field.required", "사용자 ID는 필수 항목입니다.");
        if (form.getUsername() != null && !form.getUsername().matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,15}$")) {
            errors.rejectValue("username", "username.pattern", "6자 이상 15자 이하의 영문과 숫자 조합으로 입력해주세요.");
        }
        if (userService.isUsernameExists(form.getUsername())) {
            errors.rejectValue("username", "username.exists", "이미 사용 중인 사용자 ID입니다. 다른 ID를 시도해 보세요.");
        }

        // 비밀번호 검증
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password1", "field.required", "비밀번호는 필수 항목입니다.");
        if (form.getPassword1() != null && !form.getPassword1().matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{10,}$")){
            errors.rejectValue("password1", "password.pattern", "10자 이상의 영문, 숫자, 특수 문자 조합으로 입력해주세요.");
        }

        // 비밀번호 확인 검증
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password2", "field.required", "비밀번호 확인은 필수 항목입니다.");
        if (!form.getPassword1().equals(form.getPassword2())) {
            errors.rejectValue("password2", "password.mismatch", "비밀번호가 서로 일치하는지 확인해주세요.");
        }

        // 이메일 검증
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "field.required", "이메일은 필수 항목입니다.");
        if (form.getEmail() != null && !form.getEmail().matches("^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")) {
        errors.rejectValue("email", "email.invalid", "올바른 형식의 이메일 주소를 입력해주세요.");}
    }
}