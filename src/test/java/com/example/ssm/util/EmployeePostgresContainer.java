package com.example.ssm.util;

import org.testcontainers.containers.PostgreSQLContainer;

public class EmployeePostgresContainer extends PostgreSQLContainer<EmployeePostgresContainer> {

    private static final String IMAGE_VERSION = "postgres:12.6-alpine";

    private static EmployeePostgresContainer container;


    private EmployeePostgresContainer() {
        super(IMAGE_VERSION);
    }

    public static EmployeePostgresContainer getInstance() {
        if (container == null) {
            container = new EmployeePostgresContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
    }
}
