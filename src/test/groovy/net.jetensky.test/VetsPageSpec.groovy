import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.springframework.samples.petclinic.PetClinicApplication
import org.springframework.samples.petclinic.model.Vet
import org.springframework.samples.petclinic.util.Creator
import spock.lang.Specification

public class VetsPageSpec extends Specification{

    Creator creator

    void setup() {
        String[] a = []
        java.lang.System.setProperty("spring.devtools.restart.enabled", "false")
        PetClinicApplication.main(a)
        creator = PetClinicApplication.context.getBean(Creator)
    }

    def "Vets page"() {

        given: "I'm at the sign up form"

        creator.save(
                new Vet(firstName: "Pavel", lastName: "Jetensky"),
                new Vet()
        );
        def driver = new ChromeDriver()

        when:
        driver.get("http://localhost:8080/vets.html")

        then:
        driver.findElement(By.xpath("//td[text()='Pavel Jetensky']")) !=null

    }


}
