package at.scintillation.talks.meimarie.dto;

import org.joda.time.DateTime;

/**
 * Created by admin on 14.04.2016.
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
