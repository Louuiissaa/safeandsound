package com.safeandsound.app.safeandsound;

/**
 * Created by louisapabst on 22.04.17.
 */

public class AppConfig {
        /*private static String ipAddressRP;
        // Server user login url
        public static String URL_LOGIN = "http://" + ipAddressRP + "/db_login.php";

        // Server user register url
        public static String URL_SIGNUP = "http://" + ipAddressRP + "/db_register.php";

        // Server user temperature url
        public static String URL_TEMP = "http://" + ipAddressRP + "/temp";

        public String getIpAddressRP() {
                return ipAddressRP;
        }

        public void setIpAddressRP(String ipAddressRP) {
                this.ipAddressRP = ipAddressRP;
        }*/

        // Server user login url
        public static String URL_LOGIN = "http://192.168.0.100/db_login.php";

        // Server user register url
        public static String URL_SIGNUP = "http://192.168.0.100/db_register.php";

        //Server add room url
        public static String URL_ADD_ROOM = "http://192.168.0.100/db_add_room.php";

        // Server user temperature url
        public static String URL_TEMP = "http://192.168.0.100/temp";

        // Server user window url
        public static String URL_WINDOW = "http://192.168.0.100/window";

        // Server user livestream url
        public static String URL_LIVESTREAM = "http://192.168.0.100:8081";

        // Server user motion url
        public static String URL_MOTION = "http://192.168.0.100/motion";
}