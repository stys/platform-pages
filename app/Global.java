import models.HtmlPage;
import play.Application;
import play.GlobalSettings;

/**
 *
 */
public class Global extends GlobalSettings {

    @Override
    public void onStart(Application application) {

        if (0 == HtmlPage.find.findRowCount()) {

            HtmlPage.create("test", "your/strength", 0L, "<h1>Test</h1><p>Test</p>");
            HtmlPage.create("test", "drive", 0L, "<h1>Test 2</h1><p>Test 2</p>");

        }

    }

}
