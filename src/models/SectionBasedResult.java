package models;

public class SectionBasedResult {
    private int examID;
    private String sectionName;
    private int trueNum;
    private int falseNum;
    private float net = trueNum - ((float) falseNum / 4);

    public int getExamID() {
        return examID;
    }

    public void setExamID(int examID) {
        this.examID = examID;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public int getTrueNum() {
        return trueNum;
    }

    public void setTrueNum(int trueNum) {
        this.trueNum = trueNum;
    }

    public int getFalseNum() {
        return falseNum;
    }

    public void setFalseNum(int falseNum) {
        this.falseNum = falseNum;
    }

    public float getNet() {
        return net;
    }

    public void setNet(float net) {
        this.net = net;
    }
}
