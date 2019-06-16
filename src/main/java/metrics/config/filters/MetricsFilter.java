package metrics.config.filters;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import metrics.model.daos.statistics.Statistic;
import metrics.model.daos.statistics.StatisticDao;

@Component
@WebFilter("/*")
public class MetricsFilter implements Filter {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	StatisticDao statsDao;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		String url = "";
		String query = "";
		String uuid = UUID.randomUUID().toString();
		long start = System.currentTimeMillis();
		long stop = 0l;
		
		ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);
		
		if (request instanceof HttpServletRequest) {
			url = ((HttpServletRequest) request).getRequestURL().toString();
			((HttpServletResponse) response).addHeader("id", uuid);
			
			if(((HttpServletRequest) request).getQueryString() != null)
				query = "?" + ((HttpServletRequest) request).getQueryString();
		}
		
		try {
            chain.doFilter(request, responseWrapper);
            responseWrapper.copyBodyToResponse();
        } finally {
        	
            stop = System.currentTimeMillis();
            
    		String contentLength = "";
    		if(response instanceof HttpServletResponse) {
    			
    			contentLength = ((HttpServletResponse) response).getHeader("Content-Length");
    			
    			if(contentLength != null) {
    				Statistic stat = new Statistic();
    	    		stat.setUrl(url + query);
    	    		stat.setRequestUuid(uuid);
    	    		stat.setRequestTime(stop - start);
    	    		stat.setResponseSize(Long.parseLong(contentLength));
    	    		statsDao.save(stat);
    			} else {
    				// No content length header detected.
    				Statistic stat = new Statistic();
    	    		stat.setUrl(url + query);
    	    		stat.setRequestUuid(uuid);
    	    		stat.setRequestTime(stop - start);
    	    		stat.setResponseSize(0);
    	    		statsDao.save(stat);
    			}
    		} else {
    			logger.info("Not an HttpServletResponse -> " + response.getContentType());
    		}
    		
    		
        }
	}

}
