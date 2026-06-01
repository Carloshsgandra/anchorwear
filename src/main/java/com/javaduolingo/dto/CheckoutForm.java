package com.javaduolingo.dto;

import jakarta.validation.constraints.NotBlank;

public class CheckoutForm {

    private String cartJson;

    @NotBlank(message = "Nome é obrigatório")
    private String shippingName;

    @NotBlank(message = "CEP é obrigatório")
    private String shippingZip;

    @NotBlank(message = "Rua é obrigatória")
    private String shippingStreet;

    @NotBlank(message = "Número é obrigatório")
    private String shippingNumber;

    private String shippingComplement;

    @NotBlank(message = "Bairro é obrigatório")
    private String shippingNeighborhood;

    @NotBlank(message = "Cidade é obrigatória")
    private String shippingCity;

    @NotBlank(message = "Estado é obrigatório")
    private String shippingState;

    private String shippingType = "PAC";

    @NotBlank(message = "Método de pagamento é obrigatório")
    private String paymentMethod;

    private String couponCode;
    private boolean saveAddress;

    public CheckoutForm() {}

    public String getCartJson() { return cartJson; }
    public void setCartJson(String cartJson) { this.cartJson = cartJson; }
    public String getShippingName() { return shippingName; }
    public void setShippingName(String shippingName) { this.shippingName = shippingName; }
    public String getShippingZip() { return shippingZip; }
    public void setShippingZip(String shippingZip) { this.shippingZip = shippingZip; }
    public String getShippingStreet() { return shippingStreet; }
    public void setShippingStreet(String shippingStreet) { this.shippingStreet = shippingStreet; }
    public String getShippingNumber() { return shippingNumber; }
    public void setShippingNumber(String shippingNumber) { this.shippingNumber = shippingNumber; }
    public String getShippingComplement() { return shippingComplement; }
    public void setShippingComplement(String c) { this.shippingComplement = c; }
    public String getShippingNeighborhood() { return shippingNeighborhood; }
    public void setShippingNeighborhood(String n) { this.shippingNeighborhood = n; }
    public String getShippingCity() { return shippingCity; }
    public void setShippingCity(String shippingCity) { this.shippingCity = shippingCity; }
    public String getShippingState() { return shippingState; }
    public void setShippingState(String shippingState) { this.shippingState = shippingState; }
    public String getShippingType() { return shippingType; }
    public void setShippingType(String shippingType) { this.shippingType = shippingType; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }
    public boolean isSaveAddress() { return saveAddress; }
    public void setSaveAddress(boolean saveAddress) { this.saveAddress = saveAddress; }
}
