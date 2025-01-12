package webstaraunt;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StoreInventory {
    private final Map<Integer, Product> inventory;

    public StoreInventory() {
        this.inventory = new HashMap<>();
    }

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (product.getId() < 0) {
            throw new IllegalArgumentException("Product id must be non-negative");
        }
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Product price must be non-negative");
        }
        if (product.getQuantity() < 0) {
            throw new IllegalArgumentException("Product quantity must be non-negative");
        }

        if (inventory.containsKey(product.getId())) {
            throw new IllegalArgumentException("Product already exists with Id " + product.getId());
        }

        inventory.put(product.getId(), product);
    }

    public boolean hasProduct(int productId) {
        return inventory.containsKey(productId);
    }

    public Optional<Product> getProduct(int productId) {
        return Optional.ofNullable(inventory.get(productId));
    }

    private Product getProductOrThrow(int productId) {
        return getProduct(productId)
            .orElseThrow(() -> new IllegalArgumentException("Product with id " + productId + " not found"));
    }

    public int getInventorySize() {
        return inventory.size();
    }

    public Product updateProductQuantity(int productId, int newQuantity) {
        if (newQuantity < 0) {
            throw new IllegalArgumentException("New quantity cannot be negative");
        }
        Product product = getProductOrThrow(productId);
        product.setQuantity(newQuantity);
        return product;
    }

    public void removeProductById(int productId) {
        if (inventory.isEmpty()) {
            throw new IllegalArgumentException("Inventory is empty");
        }
        if (!hasProduct(productId)) {
            throw new IllegalArgumentException("Product with id " + productId + " does not exist in inventory");
        } else {
            this.inventory.remove(productId);
        }
    }

    public void viewInventory() {
        if (inventory.isEmpty()) {
            System.out.println("Inventory is currently empty");
            return;
        }
        
        inventory.values().forEach(product -> 
            System.out.println("ID " + product.getId() + ": " + product.getName() + " - " + "$"
                    + product.getPrice() + " (Quantity: " + product.getQuantity() + ")"));
    }

    public void viewTotalInventoryValue() {
        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty. Total value: $0.00");
            return;
        }

        BigDecimal totalValue = inventory.values().stream()
                .map(product -> product.getPrice().multiply(BigDecimal.valueOf(product.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    
        System.out.printf("Total Inventory Value: $%.2f%n", totalValue);
    }
}
