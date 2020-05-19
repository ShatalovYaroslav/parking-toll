package org.myproject.parking.model;

public interface PricingPolicy {
    int getPrice(int hours, int price);
}
