package webstaraunt;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        //Main method is a simple flow of how the StoreInventory class can be used
        StoreInventory inventory = new StoreInventory();

        try {
            Product product1 = new Product(1, "Microwave", BigDecimal.valueOf(0.50), 100);
            Product product2 = new Product(2, "Oven",BigDecimal.valueOf(0.75), 50);

            inventory.addProduct(product1);
            inventory.addProduct(product2);

            //View the inventory
            System.out.println("Initial Inventory");
            inventory.viewInventory();

            //Updates quantity of a product
            inventory.updateProductQuantity(1, 150);
            System.out.println("\nUpdated Inventory (After modifying quantity of Microwave)");
            inventory.viewInventory();

            // View total inventory value
            inventory.viewTotalInventoryValue();

            // Remove a product
            inventory.removeProductById(2);
            System.out.println("\nInventory After Removing Oven");
            inventory.viewInventory();
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}