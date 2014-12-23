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
        	
        	PageEntity.create("test", "drive", "Test drive", "== Test drive Yeah!", "new");
            PageEntity.create("test", "your/strength", "Test your strength", "**Test** your strenght ", "old");

        }
        
        if (0 == Dummy.find.findRowCount()) {

            Dummy dummy = new Dummy();
            dummy.txt = "I am dummy";
            dummy.save();

        }

    }

}
