package retailer.tekmeda.com.tekmedaretailerfmcg.bean;

/**
 * Created by Viper PC on 5/26/2018.
 */

public class Retailers {

    String firstName,lastName,retailName,emailid,phonenumber,id,city,pin,shopNo,street,state,area,address;

    public Retailers() {
    }

    public Retailers(String firstName, String lastName, String retailName, String emailid, String phonenumber, String id, String city, String pin, String shopNo, String street, String state, String area, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.retailName = retailName;
        this.emailid = emailid;
        this.phonenumber = phonenumber;
        this.id = id;
        this.city = city;
        this.pin = pin;
        this.shopNo = shopNo;
        this.street = street;
        this.state = state;
        this.area = area;
        this.address = address;
    }

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRetailName() {
        return retailName;
    }

    public void setRetailName(String retailName) {
        this.retailName = retailName;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
