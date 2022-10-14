package com.orcohen.orderservice;

import com.orcohen.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.vendor.Database;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
public class OrderServiceTest {
    MySQLContainer dbServer ;
    OrderRepository orderRepository;
    Database database;

//    @BeforeAll
//    public void setUp() {
//        dbServer = new MySQLContainer<>("mysql:8.0.31")
//                .withDatabaseName("order_service")
//                .withUsername("something")
//                .withPassword("something")
//                .withExposedPorts(3306);
//        dbServer.start();
//        database = Database.MYSQL;
//        orderRepository =
//    }
}
