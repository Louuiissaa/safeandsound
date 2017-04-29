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

        //Server add room url
        public static String URL_ADD_ROOM = "http://" + ipAddressRP + "/db_add_room.php";

        // Server user temperature url
        public static String URL_TEMP = "http://" + ipAddressRP + "/temp";

        // Server user window url
        public static String URL_WINDOW = "http://" + ipAddressRP + "/window";

        // Server user livestream url
        public static String URL_LIVESTREAM = "http://" + ipAddressRP + ":8081";

        // Server user motion url
        public static String URL_MOTION = "http://" + ipAddressRP + "/motion";

        public String getIpAddressRP() {
                return ipAddressRP;
        }

        public void setIpAddressRP(String ipAddressRP) {
                ipAddressRP = ipAddressRP;
        }*/

        // Server user login url
        public static String URL_LOGIN = "http://192.168.10.53/db_login.php";

        // Server user register url
        public static String URL_SIGNUP = "http://192.168.10.53/db_register.php";

        //Server add room url
        public static String URL_ADD_ROOM = "http://192.168.10.53/db_add_room.php";

        //Server get rooms url
        public static String URL_GET_ROOMS = "http://192.168.10.53/db_get_room.php";

        // Server user temperature url
        public static String URL_TEMP = "http://192.168.10.53:1880/temp";

        // Server user window url
        public static String URL_WINDOW = "http://192.168.10.53:1880/window";

        // Server user livestream url
        public static String URL_LIVESTREAM = "http://192.168.10.53:8081";

        // Server user motion url
        public static String URL_MOTION = "http://192.168.10.53/motion";
}