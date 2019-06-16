package metrics.model.daos.statistics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Hibernate entity which represents a single record (row) in the Statistics table.
 * @author Seth
 */
@Entity
@Table(name="statistics")
public class Statistic {
	private static final long serialVersionUID = 5132988642201611556L;
	
	private long statisticId;
	private String requestUuid;
	private String url;
	private long requestTime;
	private long responseSize;
	
	/**
	 * @return Primary key for Statistics table.
	 */
	@Id
	@GeneratedValue
	@Column(name="statistic_id", unique = true, nullable = false)
	public long getStatisticId() {
		return statisticId;
	}
	
	/**
	 * @return Secondary unique identifier based on UUID to allow multi-server + single db records to mesh seamlessly.
	 */
	@Column(name="request_uuid", nullable = false)
	public String getRequestUuid() {
		return requestUuid;
	}
	
	@Column(name="url", nullable = false)
	public String getUrl() {
		return url;
	}
	
	/**
	 * @return Time in ms.
	 */
	@Column(name="request_time", nullable = false)
	public long getRequestTime() {
		return requestTime;
	}
	
	/**
	 * @return Size in bytes.
	 */
	@Column(name="response_size", nullable = false)
	public long getResponseSize() {
		return responseSize;
	}

	public void setStatisticId(long statisticId) {
		this.statisticId = statisticId;
	}

	public void setRequestUuid(String requestUuid) {
		this.requestUuid = requestUuid;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

	public void setRequestTime(long requestTime) {
		this.requestTime = requestTime;
	}

	public void setResponseSize(long responseSize) {
		this.responseSize = responseSize;
	}
	
	@Override
	public String toString() {
		return "Statistic [statisticId=" + statisticId + ", url=" + url + ", requestTime=" + requestTime
				+ "ms, responseSize=" + responseSize + " bytes]";
	}

}
