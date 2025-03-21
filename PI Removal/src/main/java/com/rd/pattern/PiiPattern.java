package com.rd.pattern;

import java.util.regex.Pattern;

public class PiiPattern {

    public static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z\\s]+");

    public static final Pattern PHONE_PATTERN = Pattern.compile("\\d{10}");


    public static final Pattern EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");

    // we can add more pattern
}
