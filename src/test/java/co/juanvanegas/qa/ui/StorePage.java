package co.juanvanegas.qa.ui;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

/** IDs públicos observados el 23/10/2026. Perfil, carrito y checkout solo validados en DEMO. */
public final class StorePage {
    private StorePage() {}
    public static final Target SHOW_REGISTER = Target.the("enlace de registro").located(By.cssSelector("#show_register"));
    public static final Target ID = Target.the("cédula de registro").located(By.cssSelector("#reg_username"));
    public static final Target FIRST = Target.the("nombres de registro").located(By.cssSelector("#first_name"));
    public static final Target LAST = Target.the("apellidos de registro").located(By.cssSelector("#last_name"));
    public static final Target EMAIL = Target.the("correo de registro").located(By.cssSelector("#reg_email"));
    public static final Target PASSWORD = Target.the("contraseña de registro").located(By.cssSelector("#reg_password"));
    public static final Target CONFIRM = Target.the("confirmación de contraseña").located(By.cssSelector("#reg_password2"));
    public static final Target PRIVACY = Target.the("aceptación de tratamiento de datos").located(By.cssSelector("#privacy_policy_reg"));
    public static final Target REGISTER = Target.the("registrarme").located(By.cssSelector("button[name='register']"));
    public static final Target LOGIN_ID = Target.the("cédula de ingreso").located(By.cssSelector("#username"));
    public static final Target LOGIN_PASSWORD = Target.the("contraseña de ingreso").located(By.cssSelector("#password"));
    public static final Target LOGIN = Target.the("iniciar sesión").located(By.cssSelector("button[name='login']"));
    public static final Target ACCOUNT = Target.the("cuenta autenticada DEMO").located(By.cssSelector("#account-home"));
    public static final Target LOGOUT = Target.the("cerrar sesión DEMO").located(By.cssSelector("#logout"));
    public static final Target EDIT = Target.the("editar perfil DEMO").located(By.cssSelector("a[href$='/mi-cuenta/edit-account/']"));
    public static final Target PROFILE_FIRST = Target.the("nombre del perfil DEMO").located(By.cssSelector("#account_first_name"));
    public static final Target SAVE = Target.the("guardar datos DEMO").located(By.cssSelector("button[name='save_account_details']"));
    public static final Target NOTICE = Target.the("mensaje DEMO").located(By.cssSelector(".woocommerce-message"));
    public static final Target SIZE = Target.the("talla DEMO").located(By.cssSelector("#pa_talla"));
    public static final Target QUANTITY = Target.the("cantidad DEMO").located(By.cssSelector("input[name='quantity']"));
    public static final Target ADD = Target.the("añadir al carrito").located(By.cssSelector("button.single_add_to_cart_button"));
    public static final Target CART_ROW = Target.the("producto del carrito DEMO").located(By.cssSelector(".cart_item"));
    public static final Target CHECKOUT = Target.the("ir al checkout DEMO").located(By.cssSelector("#checkout"));
    public static final Target BILLING_FIRST = Target.the("nombre de facturación DEMO").located(By.cssSelector("#billing_first_name"));
    public static final Target BILLING_LAST = Target.the("apellido de facturación DEMO").located(By.cssSelector("#billing_last_name"));
    public static final Target BILLING_ADDRESS = Target.the("dirección DEMO").located(By.cssSelector("#billing_address_1"));
    public static final Target BILLING_CITY = Target.the("ciudad DEMO").located(By.cssSelector("#billing_city"));
    public static final Target PAYMENT = Target.the("pago ficticio DEMO").located(By.cssSelector("#payment_method_demo"));
    public static final Target TERMS = Target.the("términos DEMO").located(By.cssSelector("#terms"));
    public static final Target PLACE_ORDER = Target.the("confirmar pedido DEMO").located(By.cssSelector("#place_order"));
}
