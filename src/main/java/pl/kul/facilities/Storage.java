package pl.kul.facilities;

import java.util.*;
import java.util.stream.Collectors;

public class Storage {
    private final Map<Product, Double> products;

public Storage() {
        products = new TreeMap<>(Comparator.comparing(Product::getId));
    }

    public Set<Long> getProductIds() {
        return products.keySet().stream()
                .map(Product::getId)
                .collect(Collectors.toSet());
    }

    public void addProduct(Product product, double quantity) {
        products.put(product, products.getOrDefault(product, 0.0) + quantity);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    public void removeProduct(Product product, double quantity) {
        products.put(product, products.get(product) - quantity);
    }

    public List<Product> getProducts() {
        return products.keySet().stream().toList();
    }

    public Map<Product, Double> getProductsMap() {
        return products;
    }

    public Product getProductById(long id) {
        return products.keySet().stream()
                .filter(product -> product.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Product getProductByName(String productName) {
        return products.keySet().stream()
                .filter(product -> product.getName().equals(productName))
                .findFirst()
                .orElse(null);
    }
}
