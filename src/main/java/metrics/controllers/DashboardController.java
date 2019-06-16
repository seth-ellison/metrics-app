package metrics.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import metrics.model.daos.statistics.Statistic;
import metrics.model.daos.statistics.StatisticDao;
import metrics.model.dtos.StatisticSummaryDto;

@Controller
public class DashboardController {
	
	@Autowired
	StatisticDao statsDao;
	
	/**
	 * The main dashboard of the site.
	 * 
	 * @param model Map of values passed to the template rendering context.
	 * @return View to resolve alongside its map of data.
	 */
	@RequestMapping(path = "/", method = RequestMethod.GET)
	public ModelAndView dashboard(Map<String, Object> model) {
		
		Iterable<Statistic> stats = statsDao.findAll();
		Iterator<Statistic> iter = stats.iterator();
		List<Statistic> samples = new ArrayList<>();
		
		long requestMin = -1;
		long requestMax = -1;
		long requestAvg = -1;
		
		long responseMin = -1;
		long responseMax = -1;
		long responseAvg = -1;
		
		int statCount = 0;
		
		while(iter.hasNext()) {
			Statistic stat = iter.next();
			
			if(statCount < 5) // Collect some sample stat records for display.
				samples.add(stat);
				
			// Process request stats. (Account for min value being impossible, always replace.)
			if(stat.getRequestTime() < requestMin || requestMin == -1)
				requestMin = stat.getRequestTime();
			
			if(stat.getRequestTime() > requestMax)
				requestMax = stat.getRequestTime();
			
			requestAvg += stat.getRequestTime();
			
			// Process response stats. (Account for min value being impossible, always replace.)
			if(stat.getResponseSize() < responseMin || responseMin == -1)
				responseMin = stat.getResponseSize();
			
			if(stat.getResponseSize() > responseMax)
				responseMax = stat.getResponseSize();
			
			responseAvg += stat.getResponseSize();
			statCount++;
		}
		
		if(statCount == 0)
			statCount = 1; // Quick sanity check against divide by zero.
		
		requestAvg = requestAvg / statCount;
		responseAvg = responseAvg / statCount;
		
		StatisticSummaryDto requestSummary = new StatisticSummaryDto(requestMin, requestMax, requestAvg);
		StatisticSummaryDto responseSummary = new StatisticSummaryDto(responseMin, responseMax, responseAvg);
		
		model.put("request", requestSummary);
		model.put("response", responseSummary);
		model.put("samples", samples);
		
		return new ModelAndView("dashboard", model);
	}
	
	/**
	 * Handles querying the Statistics table by ID.  
	 * @param uuid The request/response UUID being looked up.
	 * @return JSON-based Statistic record, or an error string.
	 */
	@RequestMapping(path = "/", method = RequestMethod.POST)
	public ResponseEntity<?> search(@RequestBody String uuid) {
		
		Statistic stat = statsDao.findByRequestUuid(uuid);
		
		if(stat == null)
			return new ResponseEntity<String>("No request found matching uuid: " + uuid, HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<Statistic>(stat, HttpStatus.FOUND);
	}
}
