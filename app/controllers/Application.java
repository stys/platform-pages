package controllers;

import com.stys.platform.pages.impl.SimplePages;
import com.stys.platform.pages.impl.SimplePagesPlugin;

import play.Logger;
import play.Play;
import play.libs.F;
import play.mvc.Content;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

	public static F.None<Long> None = new F.None<>();
	
    public static Result index() {
        return ok("Welcome");
    }

    public static Result pages(String namespace, String key) {

        Logger.debug(namespace);
        Logger.debug(key);

        SimplePages pages = Play.application().plugin(SimplePagesPlugin.class).getPagesService();
        F.Option<Content> page = pages.get(namespace, key, None);

        if (page.isEmpty()) return notFound("Not found");
        else return ok(page.get());

    }

}
