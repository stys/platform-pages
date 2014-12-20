import com.stys.platform.pages.impl.SimplePageEntity;

import models.Dummy;
import play.Application;
import play.GlobalSettings;

/**
 *
 */
public class Global extends GlobalSettings {

    @Override
    public void onStart(Application application) {

        if (0 == SimplePageEntity.find.findRowCount()) {
        	
        	SimplePageEntity.create("test", "drive", "<h1>Test</h1><p>drive</p>", "new");
            SimplePageEntity.create("test", "your/strength", "<h1>Test</h1><p>your strength</p>", "old");

        }
        
        if (0 == Dummy.find.findRowCount()) {

            Dummy dummy = new Dummy();
            dummy.txt = "I am dummy";
            dummy.save();

        }

    }

}
