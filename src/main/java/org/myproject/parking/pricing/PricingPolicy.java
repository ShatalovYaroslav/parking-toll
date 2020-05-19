package org.myproject.parking.pricing;

public interface PricingPolicy {
    float getPrice(int hours, float price);
}
