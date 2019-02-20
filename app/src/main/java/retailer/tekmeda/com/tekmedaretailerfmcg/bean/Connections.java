package retailer.tekmeda.com.tekmedaretailerfmcg.bean;

public class Connections {

    private String StockistId;
    private String RetailerId;
    private String ConnectionStatus,sender;

    public Connections(String stockistId, String retailerId, String connectionStatus, String sender, String id) {
        StockistId = stockistId;
        RetailerId = retailerId;
        ConnectionStatus = connectionStatus;
        this.sender = sender;
        this.id = id;
    }

    private String id;

    public Connections() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStockistId() {
        return StockistId;
    }

    public void setStockistId(String stockistId) {
        StockistId = stockistId;
    }

    public String getRetailerId() {
        return RetailerId;
    }

    public void setRetailerId(String retailerId) {
        RetailerId = retailerId;
    }

    public String getConnectionStatus() {
        return ConnectionStatus;
    }

    public void setConnectionStatus(String connectionStatus) {
        ConnectionStatus = connectionStatus;
    }
}
