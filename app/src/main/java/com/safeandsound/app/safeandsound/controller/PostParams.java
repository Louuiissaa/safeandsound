package com.safeandsound.app.safeandsound.controller;

import java.net.URL;

/**
 * Created by louisapabst on 22.04.17.
 */

public class PostParams {
    String name;
    String password;
    String email;
    String ipAddressRP;
    String url;

    public PostParams(String name, String password, String email, String ipAddressRP, String url){
        this.name = name;
        this.password = password;
        this.email = email;
        this.ipAddressRP = ipAddressRP;
        this.url = url;
    }
}
