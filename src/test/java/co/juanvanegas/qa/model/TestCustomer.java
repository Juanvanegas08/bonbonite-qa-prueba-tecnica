package co.juanvanegas.qa.model;

import java.util.UUID;

public record TestCustomer(String id, String first, String last, String email, String password) {
    public static TestCustomer unique() {
        String token = UUID.randomUUID().toString().replace("-", "");
        return new TestCustomer("DEMO-" + token.substring(0,12), "Juan", "Prueba", "qa+"+token+"@example.invalid", "SoloDemo!2026_X9");
    }
    public static TestCustomer existing() {
        return new TestCustomer("DEMO-EXISTENTE", "Juan", "Prueba", "existente@example.invalid", "SoloDemo!2026_X9");
    }
}
