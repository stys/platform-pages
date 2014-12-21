import com.stys.platform.pages.impl.PageEntity;

import models.Dummy;
import play.Application;
import play.GlobalSettings;

/**
 *
 */
public class Global extends GlobalSettings {

    @Override
    public void onStart(Application application) {

        if (0 == PageEntity.find.findRowCount()) {
        	
        	PageEntity.create("test", "drive", "Test drive", "<h1>Test</h1><p>drive</p>", "new");
            PageEntity.create("test", "your/strength", "Test your strength", "<h1>Test</h1><p>your strength</p>", "old");

        }
        
        if (0 == Dummy.find.findRowCount()) {

            Dummy dummy = new Dummy();
            dummy.txt = "I am dummy";
            dummy.save();

        }

    }

}
