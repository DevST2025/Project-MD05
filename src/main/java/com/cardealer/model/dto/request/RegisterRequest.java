package com.cardealer.model.dto.request;

import com.cardealer.util.validator.EmailUnique;
import com.cardealer.util.validator.PasswordMatching;
import com.cardealer.util.validator.PhoneUnique;
import com.cardealer.util.validator.UsernameUnique;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatching(password = "password", confirmPassword = "confirmPassword")
public class RegisterRequest {
    @NotBlank(message = "Username cannot be left blank.")
    @Size(min = 6, max = 100, message = "Username must be between 6 and 100 characters long.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username must not contain special characters.")
    @UsernameUnique
    private String username;
    @NotBlank(message = "Email cannot be left blank.")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "The email is not in the correct format.")
    @EmailUnique
    private String email;
    private String fullName;
    @NotBlank(message = "Password cannot be left blank.")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$", message = "Mật khẩu có ít nhất một chữ số, một chữ cái viết hoa, một chữ cái viết thường và một ký hiệu đặc biệt (“@#")
    private String password;
    private String confirmPassword;
    @NotBlank(message = "Phone cannot be left blank.")
    @Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})\\b", message = "The phone number is not in the correct format.")
    @PhoneUnique
    private String phone;
    private String address;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date birthday;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date createdAt = new Date(System.currentTimeMillis());
    private Set<String> listRoles;
}
