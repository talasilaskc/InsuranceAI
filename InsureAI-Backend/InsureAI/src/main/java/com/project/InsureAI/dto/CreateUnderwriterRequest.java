package com.project.InsureAI.dto;

public class CreateUnderwriterRequest {
    private String fullName;
    private String email;
    private String password;

    public CreateUnderwriterRequest() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "CreateUnderwriterRequest{" +
                "name='" + fullName + '\'' +
                ", email='" + email + '\'' +
                '}';  // Don't include password in toString for security reasons
    }
}
