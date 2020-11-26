package com.wpool.pdd.model;

/**
 *
 */
public class Kc_ord {
    private String phone;
    private String odid;
    private String sno;
    private String pfacevalue;
    private int status;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOdid() {
        return odid;
    }

    public void setOdid(String odid) {
        this.odid = odid;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPfacevalue() {
        return pfacevalue;
    }

    public void setPfacevalue(String pfacevalue) {
        this.pfacevalue = pfacevalue;
    }

    @Override
    public String toString() {
        return "Kc_ord{" +
                "phone='" + phone + '\'' +
                ", odid='" + odid + '\'' +
                ", sno='" + sno + '\'' +
                ", pfacevalue='" + pfacevalue + '\'' +
                ", status=" + status +
                '}';
    }
}
