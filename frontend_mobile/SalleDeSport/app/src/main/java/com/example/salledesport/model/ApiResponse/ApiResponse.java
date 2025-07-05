package com.example.salledesport.model.ApiResponse;

public class ApiResponse <T>{
    private String status;
    private String message;

    private Object errors;

    private  T data;

    public String getStatus() {
        return status;
    }

    public boolean isSuccess(){
        return status.equals("success");
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
