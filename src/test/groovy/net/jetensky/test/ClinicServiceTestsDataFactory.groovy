package net.jetensky.test

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.PetClinicApplication;
import org.springframework.samples.petclinic.model.Owner
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.util.Creator;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = PetClinicApplication.class)
class ClinicServiceTestsDataFactory {

    @Autowired
    protected ClinicService clinicService;

    @Autowired
    Creator creator;

    @Test
    void shouldFindOwnersByLastName() {
        creator.save(new Owner(lastName: "Davis"));
        creator.save(new Owner(lastName: "Davis"));

        Collection<Owner> owners = this.clinicService.findOwnerByLastName("Davis");
        assertThat(owners.size()).isEqualTo(2);

        owners = this.clinicService.findOwnerByLastName("Daviss");
        assertThat(owners.isEmpty()).isTrue();
    }

    @Test
    void shouldCreatePetWithOwner() throws Exception {
      Pet pet = creator.save(new Pet());
      assertThat(pet.getOwner().getId()).isNotNull();
    }

    @Test
    void shouldFindVisitsByPetId() throws Exception {
        Visit visit1 = creator.save(new Visit());
        creator.save(new Visit(pet: visit1.pet));

        def petId = visit1.pet.id
        Collection<Visit> visits = this.clinicService.findVisitsByPetId(petId);
        assertThat(visits.size()).isEqualTo(2);
        Visit[] visitArr = visits.toArray(new Visit[visits.size()]);
        assertThat(visitArr[0].getPet()).isNotNull();
        assertThat(visitArr[0].getDate()).isNotNull();
        assertThat(visitArr[0].getPet().getId()).isEqualTo(petId);
    }


}
