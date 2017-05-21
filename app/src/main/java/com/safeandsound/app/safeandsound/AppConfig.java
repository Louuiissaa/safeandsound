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
        private static String ip = "http://192.168.2.116";

        public static String URL_LOGIN = ip + "/db_login.php";

        // Server user register url
        public static String URL_SIGNUP = ip +"/db_register.php";

        //Server add room url
        public static String URL_ADD_ROOM = ip + "/db_add_room.php";

        //Server add room url
        public static String URL_GET_ROOMS = ip + "/db_get_room.php";

        // Server user temperature url
        public static String URL_TEMP = ip + ":1880/temp";
        //id + ":1880/tempInt?from=23.04.2017&to=24.04.2017
        public static String URL_TEMPINT = ip + ":1880/tempInt?from=";

        // Server user window url
        public static String URL_WINDOW = ip + ":1880/window";

        // Server user livestream url
        public static String URL_LIVESTREAM = ip + ":8081";

        // Server user motion url
        public static String URL_MOTION = ip + ":1880/motion";
        public static String URL_MOTION_PICTURE = ip + ":1880/getPic?id=";

        // Server user Current MQ-135 data url
        public static String URL_MQGas = ip + ":1880/mqgas";
        // id + ":1880/gasInt?from=17.05.2017&to=18.05.2017"
        public static String URL_MQGasInt = ip + ":1880/gasInt?from=";
}