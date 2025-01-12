import org.junit.jupiter.api.Test;
import webstaraunt.Product;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductTests {

    //Constructor tests
    @Test
    public void testProductConstructorValidInput() {
        Product product = new Product(1, "Microwave", BigDecimal.valueOf(99.99), 5);
        assert product.getId() == 1;
        assert product.getName().equals("Microwave");
        assert product.getPrice().equals(BigDecimal.valueOf(99.99));
        assert product.getQuantity() == 5;
    }

    @Test
    public void testProductConstructorFailsForNegativeId() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new Product(-1, "Valid Product", BigDecimal.valueOf(49.99), 10));
        assertEquals("Id must be non-negative", exception.getMessage());
    }

    @Test
    public void testProductConstructorFailsForNullOrEmptyName() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new Product(1, null, BigDecimal.valueOf(49.99), 10));
        assertEquals("Name cannot be empty", exception.getMessage());
        exception = assertThrows(IllegalArgumentException.class,
                () -> new Product(1, "", BigDecimal.valueOf(49.99), 10));
        assertEquals("Name cannot be empty", exception.getMessage());
    }

    @Test
    public void testProductConstructorFailsForNullOrNegativePrice() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new Product(1, "Valid Product", BigDecimal.valueOf(-10.50), 10));
        assertEquals("Price must be non-negative", exception.getMessage());
        exception = assertThrows(IllegalArgumentException.class,
                () -> new Product(1, "Valid Product", null, 10));
        assertEquals("Price must be non-negative", exception.getMessage());
    }

    //Setters tests
    @Test
    public void testSettersSuccessfully() {
        Product product = new Product(1, "Valid Product", BigDecimal.valueOf(49.99), 10);
        product.setId(2);
        product.setName("New Name");
        product.setPrice(BigDecimal.valueOf(99.99));
        product.setQuantity(15);
        assert product.getId() == 2;
        assert product.getName().equals("New Name");
        assert product.getPrice().equals(BigDecimal.valueOf(99.99));
        assert product.getQuantity() == 15;
    }

    @Test
    public void testSettersFailForNegativeId() {
        Product product = new Product(1, "Valid Product", BigDecimal.valueOf(49.99), 10);
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> product.setId(-1));
        assertEquals("Id must be non-negative", exception.getMessage());
    }

    @Test
    public void testSettersFailForNullOrEmptyName() {
        Product product = new Product(1, "Valid Product", BigDecimal.valueOf(49.99), 10);
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> product.setName(null));
        assertEquals("Name cannot be empty", exception.getMessage());
        exception = assertThrows(IllegalArgumentException.class,
                () -> product.setName(""));
        assertEquals("Name cannot be empty", exception.getMessage());
    }

    @Test
    public void testSettersFailForNullOrNegativePrice() {
        Product product = new Product(1, "Valid Product", BigDecimal.valueOf(49.99), 10);
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> product.setPrice(BigDecimal.valueOf(-10.50)));
        assertEquals("Price must be non-negative", exception.getMessage());
        exception = assertThrows(IllegalArgumentException.class,
                () -> product.setPrice(null));
        assertEquals("Price must be non-negative", exception.getMessage());
    }

    @Test
    public void testSettersFailForNegativeQuantity() {
        Product product = new Product(1, "Valid Product", BigDecimal.valueOf(49.99), 10);
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> product.setQuantity(-10));
        assertEquals("Quantity must be non-negative", exception.getMessage());
    }


}
