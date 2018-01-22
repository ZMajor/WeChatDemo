package com.major.weixin.pojo;

public class Message {
    private String ghsj;
    private String ghks;
    private String sfz;
    private String photo;

    public String getGhsj() {
        return ghsj;
    }

    public void setGhsj(String ghsj) {
        this.ghsj = ghsj;
    }

    public String getGhks() {
        return ghks;
    }

    public void setGhks(String ghks) {
        this.ghks = ghks;
    }

    public String getSfz() {
        return sfz;
    }

    public void setSfz(String sfz) {
        this.sfz = sfz;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "Message{" +
                "ghsj='" + ghsj + '\'' +
                ", ghks='" + ghks + '\'' +
                ", sfz='" + sfz + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
