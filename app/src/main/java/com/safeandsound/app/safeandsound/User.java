package com.safeandsound.app.safeandsound;

/**
 * Created by louisapabst on 21.04.17.
 */

public class User {
        private long user_id;
        private String username;
        private String password;
        private String email;
        private String iPAddressRP;

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getiPAddressRP() {
        return iPAddressRP;
    }

    public void setiPAddressRP(String iPAddressRP) {
        this.iPAddressRP = iPAddressRP;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", iPAddressRP='" + iPAddressRP + '\'' +
                '}';
    }
}

