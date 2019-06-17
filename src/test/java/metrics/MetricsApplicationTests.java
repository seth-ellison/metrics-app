package metrics;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.halLinks;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;

import metrics.constants.MediaTypes;
import metrics.entities.AbstractDocumentationTest;
import metrics.model.daos.statistics.Statistic;
import metrics.model.daos.statistics.StatisticDao;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=MetricsApplication.class, webEnvironment=WebEnvironment.RANDOM_PORT)
public class MetricsApplicationTests extends AbstractDocumentationTest {

	private String uri = "/api/statistics";
	private String dashboardUri = "/";
	
	@Autowired
	private StatisticDao statDao;
	
	private Statistic dummyStat;
	
	/**
	 * Put a record into the temporary in-memory database.
	 */
	@Before
	public void setup() {
		super.setup();
		
		// Warm up the table with a few records before each query.
		Statistic stat = new Statistic();
		stat.setRequestUuid(UUID.randomUUID().toString());
		stat.setResponseSize((long) (Math.random() * 2000)); // 0-1999 bytes
		stat.setRequestTime((long) (Math.random() * 300)); // 0-299ms
		stat.setUrl("http://localhost:8080/test-setup-url");
		dummyStat = statDao.save(stat); // Keep a fresh reference to the most recent test-setup record tossed into the DB.
	}
	
	/**
	 * Test querying for a single record from the dashboard endpoint.
	 * @throws Exception
	 */
	@Test
	public void dashboardSearch() throws Exception {
		this.getMvc().perform(post(this.dashboardUri)
				.content(dummyStat.getRequestUuid())
				.accept(MediaTypes.HAL_JSON)) 
		.andExpect(status().isFound())
		.andExpect(header().exists("id"))
		.andDo(document("{class-name}/{method-name}"));
	}
	
	/**
	 * Bog-standard RESTful endpoint behaviors for the repository. These usually don't need to be tested.
	 * Their behavior is automatically generated, but writing explicit tests for them DOES give us
	 * "free" markdown docs for how they work. Good for interacting with other dev systems.
	 */

	@Test
	@Override
	public void getOne() throws Exception {
		this.getMvc().perform(get(this.uri + "/" + dummyStat.getStatisticId())
				.accept(MediaTypes.HAL_JSON)) 
		.andExpect(header().exists("id"))
		.andExpect(status().isOk()) 
		.andDo(document("{class-name}/{method-name}", links(halLinks(),
				linkWithRel("self").description("Reference to self in repository."),
				linkWithRel("statistic").description("Optional reference to self in repository"))));
	}

	/**
	 * Verify that we can query the first page of results from the /statistics endpoint.
	 */
	@Test
	@Override
	public void getAll() throws Exception {
		this.getMvc().perform(get(this.uri).accept(MediaTypes.HAL_JSON)) 
		.andExpect(status().isOk())
		.andExpect(header().exists("id"))
		.andDo(document("{class-name}/{method-name}", links(halLinks(),
				linkWithRel("self").description("Reference to self in repository."),
				linkWithRel("search").description("Search this repository."),
				linkWithRel("profile").description("Overview of this endpoint"))));
		
	}

	/**
	 * Verify that we cannot trigger DELETE actions via the /statistics endpoint
	 */
	@Test
	@Override
	public void remove() throws Exception {
		this.getMvc().perform(delete(this.uri + "/" + dummyStat.getStatisticId())
				.accept(MediaTypes.HAL_JSON)) 
		.andExpect(header().exists("id"))
		.andExpect(status().isMethodNotAllowed());
	}
	
	/**
	 * Verify that stat creation functions.
	 */
	@Test
	@Override
	public void create() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		
		Gson gson = new Gson();
		Statistic stat = new Statistic();
		stat.setRequestUuid(UUID.randomUUID().toString());
		stat.setResponseSize((long) (Math.random() * 2000)); // 0-1999 bytes
		stat.setRequestTime((long) (Math.random() * 300)); // 0-299ms
		stat.setUrl("http://localhost:8080/create-test-url");
		
		String json = gson.toJson(stat);
		
		this.getMvc().perform(post(this.uri)
				.content(json)
				.headers(headers)
				.accept(MediaTypes.HAL_JSON)) 
		.andExpect(status().isCreated())
		.andExpect(header().exists("id"))
		.andDo(document("{class-name}/{method-name}"));
	}

	/**
	 * Verify that updating statistic records via the /statistics endpoint works.
	 */
	@Test
	@Override
	public void update() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		
		Gson gson = new Gson();
		dummyStat.setResponseSize((long) (Math.random() * 2000)); // 0-1999 bytes
		dummyStat.setRequestTime((long) (Math.random() * 300)); // 0-299ms
		dummyStat.setUrl("http://localhost:8080/update-test-url");
		String json = gson.toJson(dummyStat);
		
		this.getMvc().perform(put(this.uri + "/" + dummyStat.getStatisticId())
				.content(json)
				.headers(headers)
				.accept(MediaTypes.HAL_JSON)) 
		.andExpect(status().isOk())
		.andExpect(header().exists("id"))
		.andDo(document("{class-name}/{method-name}"));
	}

}
