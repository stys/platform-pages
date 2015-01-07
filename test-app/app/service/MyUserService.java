package service;

import java.util.Arrays;
import java.util.List;

import play.Application;
import play.mvc.Controller;

import com.stys.platform.pages.impl.domain.Role;
import com.stys.platform.pages.impl.domain.User;
import com.stys.platform.pages.impl.domain.UserService;

public class MyUserService extends Controller implements UserService {
	
	public MyUserService(Application application) {
		/* Empty */
	}

	@Override
	public User getUser() {
		return null;
//		return new User() {
//			
//			@Override
//			public List<Role> getRoles() {
//				return Arrays.asList(Role.Moderator);
//			}
//			
//			@Override
//			public String getID() {
//				return "user";
//			}
//		};
	}
	
	
	
}