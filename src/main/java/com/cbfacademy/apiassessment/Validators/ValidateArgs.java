package com.cbfacademy.apiassessment.Validators;

import com.cbfacademy.apiassessment.Entity.SubCategories;

import java.time.LocalDate;

// Validates arguments.

public class ValidateArgs {

    //Validates if the given string is a valid budget category,
    // returns True if the category is valid, false otherwise
    public static boolean isValidCategory(String category) {
        try {
            SubCategories.Category.valueOf(category);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }


    //Validates if the given string is a valid budget subcategory,
    // returns True if the subcategory is valid, false otherwise
    public static boolean isValidSubCategory(String subCategory) {
        try {
            SubCategories.valueOf(subCategory);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }


    //Validates if the given string is a valid date,
    // returns True if the date string is valid, false otherwise
    public static boolean isValidDate(String dateString) {
        try {
            LocalDate.parse(dateString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    //Validates if the given amount is greater than zero,
    // returns True if the amount is valid, false otherwise
    public static boolean isAmountValid(double amount) {
        return amount > 0;
    }


    //Validates if the given string can be parsed to a valid amount (double) and is greater than zero,
    // return True if the string is a valid amount, false otherwise.
    public static boolean isAmountValid(String value) {
        double amount;
        try {
            amount = Double.parseDouble(value);
        } catch (Exception e) {
            return false;
        }
        return amount > 0;
    }
}
