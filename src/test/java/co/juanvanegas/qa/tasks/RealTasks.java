package co.juanvanegas.qa.tasks;

import co.juanvanegas.qa.support.Settings;
import net.serenitybdd.screenplay.*;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.*;
import static co.juanvanegas.qa.ui.RealPage.*;

public final class RealTasks {
    private RealTasks() {}
    public static final String BASE="https://www.bon-bonite.com";
    public static final String PRODUCT=BASE+"/producto/zueco-en-carnaza-chocolate/";
    public static String secret(String key) {
        String value=System.getenv(key);
        if(value==null||value.isBlank())throw new IllegalStateException("Falta variable de entorno "+key);
        return value;
    }
    public static WebDriver driver(Actor actor){return BrowseTheWeb.as(actor).getDriver();}
    public static WebElement visible(WebDriver d,By by){
        return new WebDriverWait(d,Duration.ofSeconds(25)).until(web -> web.findElements(by).stream().filter(WebElement::isDisplayed).findFirst().orElse(null));
    }
    public static void click(WebDriver d,By by){
        dismissCookies(d);
        WebElement e=visible(d,by);new WebDriverWait(d,Duration.ofSeconds(20)).until(web->e.isEnabled());
        try {e.click();} catch(ElementClickInterceptedException blocked){
            if(!dismissCookies(d))throw blocked;
            visible(d,by).click();
        }
    }
    private static boolean dismissCookies(WebDriver d){
        List<WebElement> reject=d.findElements(By.id("cookiescript_reject"));
        Optional<WebElement> button=reject.stream().filter(WebElement::isDisplayed).findFirst();
        if(button.isEmpty())return false;
        button.get().click();
        new WebDriverWait(d,Duration.ofSeconds(10)).until(web->web.findElements(By.id("cookiescript_reject")).stream().noneMatch(WebElement::isDisplayed));
        return true;
    }
    private static void fill(WebDriver d,By by,String value){WebElement e=visible(d,by);e.clear();e.sendKeys(value);}
    public static Performable login(){
        return Task.where("{0} inicia sesión con credenciales externas",actor->{
            WebDriver d=driver(actor);d.get(BASE+"/mi-cuenta/");
            fill(d,LOGIN_ID,secret("QA_LOGIN"));fill(d,LOGIN_PASSWORD,secret("QA_PASSWORD"));click(d,LOGIN);
            new WebDriverWait(d,Duration.ofSeconds(30)).until(web->web.getCurrentUrl().contains("/orders/"));
        });
    }
    public static Performable logout(){
        return Task.where("{0} cierra su sesión real",actor->{
            WebDriver d=driver(actor);click(d,MENU);click(d,LOGOUT);
            new WebDriverWait(d,Duration.ofSeconds(25)).until(web->!web.getCurrentUrl().contains("/mi-cuenta/"));
        });
    }
    public static Performable openProfile(){return Task.where("{0} consulta su perfil real",actor->{
        WebDriver d=driver(actor);d.get(BASE+"/mi-cuenta/edit-account/");visible(d,PROFILE);
    });}
    public static Performable editName(String name){return Task.where("{0} modifica únicamente el nombre desde el formulario real",actor->{
        WebDriver d=driver(actor);click(d,EDIT);fill(d,FIRST,name);click(d,SAVE);
        new WebDriverWait(d,Duration.ofSeconds(20)).until(web->{
            return web.findElements(MESSAGE).stream().anyMatch(e->e.isDisplayed()&&e.getText().contains("actualizados correctamente"))
                &&web.findElements(FIRST).stream().noneMatch(WebElement::isDisplayed);
        });
    });}
    public static Question<String> persistedName(){return Question.about("nombre guardado tras recargar el perfil").answeredBy(actor->{
        WebDriver d=driver(actor);click(d,EDIT);String value=visible(d,FIRST).getDomProperty("value");
        // Navegar sin guardar cancela la edición y conserva el dato del servidor.
        d.get(BASE+"/mi-cuenta/edit-account/");visible(d,PROFILE);return value;
    });}
    public static void removeTestProduct(WebDriver d){
        d.get(BASE+"/carrito/");
        By remove=By.cssSelector("#cart-container a.remove[data-product_id='1026146']");
        List<WebElement> matches=d.findElements(remove);
        if(!matches.isEmpty()){
            matches.getFirst().click();
            new WebDriverWait(d,Duration.ofSeconds(20)).until(web->web.findElements(remove).isEmpty());
        }
    }
    public static Performable prepareCart(){return Task.where("{0} prepara dos unidades de una variante disponible",actor->{
        WebDriver d=driver(actor);removeTestProduct(d);d.get(PRODUCT);visible(d,PRODUCT_NAME);
        String desired=Settings.get("qa.product.size","37");
        // Las opciones con stock están expuestas en el DOM del formulario de variantes.
        String variants=d.findElement(By.cssSelector("form.variations_form")).getDomAttribute("data-product_variations");
        List<Map<String,Object>> data;
        try {data=new com.fasterxml.jackson.databind.ObjectMapper().readValue(variants,new com.fasterxml.jackson.core.type.TypeReference<List<Map<String,Object>>>(){});}
        catch(Exception e){throw new IllegalStateException("No se pudo leer el stock expuesto por el producto",e);}
        boolean available=data.stream().anyMatch(v->Boolean.TRUE.equals(v.get("is_in_stock"))&&desired.equals(((Map<?,?>)v.get("attributes")).get("attribute_pa_talla")));
        if(!available)throw new IllegalStateException("La talla configurada no está disponible. Actualizar QA_PRODUCT_SIZE o los datos de prueba.");
        actor.remember("real.size",desired);actor.remember("real.unit",money(visible(d,UNIT_PRICE).getText()));
        actor.remember("real.product",visible(d,PRODUCT_NAME).getText());
        click(d,By.cssSelector("button.variation-button[data-value='"+desired+"']"));click(d,ADD);
        new WebDriverWait(d,Duration.ofSeconds(25)).until(web->web.findElements(By.cssSelector(".woocommerce-message")).stream().anyMatch(e->e.getText().toLowerCase(Locale.ROOT).contains("añadido")));
        d.get(BASE+"/carrito/");visible(d,ROW);click(d,PLUS);
        long expected=((Long)actor.recall("real.unit"))*2;
        new WebDriverWait(d,Duration.ofSeconds(25)).until(web->
            web.findElements(QTY).stream().anyMatch(e->e.isDisplayed()&&"2".equals(e.getDomProperty("value")))
            &&web.findElements(By.cssSelector(".cart-subtotal .amount")).stream().anyMatch(e->money(e.getText())==expected));
    });}
    public static Question<Long> subtotal(){return Question.about("subtotal real del carrito").answeredBy(actor->{
        WebDriver d=driver(actor);return money(d.findElement(By.cssSelector(".cart-subtotal .amount")).getText());
    });}
    public static Performable openCheckout(){return Task.where("{0} abre el resumen real de checkout sin enviar la orden",actor->{
        WebDriver d=driver(actor);click(d,CHECKOUT);visible(d,CHECKOUT_ROW);
    });}
    public static long money(String text){return Long.parseLong(text.replaceAll("[^0-9]",""));}
}
