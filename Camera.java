package com.example.project;

import java.io.Serializable;

public class Camera implements Serializable {

    String name;
    String ip;

    public Camera(String name, String ip) {
        this.name = name;
        this.ip = ip;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }

}