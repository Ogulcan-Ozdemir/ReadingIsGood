package tr.com.readingisgood.app.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class UserDTO {

    @NotBlank(message = "email is mandatory")
    @Email
    private String email;

    @NotBlank(message = "password is mandatory")
    private String password;

}
