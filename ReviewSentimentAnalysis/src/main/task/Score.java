package task;

public class Score {
    private Double val=0.0;
    private Double m=1.0;

    Double getVal() {
        return val;
    }

    void setVal(Double val) {
        this.val = val;
    }

    Double getM() {
        return m;
    }

    void setM(Double m) {
        this.m = m;
    }

    @Override
    public String toString() {
        return "{\"value\":\""+val+"\",\"m\":\""+m+"\"}";
    }
}
