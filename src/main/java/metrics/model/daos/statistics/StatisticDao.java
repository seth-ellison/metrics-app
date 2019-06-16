package metrics.model.daos.statistics;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * This interface defines all the properties of the derived queries
 * used to interact with the Statistics database table.
 * 
 * @author Seth
 *
 */
public interface StatisticDao extends PagingAndSortingRepository<Statistic, Integer> {
	
	/**
	 * Query all stats for a given endpoint url.
	 * 
	 * @param url The endpoint we want stats for
	 * @return A list of all stats related to the specified endpoint, or null.
	 */
	public List<Statistic> findAllByUrl(String url);
	
	/**
	 * Query a single record from the Statistics table by UUID.
	 * @param uuid The request/response stat record's unique identifier.
	 * @return A single matching statistic record, or null.
	 */
	public Statistic findByRequestUuid(String uuid);
}