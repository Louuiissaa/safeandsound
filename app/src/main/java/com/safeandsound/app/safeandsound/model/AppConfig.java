package com.safeandsound.app.safeandsound.model;

/**
 * Created by louisapabst on 22.04.17.
 */

public class AppConfig {
        private static String ip = "http://192.168.10.53";
        // Server user login url
        public static String URL_LOGIN = "http://" + ip + "/db_login.php";

        // Server user register url
        public static String URL_SIGNUP = "http://" + ip + "/db_register.php";

        // Server user temperature url
        public static String URL_TEMP = "http://" + ip + "/temp";

        // Server user window url
        public static String URL_WINDOW = "http://" + ip + "/window";

        // Server user livestream url
        public static String URL_LIVESTREAM = "http://" + ip + ":8081";

        // Server user motion url
        public static String URL_MOTION = "http://" + ip + "/motion";

        // Server MQGas url
        public static String URL_MQGas = "http://" + ip + ":1880/mqgas";

        //Server Temp Intervall url
        public static String URL_TEMPINT = "http://" + ip + ":1880/tempInt?from=";

        //Server MQ Gas Intervall url
        public static String URL_MQGasInt = "http://" + ip + ":1880/gasInt?from=";

        //Server Motion Picture url
        public static String URL_MOTION_PICTURE = "http://" + ip + ":1880/getPic?id=";



        public String getIp() {
                return ip;
        }

        public void setIp(String ip) {
                ip = ip;
        }
}