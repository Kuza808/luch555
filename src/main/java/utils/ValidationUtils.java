package com.restaurant.luch.utils;

public class ValidationUtils {

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && phone.length() >= 10;
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
}
