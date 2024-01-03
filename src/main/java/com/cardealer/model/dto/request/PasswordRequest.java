package com.cardealer.model.dto.request;

import com.cardealer.util.validator.PasswordMatching;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatching(password = "newPass", confirmPassword = "confirmNewPass")
public class PasswordRequest {
    @NotBlank(message = "Password cannot be left blank.")
    private String oldPass;

    @NotBlank(message = "Password cannot be left blank.")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$", message = "Mật khẩu có ít nhất một chữ số, một chữ cái viết hoa, một chữ cái viết thường và một ký hiệu đặc biệt (“@#")
    private String newPass;
    private String confirmNewPass;
}
