package retailer.tekmeda.com.tekmedaretailerfmcg.bean;

import java.io.Serializable;

public class Stockists implements Serializable{

    private String firstName, lastName, id, emailid, area, enterpriseName,phone, street,city,address,pinshop;

    public Stockists() {
    }


    public Stockists(String firstName, String lastName, String id, String emailid, String area, String enterpriseName, String phone, String street, String city, String address, String pinshop) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.emailid = emailid;
        this.area = area;
        this.enterpriseName = enterpriseName;
        this.phone = phone;
        this.street = street;
        this.city = city;
        this.address = address;
        this.pinshop = pinshop;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPinshop() {
        return pinshop;
    }

    public void setPinshop(String pinshop) {
        this.pinshop = pinshop;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getArea() {
        return area;
    }

    @Override
    public String toString() {
        return "Stockists{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", id='" + id + '\'' +
                ", emailid='" + emailid + '\'' +
                ", area='" + area + '\'' +
                ", enterpriseName='" + enterpriseName + '\'' +
                '}';
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }
}
