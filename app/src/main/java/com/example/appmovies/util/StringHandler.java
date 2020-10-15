package com.example.appmovies.util;

public class StringHandler {

    // useful function to format dates
    public static String formatDate(String date) {
        String[] parts = date.split("-");
        if (parts.length < 3) return date;
        return parts[2] + '/' + parts[1] + '/' + parts[0];
    }
}
