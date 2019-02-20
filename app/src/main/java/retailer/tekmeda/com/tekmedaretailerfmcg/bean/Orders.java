package retailer.tekmeda.com.tekmedaretailerfmcg.bean;

import java.io.Serializable;

public class Orders implements Serializable{

    private String medicineName;
    private String medicineQty;
    private String unit;
    private String retailerId;
    private String stockistId;
    private String status;
    private String orderId;
    private String orderNumber;
    private boolean showOrderNumber;
    private boolean isModified;


    public boolean isShowOrderNumber() {
        return showOrderNumber;
    }

    public void setShowOrderNumber(boolean showOrderNumber) {
        this.showOrderNumber = showOrderNumber;
    }

    public boolean isModified() {
        return isModified;
    }

    public void setModified(boolean modified) {
        isModified = modified;
    }

    public Orders(String medicineName, String medicineQty, String unit, String retailerId, String stockistId, String status, String orderId, String orderNumber, boolean showOrderNumber, boolean isModified, String time) {

        this.medicineName = medicineName;
        this.medicineQty = medicineQty;
        this.unit = unit;
        this.retailerId = retailerId;
        this.stockistId = stockistId;
        this.status = status;
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.showOrderNumber = showOrderNumber;
        this.isModified = isModified;
        this.time = time;
    }

    public String getTime() {

        return time;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String time;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }



    public Orders() {
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getMedicineQty() {
        return medicineQty;
    }

    public void setMedicineQty(String medicineQty) {
        this.medicineQty = medicineQty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(String retailerId) {
        this.retailerId = retailerId;
    }

    public String getStockistId() {
        return stockistId;
    }

    public void setStockistId(String stockistId) {
        this.stockistId = stockistId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Orders(String medicineName, String medicineQty, String unit, String retailerId, String stockistId, String status, String orderId) {



        this.medicineName = medicineName;
        this.medicineQty = medicineQty;
        this.unit = unit;
        this.retailerId = retailerId;
        this.stockistId = stockistId;
        this.status = status;
        this.orderId=orderId;
    }
}
