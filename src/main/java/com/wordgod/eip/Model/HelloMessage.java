package com.wordgod.eip.Model;

import javax.validation.constraints.Size;

public class HelloMessage {

    @Size(min=2, max=30) // 1
    private String name;

    @Size(min=10, max=300)// 2
    private String message;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

}
