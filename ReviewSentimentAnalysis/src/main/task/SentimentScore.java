package task;

public class SentimentScore {
    private Double positiveScore = 0.0;
    private Double positiveCount = 0.0;
    private Double negativeScore = 0.0;
    private Double negativeCount = 0.0;
    private Double rpd;
    private Double lg;
    Double getPositiveScore() {
        return positiveScore;
    }

    private void setPositiveScore(Double positiveScore) {
        this.positiveScore = positiveScore;
    }

    Double getPositiveCount() {
        return positiveCount;
    }

    public void setPositiveCount(Double positiveCount) {
        this.positiveCount = positiveCount;
    }

    public Double getLg() {
        return lg;
    }

    public void setLg(Double lg) {
        this.lg = lg;
    }

    Double getNegativeScore() {
        return negativeScore;
    }

    public void setNegativeScore(Double negativeScore) {
        this.negativeScore = negativeScore;
    }

    Double getNegativeCount() {
        return negativeCount;
    }

    public void setNegativeCount(Double negativeCount) {
        this.negativeCount = negativeCount;
    }

    void incrementPositiveScore(Double scoreValue) {
        positiveScore+=scoreValue;
    }

    void incrementPositiveCount() {
        positiveCount++;
    }

    void incrementNegativeScore(Double scoreValue) {
        negativeScore+=scoreValue;
    }

    void incrementNegativeCount() {
        negativeCount++;
    }

    public void setRpd(Double rpd) {
        this.rpd = rpd;
    }

    public Double getRpd() {
        return rpd;
    }
}
