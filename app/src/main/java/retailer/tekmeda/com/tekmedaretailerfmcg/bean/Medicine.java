package retailer.tekmeda.com.tekmedaretailerfmcg.bean;

import java.io.Serializable;

public class Medicine implements Serializable
{

    private String Name,Company,Desc,StockistId,Verified,Generic;

    public Medicine() {

    }

    public Medicine(String name, String company, String desc, String stockistId, String verified, String generic) {
        Name = name;
        Company = company;
        Desc = desc;
        StockistId = stockistId;
        Verified = verified;
        Generic = generic;
    }

    public String getGeneric() {
        return Generic;
    }

    public void setGeneric(String generic) {
        Generic = generic;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getStockistId() {
        return StockistId;
    }

    public void setStockistId(String stockistId) {
        StockistId = stockistId;
    }

    public String getVerified() {
        return Verified;
    }

    public void setVerified(String verified) {
        Verified = verified;
    }
}
