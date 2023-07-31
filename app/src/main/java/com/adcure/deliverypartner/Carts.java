package com.adcure.deliverypartner;

public class Carts {
    private String pid, date, discount, quantity, pname, price, time, umail, uname, uphone, pimg, deliverycharge, ordered, presc, imgPresc;

    public Carts(String pid, String pimg, String deliverycharge, String date, String discount, String quantity, String pname, String price, String time, String umail, String uname, String uphone) {
        this.pid = pid;
        this.deliverycharge = deliverycharge;
        this.date = date;
        this.discount = discount;
        this.quantity = quantity;
        this.pname = pname;
        this.price = price;
        this.time = time;
        this.umail = umail;
        this.uname = uname;
        this.uphone = uphone;
        this.pimg = pimg;
    }

    public String getPresc() {
        return presc;
    }

    public void setPresc(String presc) {
        this.presc = presc;
    }

    public String getImgPresc() {
        return imgPresc;
    }

    public void setImgPresc(String imgPresc) {
        this.imgPresc = imgPresc;
    }

    public String getDeliverycharge() {
        return deliverycharge;
    }

    public void setDeliverycharge(String deliverycharge) {
        this.deliverycharge = deliverycharge;
    }

    public Carts() {
    }

    public String getOrdered() {
        return ordered;
    }

    public void setOrdered(String ordered) {
        this.ordered = ordered;
    }

    public String getPimg() {
        return pimg;
    }

    public void setPimg(String pimg) {
        this.pimg = pimg;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUmail() {
        return umail;
    }

    public void setUmail(String umail) {
        this.umail = umail;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUphone() {
        return uphone;
    }

    public void setUphone(String uphone) {
        this.uphone = uphone;
    }
}
