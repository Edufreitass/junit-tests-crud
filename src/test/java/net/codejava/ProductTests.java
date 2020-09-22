package net.codejava;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import net.codejava.entity.Product;
import net.codejava.repository.ProductRepository;

@DataJpaTest
@TestMethodOrder(OrderAnnotation.class)
//@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ProductTests {

	@Autowired
	private ProductRepository repository;

	@Test
	@Rollback(false)
	@Order(1)
	public void testCreateProduct() {
		Product product = new Product("iPhone 10", 789);
		Product savedProduct = repository.save(product);

		assertNotNull(savedProduct);
	}

	@Test
	@Order(2)
	public void testFindProductByNameExist() {
		String name = "iPhone 10";
		Product product = repository.findByName(name);

		assertThat(product.getName()).isEqualTo(name);
	}

	@Test
	@Order(3)
	public void testFindProductByNameNotExist() {
		String name = "iPhone 11";
		Product product = repository.findByName(name);

		assertNull(product);
	}

	@Test
	@Rollback(false)
	@Order(5)
	public void testUpdateProduct() {
		String productName = "Kindle Reader";
		Product product = new Product(productName, 199);
		product.setId(1);

		repository.save(product);

		Product updatedProduct = repository.findByName(productName);

		assertThat(updatedProduct.getName()).isEqualTo(productName);
	}

	@Test
	@Order(4)
	public void testListProducts() {
		List<Product> products = repository.findAll();

		for (Product product : products) {
			System.out.println(product);
		}

		assertThat(products).size().isGreaterThan(0);
	}

	@Test
	@Rollback(false)
	@Order(6)
	public void testDeleteProduct() {
		Integer id = 1;

		boolean isExistBeforeDelete = repository.findById(id).isPresent();

		repository.deleteById(id);

		boolean notExistAfterDelete = repository.findById(id).isPresent();

		assertTrue(isExistBeforeDelete);
		assertFalse(notExistAfterDelete);
	}

}
