package service;

import com.stys.platform.pages.api.Role;
import com.stys.platform.pages.api.User;
import com.stys.platform.pages.api.UserService;

import java.util.Arrays;
import java.util.List;

public class MyUserService implements UserService {

    @Override
    public User getUser() {
        return new User() {
            @Override
            public String getID() {
                return "root";
            }

            @Override
            public List<Role> getRoles() {
                return Arrays.asList(Role.Administrator, Role.Moderator, Role.User);
            }
        };
    }

}
