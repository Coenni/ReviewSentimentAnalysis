package task;

public class SentimentScore {
    private Double positiveScore = 0.0;
    private Double positiveCount = 0.0;
    private Double negativeScore = 0.0;
    private Double negativeCount = 0.0;

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
}
