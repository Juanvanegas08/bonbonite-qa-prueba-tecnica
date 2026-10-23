package co.juanvanegas.qa.tasks;

import co.juanvanegas.qa.support.Settings;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.*;
import net.serenitybdd.screenplay.waits.WaitUntil;
import static co.juanvanegas.qa.ui.StorePage.*;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.*;

public final class PurchaseTasks {
    private PurchaseTasks() {}
    public static Task addProduct() {
        Settings.requireDemo();
        return Task.where("{0} añade dos unidades de la talla 37 en DEMO",
            Open.url(Settings.base()+"/producto/zueco-demo/"),
            SelectFromOptions.byValue("37").from(SIZE), Enter.theValue("2").into(QUANTITY),
            Click.on(ADD), WaitUntil.the(CART_ROW,isVisible()).forNoMoreThan(15).seconds());
    }
    public static Task pay() {
        Settings.requireDemo();
        return Task.where("{0} confirma un pedido con pago ficticio DEMO",
            Click.on(CHECKOUT), Enter.theValue("Juan").into(BILLING_FIRST),
            Enter.theValue("Prueba").into(BILLING_LAST), Enter.theValue("Calle DEMO 123").into(BILLING_ADDRESS),
            Enter.theValue("Bogotá").into(BILLING_CITY), Click.on(PAYMENT), Click.on(TERMS), Click.on(PLACE_ORDER));
    }
}
