import spock.lang.Specification

class HelloWorldSpec extends Specification {

    def message = "Hello world!"

    def "first feature method"() {

        when: "Message is transformed into lowercase"
        message = message.toLowerCase()

        then: "Should transform message into lowercase"
        message == "hello world!"
    }
}
