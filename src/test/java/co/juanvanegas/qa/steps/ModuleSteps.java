package co.juanvanegas.qa.steps;
import io.cucumber.java.es.*;
import net.serenitybdd.annotations.Managed;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
public class ModuleSteps {
 @Managed(driver="chrome") WebDriver browser;
 private WebDriverWait waitFor(){return new WebDriverWait(browser,Duration.ofSeconds(25));}
 private void requireAccessible(){
  String text=browser.findElement(By.tagName("body")).getText();
  assumeFalse(text.contains("403 Forbidden"),"BLOQUEADO: el servidor devuelve 403 Forbidden. No es un fallo funcional ni de selector; requiere acceso permitido.");
 }
 private void open(String path){browser.get("https://www.bon-bonite.com/"+path);requireAccessible();}
 @Dado("que se consulta el catálogo {string}") public void catalog(String path){open("categoria-producto/"+path+"/");}
 @Entonces("un producto del catálogo conserva nombre precio e imagen en su ficha") public void product(){
  var product=waitFor().until(d->d.findElements(By.cssSelector(".product-wrapper-with-variation > a[href*='/producto/']")).stream().filter(WebElement::isDisplayed).findFirst().orElse(null));
  browser.get(product.getDomAttribute("href"));
  requireAccessible();
  assertFalse(waitFor().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1.product_title"))).getText().isBlank());
  assertTrue(browser.findElement(By.cssSelector("p.price")).getText().matches("(?s).*\\d.*"));
  assertFalse(browser.findElements(By.cssSelector("img.product_img")).isEmpty());
 }
 @Dado("que se consulta PQRS") public void pqrs(){open("pqrs/");}
 @Entonces("el correo inválido no cumple la validación y existen tipos de solicitud") public void validation(){
  var select=waitFor().until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("select[name='select-3']")));
  assertTrue(new Select(select).getOptions().size()>1);
  var email=browser.findElement(By.cssSelector("input[name='email-1']"));email.sendKeys("correo-invalido");
  assertEquals(false,((JavascriptExecutor)browser).executeScript("return arguments[0].validity.valid",email));
  assertEquals("true",email.getDomAttribute("aria-required"));
 }
 @Dado("que se consulta el bono") public void gift(){open("producto/bono-de-regalo/");}
 @Entonces("seleccionar una denominación resuelve su variante") public void variant(){
  var button=waitFor().until(d->d.findElements(By.cssSelector("button.variation-button")).stream().filter(WebElement::isDisplayed).findFirst().orElse(null));
  String value=button.getDomAttribute("data-value");
  ((JavascriptExecutor)browser).executeScript("arguments[0].click()",button);
  waitFor().until(d->!d.findElement(By.name("variation_id")).getDomProperty("value").matches("0|"));
  assertNotNull(value);
 }
}
