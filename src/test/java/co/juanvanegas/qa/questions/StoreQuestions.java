package co.juanvanegas.qa.questions;

import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;

public final class StoreQuestions {
    private StoreQuestions() {}
    public static Question<String> text(String css) {
        return Question.about("texto visible de "+css).answeredBy(actor ->
            BrowseTheWeb.as(actor).getDriver().findElement(By.cssSelector(css)).getText());
    }
    public static Question<String> value(String css) {
        return Question.about("valor persistido de "+css).answeredBy(actor ->
            BrowseTheWeb.as(actor).getDriver().findElement(By.cssSelector(css)).getDomProperty("value"));
    }
    public static Question<String> attribute(String css, String attribute) {
        return Question.about(attribute+" de "+css).answeredBy(actor ->
            BrowseTheWeb.as(actor).getDriver().findElement(By.cssSelector(css)).getDomAttribute(attribute));
    }
}
