package at.scintillation.talks.meimarie.dto;

/**
 * DTO for storing the sums for a bar chart including their interval.
 *
 * @author <a href="mailto:alexander.rosemann@scintillation.at">Alexander Rosemann</a>
 * @since 1.0.0
 */
public class SumPerInterval {
    private Long interval;
    private Double sum;

    public SumPerInterval() {
    }

    public SumPerInterval(Long interval, Double sum) {

        this.interval = interval;
        this.sum = sum;
    }

    public Long getInterval() {
        return interval;
    }

    public void setInterval(Long interval) {
        this.interval = interval;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }
}
