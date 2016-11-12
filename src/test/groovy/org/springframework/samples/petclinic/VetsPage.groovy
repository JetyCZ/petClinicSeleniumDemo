package org.springframework.samples.petclinic

import geb.Page


public class VetsPage extends Page{

        static url = "/vets.html"
        static at = { $("h1").text() == "Sample App Sign-up Result Page" }
}
