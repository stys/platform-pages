import static org.junit.Assert.*;
import static play.test.Helpers.*;
import global.Global;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import play.Configuration;
import play.libs.F;

import com.stys.platform.pages.Result;
import com.stys.platform.pages.Service;
import com.stys.platform.pages.impl.repository.Repository;
import com.stys.platform.pages.impl.domain.Access;
import com.stys.platform.pages.impl.domain.Page;
import com.stys.platform.pages.impl.domain.State;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class DatabaseRepositoryTest {
	
	private Configuration configuration;
	
	@Before
	public void init() {
		// Read configuration 
		Config config = ConfigFactory.parseFile(new File("conf/application.conf"));
		// Additional configuration for in-memory database
		Config db = ConfigFactory.parseMap(inMemoryDatabase());
		// Mix configurations
		configuration = new Configuration(db.withFallback(config));
	}

	@Test
	public void testGlobal() {
		
		running(fakeApplication(configuration.asMap(), new Global()), new Runnable() {
			@Override
			public void run() {
				
			}
		});
		
	}
	
	@Test
	public void testPut() {
		
		running(fakeApplication(configuration.asMap()), new Runnable() {
			@Override
			public void run() {
							
				// Create a repository service
				Service<Result<Page>, Page> repository = 
						new Repository(null, null);
				
				// Prepare a test page
				Page page = new Page();
				page.namespace = "alice";
				page.key = "down-the-rabbit-hole";
				page.access = Access.Private;
				page.state = State.Deleted;
				page.template = "a";
				page.title = "Alice in Wonderland";
				page.source = 
	            	"# Alice's Adventures in Wonderland\n "
	            	+ "## CHAPTER I. Down the Rabbit-Hole"
	            	+ "Alice was beginning to get very tired of sitting by her sister on the bank, and of having nothing to do: once or twice she had peeped into the book her sister was reading, but it had no pictures or conversations in it, 'and what is the use of a book,' thought Alice 'without pictures or conversations?' ";
				page.summary = "Cаммари";
				page.thumb = "tiger.png";
				page.description = "Printing";
				page.keywords = "Printer";
				
	            // Put
	            repository.put(page, page.namespace, page.key, new F.None<Long>());
				
	            // Get line count
	            int page_count = com.stys.platform.pages.impl.models.PageEntity.find.findRowCount();
	            int revision_count = com.stys.platform.pages.impl.models.RevisionEntity.find.findRowCount();
	            
	            assertEquals(2, page_count);
	            assertEquals(3, revision_count);
	            
			}		
		});
		
		
	}
	
	
	

}
