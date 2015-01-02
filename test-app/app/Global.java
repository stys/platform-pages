import com.stys.platform.pages.impl.models.Page;
import com.stys.platform.pages.impl.models.Revision;

import play.Application;
import play.GlobalSettings;

/**
 *
 */
public class Global extends GlobalSettings {

    @Override
    public void onStart(Application application) {

        if (0 == Revision.find.findRowCount()) {

            com.stys.platform.pages.impl.Page page1 = new com.stys.platform.pages.impl.Page();
            page1.namespace = "test";
            page1.key = "drive";
            page1.status = com.stys.platform.pages.impl.Page.PUBLISHED;
            page1.template = "new";
            page1.title = "Hello";
            page1.source = "Hello";
            page1.content = "Hello";

            Page.create(page1);

        }

    }

}
