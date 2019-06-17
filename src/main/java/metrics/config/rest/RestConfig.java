package metrics.config.rest;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult;

/**
 * Simply exists to make our REST api endpoints expose the primary key of entities they represent. 
 * @author seth.ellion
 *
 */
@Configuration
public class RestConfig {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	RepositoryRestConfiguration config;
	
	@PostConstruct
	public void restConfig() {
		
		config.setReturnBodyOnCreate(true);
		
		/*
		 *  In order to process entities sent back from POST responses in the
		 *  content body, we have to expose the primary key to get marshalling
		 *  to work.
		 *  
		 */
		
		FastClasspathScanner scanner = new FastClasspathScanner("metrics.model.daos");
		ScanResult result = scanner.scan();
		
		List<String> classNames = result.getNamesOfAllClasses();
		List<Class<?>> classes = result.classNamesToClassRefs(classNames);
		
		for(Class<?> c : classes) {
			logger.info("Exposing: " + c.getCanonicalName());
			config.exposeIdsFor(c);
		}
	}
}
