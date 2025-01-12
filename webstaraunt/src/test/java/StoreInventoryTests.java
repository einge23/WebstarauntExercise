import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import webstaraunt.Product;
import webstaraunt.StoreInventory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class StoreInventoryTests {

    private StoreInventory inventory;

    @BeforeEach
    public void setup() {
        inventory = new StoreInventory();
    }

    //Tests for adding products to store inventory
    @Test
    public void testAddProductSuccessfully() {
        var product = new Product(1, "Microwave", BigDecimal.valueOf(99.99), 5);
        inventory.addProduct(product);

        assert inventory.hasProduct(1);
        Optional<Product> retrievedProduct = inventory.getProduct(1);
        assert retrievedProduct.isPresent();
        assertEquals(product, retrievedProduct.get());
    }
    
    @Test
    public void testAddMultipleProductsSuccessfully() {
        var product1 = new Product(1, "Microwave", BigDecimal.valueOf(99.99), 5);
        var product2 = new Product(2, "Toaster", BigDecimal.valueOf(49.99), 3);
        
        inventory.addProduct(product1);
        inventory.addProduct(product2);
    
        assert inventory.hasProduct(1);
        assert inventory.hasProduct(2);
    
        Optional<Product> retrievedProduct1 = inventory.getProduct(1);
        Optional<Product> retrievedProduct2 = inventory.getProduct(2);
    
        assert retrievedProduct1.isPresent();
        assertEquals(product1, retrievedProduct1.get());
        assert retrievedProduct2.isPresent();
        assertEquals(product2, retrievedProduct2.get());
    }
    
    @Test
    public void testAddProductFailsForNullProduct() {
        assertThrows(IllegalArgumentException.class, () -> inventory.addProduct(null));
    }

    @Test
    public void testAddProductFailsForDuplicateId() {
        var product = new Product(1, "Microwave", BigDecimal.valueOf(99.99), 10);
        inventory.addProduct(product);
        var illegalProduct = new Product(1, "Oven", BigDecimal.valueOf(199.99), 5);
        assertThrows(IllegalArgumentException.class, () -> inventory.addProduct(illegalProduct));
        assertEquals(1, inventory.getInventorySize());
    }
    
    //Tests for updating a product's quantity
    @Test
    public void testUpdateQuantitySuccessfully() {
        var product = new Product(1, "Microwave", BigDecimal.valueOf(99.99), 5);
        inventory.addProduct(product);
        var updatedProduct = inventory.updateProductQuantity(1, 100);
        assertNotNull(updatedProduct);
        assertEquals(100, updatedProduct.getQuantity());
    }

    @Test
    public void testUpdateQuantityFailsForInvalidProductId() {
        var product = new Product(1, "Microwave", BigDecimal.valueOf(99.99), 5);
        inventory.addProduct(product);
        assertThrows(IllegalArgumentException.class, () -> inventory.updateProductQuantity(2, 100));
    }

    @Test
    public void testUpdateQuantityFailsForNegativeQuantity() {
        var product = new Product(1, "Microwave", BigDecimal.valueOf(99.99), 5);
        inventory.addProduct(product);
        assertThrows(IllegalArgumentException.class, () -> inventory.updateProductQuantity(1, -100));
    }

    //Tests for removing a product by id
    @Test
    public void testRemoveProductByIdSuccessfully() {
        var product = new Product(1, "Microwave", BigDecimal.valueOf(99.99), 5);
        inventory.addProduct(product);
        inventory.removeProductById(1);
        assertFalse(inventory.hasProduct(1));
        assertEquals(0, inventory.getInventorySize());
    }
    
    @Test
    public void testRemoveProductByIdFailsForInvalidProductId() {
        var product = new Product(1, "Microwave", BigDecimal.valueOf(99.99), 5);
        inventory.addProduct(product);
        assertThrows(IllegalArgumentException.class, () -> inventory.removeProductById(2));
    }
    
    @Test
    public void testRemoveProductByIdFailsForEmptyInventory() {
        assertThrows(IllegalArgumentException.class, () -> inventory.removeProductById(1));
    }

    @Test
    public void testViewInventory() {
        var product1 = new Product(1, "Microwave", BigDecimal.valueOf(99.99), 5);
        var product2 = new Product(2, "Blender", BigDecimal.valueOf(49.99), 10);
        var product3 = new Product(3, "Toaster", BigDecimal.valueOf(29.99), 15);
        inventory.addProduct(product1);
        inventory.addProduct(product2);
        inventory.addProduct(product3);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            inventory.viewInventory();
            System.out.flush();
            String output = outContent.toString();

            for (var product : new Product[]{product1, product2, product3}) {
                assertTrue(output.contains("ID " + product.getId()));
                assertTrue(output.contains(product.getName()));
                assertTrue(output.contains(String.format("$%.2f", product.getPrice())));
                assertTrue(output.contains("Quantity: " + product.getQuantity()));
            }
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void testViewInventoryFailsForEmptyInventory() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            inventory.viewInventory();
            System.out.flush();
            String output = outContent.toString();
            assertEquals("Inventory is currently empty" + System.lineSeparator(), output);
        }
        finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void testViewInventoryWithProductHavingHighPrecisionPrice() {
        var product = new Product(1, "Microwave", BigDecimal.valueOf(99.999), 5);
        inventory.addProduct(product);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            inventory.viewInventory();
            System.out.flush();
            String output = outContent.toString();

            assertTrue(output.contains("ID " + product.getId()));
            assertTrue(output.contains(product.getName()));
            assertTrue(output.contains(String.format("$%.2f", product.getPrice())));
            assertTrue(output.contains("Quantity: " + product.getQuantity()));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void testViewTotalInventoryValueSuccessfully() {
        var product1 = new Product(1, "Microwave", BigDecimal.valueOf(99.99), 5);
        var product2 = new Product(2, "Blender", BigDecimal.valueOf(49.99), 10);
        var product3 = new Product(3, "Toaster", BigDecimal.valueOf(29.99), 15);
        var expectedTotalValue = BigDecimal.valueOf(1449.70);
        inventory.addProduct(product1);
        inventory.addProduct(product2);
        inventory.addProduct(product3);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            inventory.viewTotalInventoryValue();
            System.out.flush();
            String output = outContent.toString();
            assertTrue(output.contains("Total Inventory Value: $" + expectedTotalValue));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void testViewTotalInventoryValueFailsForEmptyInventory() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            inventory.viewTotalInventoryValue();
            System.out.flush();
            String output = outContent.toString();
            assertEquals("Inventory is empty. Total value: $0.00" + System.lineSeparator(), output);
        }
        finally {
            System.setOut(originalOut);
        }
    }
}
