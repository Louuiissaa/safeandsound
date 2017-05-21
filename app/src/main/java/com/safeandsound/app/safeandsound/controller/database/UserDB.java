package com.safeandsound.app.safeandsound.controller.database;

import android.content.Context;

/**
 * Created by louisapabst on 20.05.17.
 */

public class UserDB {

        private long user_id;
        private String username;
        private String password;
        private String email;
        private String iPAddressRP;


        public UserDB(){

        }

        public UserDB(long user_id, String username, String password, String email, String iPAddressRP){
            this.user_id = user_id;
            this.username = username;
            this.password = password;
            this.email = email;
            this.iPAddressRP = iPAddressRP;
        }

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

        public String getIPAddressRP() {
            return iPAddressRP;
        }

        public void setIPAddressRP(String iPAddressRP) {
            this.iPAddressRP = iPAddressRP;
        }

}
