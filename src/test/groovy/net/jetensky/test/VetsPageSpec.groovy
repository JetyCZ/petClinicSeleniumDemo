package net.jetensky.test

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.springframework.samples.petclinic.PetClinicApplication
import org.springframework.samples.petclinic.model.Vet
import org.springframework.samples.petclinic.util.Creator
import org.springframework.samples.petclinic.web.WebDriverHelper
import spock.lang.Specification

class VetsPageSpec extends Specification{

    Creator creator
    WebDriver driver

    void setup() {
        String[] a = []


        java.lang.System.setProperty("spring.devtools.restart.enabled", "false")
        PetClinicApplication.main(a)
        creator = PetClinicApplication.context.getBean(Creator)
        driver = WebDriverHelper.getWebDriver();
    }

    def "Vets page"() {

        given:
        creator.save(
                new Vet(firstName: "Pavel", lastName: "Jetensky"),
                new Vet()
        )

        when:
        driver.get("http://localhost:8080/vets.html")

        then:
        driver.findElement(By.xpath("//td[text()='Pavel Jetensky']")) !=null

    }

    void cleanup() {
        driver.quit()
        PetClinicApplication.context.close()
    }
}
