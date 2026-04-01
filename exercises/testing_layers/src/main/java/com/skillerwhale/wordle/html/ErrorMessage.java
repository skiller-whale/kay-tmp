package com.skillerwhale.wordle.html;

public class ErrorMessage {

    public static String renderErrorMessage(String error) {
        if (error == null || error.trim().isEmpty()) {
            return "";
        }

        return "<div class=\"error\">" + error + "</div>\n";
    }
}
