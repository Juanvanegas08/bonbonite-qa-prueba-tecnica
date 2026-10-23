package co.juanvanegas.qa.steps;

import co.juanvanegas.qa.model.TestCustomer;
import co.juanvanegas.qa.questions.StoreQuestions;
import co.juanvanegas.qa.support.*;
import co.juanvanegas.qa.tasks.*;
import io.cucumber.java.Before;
import io.cucumber.java.AfterAll;
import io.cucumber.java.es.*;
import net.serenitybdd.annotations.Managed;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.*;
import net.serenitybdd.screenplay.waits.WaitUntil;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.*;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static co.juanvanegas.qa.ui.StorePage.*;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.*;

public class StoreSteps {
    @Managed(driver="chrome") WebDriver browser;
    private Actor juan;
    private TestCustomer customer;
    private String updatedName;

    @Before public void actor() { juan = Actor.named("Juan").whoCan(BrowseTheWeb.with(browser)); }
    @Before(value="@e2e", order=1) public void demo() throws Exception {
        Settings.requireDemo(); DemoStore.start();
    }
    @AfterAll public static void stopDemo() { DemoStore.stop(); }
    @Dado("que Juan abre el registro de clientes") public void registration() {
        juan.attemptsTo(Open.url(Settings.base()+"/mi-cuenta/"), Click.on(SHOW_REGISTER));
    }
    @Cuando("registra una cuenta de prueba única con datos válidos") public void registers() {
        customer=TestCustomer.unique(); juan.attemptsTo(CustomerTasks.register(customer));
        assertEquals(customer.first(), juan.asksFor(StoreQuestions.text("#account-name")));
    }
    @Entonces("puede salir e ingresar con la cuenta creada") public void reloginNew() {
        juan.attemptsTo(Click.on(LOGOUT), CustomerTasks.login(customer));
        assertEquals(customer.id(), juan.asksFor(StoreQuestions.text("#account-id")));
        assertEquals(customer.email(), juan.asksFor(StoreQuestions.text("#account-email")));
    }
    @Dado("que Juan ingresa con una cuenta de prueba existente") public void existing() {
        customer=TestCustomer.existing(); juan.attemptsTo(CustomerTasks.login(customer));
    }
    @Cuando("modifica su nombre y guarda los cambios") public void update() {
        updatedName="Juan QA "+UUID.randomUUID().toString().substring(0,8);
        juan.attemptsTo(CustomerTasks.updateName(updatedName));
        assertEquals("Datos guardados", juan.asksFor(StoreQuestions.text(".woocommerce-message")));
    }
    @Entonces("el nombre actualizado permanece después de cerrar e iniciar sesión") public void persisted() {
        juan.attemptsTo(Click.on(LOGOUT),CustomerTasks.login(customer),Click.on(EDIT));
        assertEquals(updatedName, juan.asksFor(StoreQuestions.value("#account_first_name")));
    }
    @Cuando("agrega dos unidades de un producto disponible al carrito") public void add() {
        juan.attemptsTo(PurchaseTasks.addProduct());
    }
    @Entonces("el carrito conserva producto talla cantidad y subtotal") public void cart() {
        assertEquals("Zueco DEMO",juan.asksFor(StoreQuestions.text(".cart_item .product-name")));
        assertEquals("37",juan.asksFor(StoreQuestions.text(".cart_item .variation-Talla")));
        assertEquals("2",juan.asksFor(StoreQuestions.value(".cart_item input.qty")));
        long unit=Long.parseLong(juan.asksFor(StoreQuestions.attribute(".product-price","data-amount")));
        long subtotal=Long.parseLong(juan.asksFor(StoreQuestions.attribute(".product-subtotal","data-amount")));
        assertEquals(327900L, unit); assertEquals(unit*2,subtotal);
    }
    @Cuando("completa el pedido con el medio de pago de demostración") public void purchase() {
        juan.attemptsTo(PurchaseTasks.pay());
    }
    @Entonces("recibe una confirmación con número de pedido y total correcto") public void confirmation() {
        juan.attemptsTo(WaitUntil.the(Target.the("confirmación del pedido DEMO").located(By.cssSelector(".woocommerce-thankyou-order-received")),isVisible()).forNoMoreThan(15).seconds());
        assertTrue(browser.getCurrentUrl().contains("/order-received/"));
        assertTrue(juan.asksFor(StoreQuestions.text("#order-number")).matches("DEMO-[0-9]+"));
        assertEquals("655800",juan.asksFor(StoreQuestions.attribute("#order-total","data-amount")));
        assertEquals("Pago ficticio aprobado",juan.asksFor(StoreQuestions.text("#payment-status")));
    }
    @Dado("que Juan consulta el formulario público de registro") public void publicRegister() {
        juan.attemptsTo(Open.url(Settings.publicBase()+"/mi-cuenta/"),
            WaitUntil.the(SHOW_REGISTER,isClickable()).forNoMoreThan(30).seconds(),Click.on(SHOW_REGISTER),
            WaitUntil.the(ID,isVisible()).forNoMoreThan(15).seconds());
    }
    @Entonces("identifica los campos obligatorios sin enviar datos") public void publicFields() {
        for (String css : new String[]{"#reg_username","#first_name","#last_name","#reg_email","#reg_password","#reg_password2","#privacy_policy_reg"}) {
            assertTrue(browser.findElement(By.cssSelector(css)).isDisplayed(),"Campo visible: "+css);
        }
        assertEquals("email",browser.findElement(By.id("reg_email")).getDomAttribute("type"));
        assertEquals("password",browser.findElement(By.id("reg_password")).getDomAttribute("type"));
        assertNotNull(browser.findElement(By.id("reg_password")).getDomAttribute("required"));
        assertEquals("checkbox",browser.findElement(By.id("privacy_policy_reg")).getDomAttribute("type"));
        assertTrue(browser.findElement(By.cssSelector("button[name='register']")).isDisplayed());
    }
    @Entonces("puede consultar un producto con nombre precio y opciones de talla") public void publicProduct() {
        juan.attemptsTo(Open.url(Settings.publicProduct()));
        assertFalse(browser.findElement(By.cssSelector("h1.product_title")).getText().isBlank());
        assertFalse(browser.findElement(By.cssSelector("p.price")).getText().isBlank());
        assertFalse(browser.findElements(By.cssSelector("[name='attribute_pa_talla']")).isEmpty());
        assertFalse(browser.findElements(By.cssSelector("button.single_add_to_cart_button")).isEmpty());
    }
}
