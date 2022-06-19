package com.myapp.model;

public class OrderDetail {

    private Long id;

    private Order order;

    private Product product;
    Long productId;

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    private int quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getProductId() {
        return this.product == null ? null : this.product.getId();
    }

    public Float getProductPrice() {
        return this.product == null ? null : this.product.getPrice();
    }

    public Long getOrderId() {
        return this.order == null ? null : this.order.getId();
    }
}
