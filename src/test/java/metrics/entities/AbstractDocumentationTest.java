package metrics.entities;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;

import org.junit.Before;
import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.templates.TemplateFormats;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import metrics.config.filters.MetricsFilter;

/**
 * This class provides a boilerplate setup for writing documentation-generating tests.
 * It also provides a series of must-override functions which enforce a naming standard for test
 * functions.
 * 
 * This is a simple foundation, a more feature-rich approach would include pass-through logic for a
 * security system.
 * 
 * @author seth.ellison
 *
 */
public abstract class AbstractDocumentationTest {
		
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	MetricsFilter filter;
	
	@Rule
	public JUnitRestDocumentation restDocumentation =
			new JUnitRestDocumentation("target/generated-snippets");
	
	private MockMvc mvc;
	
	private RestDocumentationResultHandler documentationHandler;
	
	protected abstract void getOne() throws Exception;
	protected abstract void getAll() throws Exception;
	protected abstract void create() throws Exception;
	protected abstract void update() throws Exception;
	protected abstract void remove() throws Exception;
	
	/**
	 * This code runs before EACH test, setting up some basic rules & config.
	 */
	@Before
	public void setup() {
		
		
		// By default (can be overridden on function level) document all mvc service calls
		// and strip out authorization tokens.
		this.documentationHandler = document("{class-name}/{method-name}",
				preprocessRequest(removeHeaders("Authorization")));
		
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.addFilter(filter, "/*")
				.apply(MockMvcRestDocumentation.documentationConfiguration(this.restDocumentation)
						.snippets().withTemplateFormat(TemplateFormats.markdown())) // Emit Markdown Docs
				.alwaysDo(this.documentationHandler)
				.build();	
	}
	
	public MockMvc getMvc() {
		return this.mvc;
	}
}
