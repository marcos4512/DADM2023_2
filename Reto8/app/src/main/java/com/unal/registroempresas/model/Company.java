package com.unal.registroempresas.model;

public class Company {

    private int id;
    private String name;
    private String url;
    private String phone;
    private String email;
    private String productService;
    private String classification;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getProductService() {
        return productService;
    }

    public String getClassification() {
        return classification;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProductService(String productService) {
        this.productService = productService;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }
}
