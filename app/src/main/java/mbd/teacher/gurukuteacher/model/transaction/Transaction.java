package mbd.teacher.gurukuteacher.model.transaction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Naufal on 21/02/2018.
 */

public class Transaction {
    @SerializedName("transactionID")
    @Expose
    private Integer transactionID;
    @SerializedName("bookID")
    @Expose
    private Integer bookID;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("paymentMethod")
    @Expose
    private String paymentMethod;
    @SerializedName("total_price")
    @Expose
    private Integer totalPrice;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    public Transaction(Integer transactionID, Integer bookID, Integer status, String paymentMethod, Integer totalPrice) {
        this.transactionID = transactionID;
        this.bookID = bookID;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.totalPrice = totalPrice;
    }

    public Integer getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(Integer transactionID) {
        this.transactionID = transactionID;
    }

    public Integer getBookID() {
        return bookID;
    }

    public void setBookID(Integer bookID) {
        this.bookID = bookID;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
