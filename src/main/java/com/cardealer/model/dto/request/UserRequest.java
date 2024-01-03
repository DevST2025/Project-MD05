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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @NotBlank(message = "Email cannot be left blank.")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "The email is not in the correct format.")
    private String email;
    private String fullName;
    @NotBlank(message = "Phone cannot be left blank.")
    @Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})\\b", message = "The phone number is not in the correct format.")
    @PhoneUnique
    private String phone;
    private String address;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date birthday;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date updatedAt = new Date(System.currentTimeMillis());
    private MultipartFile file;
}
