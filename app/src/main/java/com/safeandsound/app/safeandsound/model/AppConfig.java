package com.safeandsound.app.safeandsound.model;

/**
 * Created by louisapabst on 22.04.17.
 */

public class AppConfig {
        private static String ipAddressRP;
        // Server user login url
        public static String URL_LOGIN = "http://" + ipAddressRP + "/db_login.php";

        // Server user register url
        public static String URL_SIGNUP = "http://" + ipAddressRP + "/db_register.php";

        // Server user temperature url
        public static String URL_TEMP = "http://" + ipAddressRP + "/temp";

        // Server user window url
        public static String URL_WINDOW = "http://" + ipAddressRP + "/window";

        // Server user livestream url
        public static String URL_LIVESTREAM = "http://" + ipAddressRP + ":8081";

        // Server user motion url
        public static String URL_MOTION = "http://" + ipAddressRP + "/motion";

        // Server MQGas url
        public static String URL_MQGas = "http://" + ipAddressRP + ":1880/mqgas";

        //Server Temp Intervall url
        public static String URL_TEMPINT = "http://" + ipAddressRP + ":1880/tempInt?from=";

        //Server MQ Gas Intervall url
        public static String URL_MQGasInt = "http://" + ipAddressRP + ":1880/gasInt?from=";

        //Server Motion Picture url
        public static String URL_MOTION_PICTURE = "http://" + ipAddressRP + ":1880/getPic?id=";



        public String getIpAddressRP() {
                return ipAddressRP;
        }

        public void setIpAddressRP(String ipAddressRP) {
                ipAddressRP = ipAddressRP;
        }
}