package service;

import com.stys.platform.pages.api.UserService;
import play.api.Configuration;
import play.api.Environment;
import play.api.inject.Binding;
import play.api.inject.Module;
import scala.collection.Seq;

public class MyUserServiceModule extends Module {

    @Override
    public Seq<Binding<?>> bindings(Environment environment, Configuration configuration) {
        return seq(
            bind(UserService.class).to(MyUserService.class)
        );
    }
}
