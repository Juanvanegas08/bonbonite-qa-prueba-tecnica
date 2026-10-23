package co.juanvanegas.qa.tasks;

import co.juanvanegas.qa.model.TestCustomer;
import co.juanvanegas.qa.support.Settings;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.*;
import net.serenitybdd.screenplay.waits.WaitUntil;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.*;
import static co.juanvanegas.qa.ui.StorePage.*;

public final class CustomerTasks {
    private CustomerTasks() {}
    public static Task register(TestCustomer c) {
        return Task.where("{0} registra datos sintéticos en DEMO",
            Enter.theValue(c.id()).into(ID), Enter.theValue(c.first()).into(FIRST),
            Enter.theValue(c.last()).into(LAST), Enter.theValue(c.email()).into(EMAIL),
            Enter.theValue(c.password()).into(PASSWORD), Enter.theValue(c.password()).into(CONFIRM),
            Click.on(PRIVACY), Click.on(REGISTER), WaitUntil.the(ACCOUNT, isVisible()).forNoMoreThan(15).seconds());
    }
    public static Task login(TestCustomer c) {
        return Task.where("{0} ingresa a su cuenta DEMO",
            Open.url(Settings.base()+"/mi-cuenta/"), Enter.theValue(c.id()).into(LOGIN_ID),
            Enter.theValue(c.password()).into(LOGIN_PASSWORD), Click.on(LOGIN),
            WaitUntil.the(ACCOUNT, isVisible()).forNoMoreThan(15).seconds());
    }
    public static Task updateName(String name) {
        return Task.where("{0} actualiza el nombre de su perfil DEMO",
            Click.on(EDIT), Enter.theValue(name).into(PROFILE_FIRST), Click.on(SAVE),
            WaitUntil.the(NOTICE, isVisible()).forNoMoreThan(15).seconds());
    }
}
