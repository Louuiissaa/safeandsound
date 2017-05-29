package com.safeandsound.app.safeandsound.model;

import java.util.HashMap;

/**
 * Created by louisapabst on 21.04.17.
 */

public class User {
    //Singleton
    private static User user;

    private long user_id;
    private String username;
    private String email;
    private String iPAddressRP;
    private HashMap<String, String> windows;

    private User(long user_id, String username, String email, String iPAddressRP){
        this.user_id = user_id;
        this.username = username;
        this.email = email;
        this.iPAddressRP = iPAddressRP;
        this.windows = new HashMap<String, String>();
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

    public HashMap<String, String> getWindows() {
        return windows;
    }

    public void setWindows(HashMap<String, String> windows) {
        this.windows = windows;
    }

    /**
     * Bei Erstellung des Objektes User, wenn User bereits erstellt wurde wird das Objekt übergeben
     * @param user_id
     * @param username
     * @param email
     * @param iPAddressRP
     */
    public static void init(long user_id, String username, String email, String iPAddressRP){
        if(user == null){
            user = new User(user_id, username, email, iPAddressRP);
        }
    }

    /**
     * Gibt die Instanz des User Objektes zurück
     * @return
     * @throws Exception
     */
    public static User getInstance() throws Exception {
        if(user != null) {
            return user;
        }else{
            throw new Exception("User Object has not been created");
        }
    }

    public static void reset() {
        user = null;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", iPAddressRP='" + iPAddressRP + '\'' +
                '}';
    }
}
