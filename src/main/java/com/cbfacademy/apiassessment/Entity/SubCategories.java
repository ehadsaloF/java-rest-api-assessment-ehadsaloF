package com.cbfacademy.apiassessment.Entity;

import lombok.ToString;


public enum SubCategories {
    // SubCategory of Food
    Groceries(Category.Food),
    Restaurant(Category.Food),
    OnlineOrder(Category.Food),

    // SubCategory of Transport
    PublicTransport(Category.Transport),
    CarHailing(Category.Transport),
    Fuel(Category.Transport),
    CarPayments(Category.Transport),
    CarInsurance(Category.Transport),

    // SubCategory of Utilities
    Water(Category.Utilities),
    Electricity(Category.Utilities),
    Gas(Category.Utilities),
    Internet(Category.Utilities),

    // SubCategory of Housing
    Rent(Category.Housing),
    MortgagePayment(Category.Housing),
    CouncilTax(Category.Housing),
    Repairs(Category.Housing),

    // SubCategory of Entertainment
    TVLicense(Category.Entertainment),
    Streaming(Category.Entertainment),
    Music(Category.Entertainment),
    Outside(Category.Entertainment),

    // SubCategory of Shopping
    Gifts(Category.Shopping),
    Personal(Category.Shopping),
    Clothes(Category.Shopping),

    // SubCategory of Savings
    Investments(Category.Savings),
    Basic(Category.Savings);

    private final Category type;

    SubCategories(Category type) {
        this.type = type;
    }

    public String toString() {
        return name();
    }


    public enum Category {
        Food, Transport, Utilities, Housing, Entertainment, Shopping, Savings;

        public String toString() {
            return name();
        }
    }

}
