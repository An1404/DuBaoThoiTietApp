package com.example.btl_dubaothoitiet.dubao;

public class ThoiTiet {
    private String day;
    private int Status;
    private String img;
    private String maxTemp;
    private String MinTemp;

    public ThoiTiet(String day, int status, String img, String maxTemp, String minTemp) {
        this.day = day;
        this.Status = status;
        this.img = img;
        this.maxTemp = maxTemp;
        this.MinTemp = minTemp;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getMinTemp() {
        return MinTemp;
    }

    public void setMinTemp(String minTemp) {
        MinTemp = minTemp;
    }
}
