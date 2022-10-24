package uz.pdp.bot.model.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDao {
    private Long id;
    private String fullName;
    private String username;
    private String password;
    private boolean isActive;
}
