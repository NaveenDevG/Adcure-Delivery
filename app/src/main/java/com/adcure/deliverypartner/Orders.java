package com.adcure.deliverypartner;

public class Orders {
    String date, gaddress, gcity, gname, gphone, gpincode, gstate, uid, paid, paymentid, time, umail, uphone, items, placed, shipped, delivered, invoiceid, cashback, coupon, fivepercentcoupon, couponamount;

    public String getCouponamount() {
        return couponamount;
    }

    public void setCouponamount(String couponamount) {
        this.couponamount = couponamount;
    }

    public String getFivepercentcoupon() {
        return fivepercentcoupon;
    }

    public void setFivepercentcoupon(String fivepercentcoupon) {
        this.fivepercentcoupon = fivepercentcoupon;
    }

    public Orders(String date, String gaddress, String gcity, String gname, String items, String gphone, String gpincode, String gstate, String uid, String paid, String paymentid, String time, String umail, String uphone) {
        this.date = date;
        this.gaddress = gaddress;
        this.gcity = gcity;
        this.gname = gname;
        this.gphone = gphone;
        this.gpincode = gpincode;
        this.gstate = gstate;
        this.uid = uid;
        this.paid = paid;
        this.paymentid = paymentid;
        this.time = time;
        this.umail = umail;
        this.uphone = uphone;
        this.items = items;
    }

    public Orders() {
    }

    public String getCashback() {
        return cashback;
    }

    public void setCashback(String cashback) {
        this.cashback = cashback;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getInvoiceid() {
        return invoiceid;
    }

    public void setInvoiceid(String invoiceid) {
        this.invoiceid = invoiceid;
    }

    public String getPlaced() {
        return placed;
    }

    public void setPlaced(String placed) {
        this.placed = placed;
    }

    public String getShipped() {
        return shipped;
    }

    public void setShipped(String shipped) {
        this.shipped = shipped;
    }

    public String getDelivered() {
        return delivered;
    }

    public void setDelivered(String delivered) {
        this.delivered = delivered;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGaddress() {
        return gaddress;
    }

    public void setGaddress(String gaddress) {
        this.gaddress = gaddress;
    }

    public String getGcity() {
        return gcity;
    }

    public void setGcity(String gcity) {
        this.gcity = gcity;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getGphone() {
        return gphone;
    }

    public void setGphone(String gphone) {
        this.gphone = gphone;
    }

    public String getGpincode() {
        return gpincode;
    }

    public void setGpincode(String gpincode) {
        this.gpincode = gpincode;
    }

    public String getGstate() {
        return gstate;
    }

    public void setGstate(String gstate) {
        this.gstate = gstate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getPaymentid() {
        return paymentid;
    }

    public void setPaymentid(String paymentid) {
        this.paymentid = paymentid;
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

    public String getUphone() {
        return uphone;
    }

    public void setUphone(String uphone) {
        this.uphone = uphone;
    }
}
