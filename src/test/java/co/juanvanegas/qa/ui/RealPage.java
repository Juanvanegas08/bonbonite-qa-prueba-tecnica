package co.juanvanegas.qa.ui;

import org.openqa.selenium.By;

/** Controles observados en el sitio real el 23/10/2026. */
public final class RealPage {
    private RealPage() {}
    public static final By LOGIN_ID=By.id("username"), LOGIN_PASSWORD=By.id("password");
    public static final By LOGIN=By.cssSelector("button[name='login']");
    public static final By PROFILE=By.id("profile-update-form");
    public static final By EDIT=By.xpath("//form[@id='profile-update-form']//button[contains(normalize-space(.),'Actualizar') and contains(normalize-space(.),'Información')]");
    public static final By FIRST=By.cssSelector("#profile-update-form input[name='first_name']");
    public static final By SAVE=By.xpath("//form[@id='profile-update-form']//button[normalize-space()='Guardar']");
    public static final By MESSAGE=By.id("profile-message");
    public static final By MENU=By.cssSelector("#toggle-account-menu > a, #toggle-account-menu-mobile > a");
    public static final By LOGOUT=By.cssSelector("a[href*='action=logout']");
    public static final By PRODUCT_NAME=By.cssSelector("h1.product_title");
    public static final By UNIT_PRICE=By.cssSelector("p.price .woocommerce-Price-amount");
    public static final By SIZES=By.cssSelector("button.variation-button");
    public static final By ADD=By.cssSelector("button.single_add_to_cart_button");
    public static final By ROW=By.cssSelector("#cart-container tr.cart_item");
    public static final By QTY=By.cssSelector("#cart-container .cart_item input.qty");
    public static final By PLUS=By.cssSelector("#cart-container .cart_item .btn-qty-plus");
    public static final By CART_NAME=By.cssSelector("#cart-container .cart_item .product-name > a");
    public static final By CART_UNIT=By.cssSelector("#cart-container .cart_item .product-price .amount");
    public static final By CHECKOUT=By.cssSelector("a[href$='/finalizar-compra/']");
    public static final By CHECKOUT_ROW=By.cssSelector("#step1 .cart_item");
    public static final By CHECKOUT_SUBTOTAL=By.cssSelector("#step1 [data-title='Original Price'] .amount");
    public static final By CONTINUE=By.cssSelector("#step1 button[data-step='step2']");
    public static final By BILLING=By.id("billing_address_1");
    public static final By PLACE_ORDER=By.id("place_order");
}
