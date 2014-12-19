package models;

import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.sql.Timestamp;

/**
 *
 */
@Entity
public class HtmlPage extends Model {

    @Id
    public Long id;

    @Lob
    public String namespace;

    public String key;

    public Long version;

    @Lob
    public String html;

    @CreatedTimestamp
    public Timestamp createDateTime;

    @UpdatedTimestamp
    public Timestamp updateDateTime;

    public static Finder<Long, HtmlPage> find =
            new Finder<Long, HtmlPage>(Long.class, HtmlPage.class);

    public static HtmlPage create(String namespace, String key, Long version, String html) {

        HtmlPage page = new HtmlPage();
        page.namespace = namespace;
        page.key = key;
        page.html = html;
        page.version = version;
        page.save();
        return page;

    }

}
