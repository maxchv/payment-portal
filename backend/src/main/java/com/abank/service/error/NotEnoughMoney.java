package com.abank.service.error;

public class NotEnoughMoney extends Exception {

    private Long paymentId;

    public NotEnoughMoney() {
    }

    public NotEnoughMoney(String message) {
        super(message);
    }

    public NotEnoughMoney(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughMoney(Throwable cause) {
        super(cause);
    }

    public NotEnoughMoney(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public NotEnoughMoney(Long id) {
        paymentId = id;
    }

    public Long getPaymentId() {
        return paymentId;
    }
}
