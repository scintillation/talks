package at.scintillation.talks.meimarie.dto;

/**
 * Interval for retrieving the sums of transactions for the given time period.
 *
 * @author <a href="mailto:alexander.rosemann@scintillation.at">Alexander Rosemann</a>
 * @since 1.0.0
 */
public enum IntervalType {


    SECOND("1s"),
    MINUTE("1m"),
    HOUR("1h"),
    DAY("1d"),
    WEEK("1w"),
    MONTH("1M"),
    QUARTER("1q"),
    YEAR("1y");

    private String type;

    IntervalType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }


}
