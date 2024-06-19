package pl.kul.facilities;

import lombok.Getter;

@Getter

public enum TaxRate {
    A(23),
    B(8),
    C(5),
    D(0);

    private final int rate;

    TaxRate(int rate) {
        this.rate = rate;
    }

    public static TaxRate getTaxRate(int vat) {
        return switch (vat) {
            case 23 -> A;
            case 8 -> B;
            case 5 -> C;
            case 0 -> D;
            default -> throw new IllegalArgumentException("Invalid VAT rate");
        };
    }
}
