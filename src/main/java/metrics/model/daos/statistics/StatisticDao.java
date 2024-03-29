package metrics.model.daos.statistics;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * This interface defines all the properties of the derived queries
 * used to interact with the Statistics database table.
 * 
 * @author Seth
 *
 */
public interface StatisticDao extends PagingAndSortingRepository<Statistic, Long> {
	
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
	
	// Disable destructive endpoint commands. e.g. DELETE /api/statistics/:id 
	
	/**
	 * Deletes the entity with the given id.
	 *
	 * @param id must not be {@literal null}.
	 * @throws IllegalArgumentException in case the given {@code id} is {@literal null}
	 */
	@RestResource(exported = false)
	@Override
	public void deleteById(Long id);
	
	/**
	 * Deletes a given entity.
	 *
	 * @param entity
	 * @throws IllegalArgumentException in case the given entity is {@literal null}.
	 */
	@RestResource(exported = false)
	@Override
	void delete(Statistic entity);

	/**
	 * Deletes the given entities.
	 *
	 * @param entities
	 * @throws IllegalArgumentException in case the given {@link Iterable} is {@literal null}.
	 */
	@RestResource(exported = false)
	@Override
	void deleteAll(Iterable<? extends Statistic> entities);

	/**
	 * Deletes all entities managed by the repository.
	 */
	@RestResource(exported = false)
	@Override
	void deleteAll();
}