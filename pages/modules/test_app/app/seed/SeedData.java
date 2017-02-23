package seed;

import com.google.inject.Singleton;
import com.stys.platform.pages.api.Access;
import com.stys.platform.pages.api.Page;
import com.stys.platform.pages.api.Selector;
import com.stys.platform.pages.api.State;
import com.stys.platform.pages.impl.DefaultEditService;

import javax.inject.Inject;

/** Populate database with initial data */
@Singleton
public class SeedData {

    @Inject
    public SeedData(DefaultEditService pages) {
        Page page = new Page();
        page.owner = "root";
        page.title = "Hello world!";
        page.source = "# Hello world\nHello world";
        page.access = Access.Open;
        page.state = State.Published;
        pages.put(new Selector("blog", "hello-world"), page);
    }

}
