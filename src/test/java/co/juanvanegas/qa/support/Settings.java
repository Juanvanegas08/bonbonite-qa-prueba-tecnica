package co.juanvanegas.qa.support;

import java.net.URI;

public final class Settings {
    private Settings() {}
    public static String get(String key, String fallback) {
        return System.getProperty(key, System.getenv().getOrDefault(key.toUpperCase().replace('.', '_'), fallback));
    }
    public static boolean demo() { return get("qa.env", "demo").equals("demo"); }
    public static String base() { return get("qa.base.url", "http://127.0.0.1:8765").replaceAll("/$", ""); }
    public static String publicBase() { return "https://www.bon-bonite.com"; }
    public static String publicProduct() { return publicBase() + "/producto/zueco-en-carnaza-chocolate/"; }
    public static void requireDemo() {
        String host = URI.create(base()).getHost();
        if (!demo() || !("127.0.0.1".equals(host) || "localhost".equals(host))) {
            throw new IllegalStateException("Los E2E requieren la DEMO local. Falta ambiente autorizado con credenciales y pago sandbox para validar Bon-Bonite.");
        }
    }
}
