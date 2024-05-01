package com.example.demo.InputDto.UserManagement.Authentication;

import lombok.Data;

@Data
public class ResponseDetail {
    private boolean success;
    private String description;
    public boolean isSuccess() {
        return success;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    @Override
    public String toString() {
        return "ResponseDetail [success=" + success + ", description=" + description + "]";
    }


}
