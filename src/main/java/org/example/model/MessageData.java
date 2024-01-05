package org.example.model;

import java.io.Serializable;

public class MessageData implements Serializable {
    private String message;
    private String jsonData;

    public MessageData(String message, String jsonData) {
        this.message = message;
        this.jsonData = jsonData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    @Override
    public String toString() {
        return "MessageData{" +
                "message='" + message + '\'' +
                ", jsonData='" + jsonData + '\'' +
                '}';
    }
}
