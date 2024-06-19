package pl.kul.facilities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
    private long id;
    private String name;
    private double netPrice;
    private TaxRate taxRate;

    public double getGrossPrice() {
        return Math.round(netPrice * (1.0d + (taxRate.getRate() / 100.0d))*100.0d)/100.0d;
    }

}
