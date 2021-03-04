package com.testonline.tools;

public class Userinfo {
    private int id;
    private String password;
    private String identity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    @Override
    public String toString() {
        return "Userinfo{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", identity='" + identity + '\'' +
                '}';
    }
}
