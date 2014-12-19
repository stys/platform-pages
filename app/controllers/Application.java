package controllers;

import com.stys.platform.pages.Pages;
import pages.SimplePagesPlugin;
import play.*;
import play.libs.F;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok("Welcome");
    }

    public static Result pages(String namespace, String key) {

        Logger.debug(namespace);
        Logger.debug(key);

        Pages pages = Play.application().plugin(SimplePagesPlugin.class).getPagesService();
        F.Option<Content> page = pages.get(namespace, key, 0L);

        if (page.isEmpty()) return notFound("Not found");
        else return ok(page.get());

    }

}
