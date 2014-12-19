package pages;

import com.stys.platform.pages.Repository;
import models.HtmlPage;

import java.util.List;

/**
 *
 */
public class SimpleRepository implements Repository<String> {

    public void put(String namespace, String key, Long version, String html) {
        HtmlPage page = HtmlPage.create(namespace, key, version, html);
    }

    public String getOr(String namespace, String key, Long version, String default_) {

        // Result
        String result = default_;

        // Get list of all versions
        List<HtmlPage> pages = HtmlPage.find.where()
                .eq("namespace", namespace).eq("key", key)
                .orderBy("version desc")
                .findList();

        // Return most resent version
        if ( !pages.isEmpty() ) {
            result = pages.get(0).html;
        }

        // Done
        return result;
    }

}
