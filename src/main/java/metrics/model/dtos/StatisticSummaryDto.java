package metrics.model.dtos;

/**
 * Represents a summary of all Statistic records processed when
 * building our Dashboard UI.
 * 
 * When used for requests, these fields contain millisecond values.
 * When used for responses, these fields contain payload sizes measured in bytes.
 * 
 * @author Seth
 */
public class StatisticSummaryDto {
	private long min;
	private long max;
	private long avg;
	
	public StatisticSummaryDto(long min, long max, long avg) {
		this.min = min;
		this.max = max;
		this.avg = avg;
	}
	
	public long getMin() {
		return min;
	}
	public void setMin(long min) {
		this.min = min;
	}
	public long getMax() {
		return max;
	}
	public void setMax(long max) {
		this.max = max;
	}
	public long getAvg() {
		return avg;
	}
	public void setAvg(long avg) {
		this.avg = avg;
	}
	
	
}
