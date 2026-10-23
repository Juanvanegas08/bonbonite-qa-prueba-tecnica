package co.juanvanegas.qa.steps;

import co.juanvanegas.qa.tasks.RealTasks;
import io.cucumber.java.*;
import io.cucumber.java.es.*;
import net.serenitybdd.annotations.Managed;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.*;
import java.util.UUID;
import static co.juanvanegas.qa.ui.RealPage.*;
import static org.junit.jupiter.api.Assertions.*;

public class RealSteps {
    @Managed(driver="chrome") WebDriver browser;
    private Actor juan;
    private String originalName,changedName;
    private boolean changed,cart;
    @Before("@real or @registro_nuevo") public void setup(){juan=Actor.named("Juan").whoCan(BrowseTheWeb.with(browser));}
    @Dado("que Juan inicia sesión en Bon-Bonite con su cuenta autorizada") public void login(){juan.attemptsTo(RealTasks.login());}
    @Entonces("puede consultar los datos personales de su cuenta real") public void profile(){
        juan.attemptsTo(RealTasks.openProfile());assertFalse(juan.asksFor(RealTasks.persistedName()).isBlank());
    }
    @Cuando("cambia temporalmente el nombre de su perfil real") public void update(){
        juan.attemptsTo(RealTasks.openProfile());originalName=juan.asksFor(RealTasks.persistedName());
        changedName=originalName+" QA "+UUID.randomUUID().toString().substring(0,6);changed=true;
        juan.attemptsTo(RealTasks.editName(changedName));
    }
    @Entonces("recupera el nombre modificado tras cerrar e iniciar sesión") public void persists(){
        juan.attemptsTo(RealTasks.logout(),RealTasks.login(),RealTasks.openProfile());
        assertEquals(changedName,juan.asksFor(RealTasks.persistedName()));
    }
    @Entonces("restaura el nombre original de su perfil") public void restore(){
        juan.attemptsTo(RealTasks.editName(originalName),RealTasks.openProfile());
        assertEquals(originalName,juan.asksFor(RealTasks.persistedName()));changed=false;
    }
    @Cuando("selecciona una talla disponible y prepara dos unidades en el carrito real") public void cart(){cart=true;juan.attemptsTo(RealTasks.prepareCart());}
    @Entonces("el carrito real conserva el producto la talla y el cálculo correcto") public void assertCart(){
        String name=RealTasks.visible(browser,CART_NAME).getText();String product=juan.recall("real.product"),size=juan.recall("real.size");
        assertTrue(name.contains(product));assertTrue(name.endsWith("- "+size));
        assertEquals("2",RealTasks.visible(browser,QTY).getDomProperty("value"));
        long price=juan.recall("real.unit");assertEquals(price,RealTasks.money(RealTasks.visible(browser,CART_UNIT).getText()));
        assertEquals(price*2,juan.asksFor(RealTasks.subtotal()));
    }
    @Entonces("el checkout real conserva el subtotal y ofrece el paso de facturación") public void checkout(){
        juan.attemptsTo(RealTasks.openCheckout());long price=juan.recall("real.unit");
        assertEquals(price*2,RealTasks.money(RealTasks.visible(browser,CHECKOUT_SUBTOTAL).getText()));
        assertTrue(RealTasks.visible(browser,CHECKOUT_ROW).getText().contains(juan.<String>recall("real.size")));
        RealTasks.click(browser,CONTINUE);assertNotNull(RealTasks.visible(browser,BILLING));
        assertFalse(browser.findElements(PLACE_ORDER).isEmpty());
        assertFalse(browser.getCurrentUrl().contains("order-received"));
        // No se pulsa place_order: es una orden del comercio real, no un pago sandbox.
    }
    @After("@real") public void cleanup(){
        if(changed&&originalName!=null){juan.attemptsTo(RealTasks.openProfile(),RealTasks.editName(originalName));changed=false;}
        if(cart){RealTasks.removeTestProduct(browser);cart=false;}
    }
    @Dado("que hay datos únicos y autorización explícita para un nuevo registro real") public void approved(){
        if(!"SI".equals(System.getenv("QA_REGISTER_APPROVED")))throw new IllegalStateException("Requiere QA_REGISTER_APPROVED=SI y datos únicos autorizados.");
        for(String key:new String[]{"QA_LOGIN","QA_PASSWORD","QA_EMAIL","QA_FIRST_NAME","QA_LAST_NAME"})RealTasks.secret(key);
    }
    @Cuando("Juan envía el formulario real de registro") public void register(){
        browser.get(RealTasks.BASE+"/mi-cuenta/");RealTasks.click(browser,By.id("show_register"));
        String[][] fields={{"reg_username","QA_LOGIN"},{"first_name","QA_FIRST_NAME"},{"last_name","QA_LAST_NAME"},{"reg_email","QA_EMAIL"},{"reg_password","QA_PASSWORD"},{"reg_password2","QA_PASSWORD"}};
        for(String[] f:fields)RealTasks.visible(browser,By.id(f[0])).sendKeys(RealTasks.secret(f[1]));
        RealTasks.click(browser,By.id("privacy_policy_reg"));RealTasks.click(browser,By.cssSelector("button[name='register']"));
        new org.openqa.selenium.support.ui.WebDriverWait(browser,java.time.Duration.ofSeconds(30)).until(d->d.getCurrentUrl().contains("/orders/"));
    }
    @Entonces("puede cerrar sesión y entrar con esa cuenta nueva") public void newLogin(){juan.attemptsTo(RealTasks.logout(),RealTasks.login(),RealTasks.openProfile());assertFalse(juan.asksFor(RealTasks.persistedName()).isBlank());}
}
