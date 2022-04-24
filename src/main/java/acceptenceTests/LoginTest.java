package acceptenceTests;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest extends AbstractTest {

    public LoginTest() {
        super();
    }

    @BeforeEach
    public void setup() {
        this.bridge = new Real();
        System.out.println("asd");
    }

    @Test
    public void doNothing() {
        assertTrue(true);

    }

}
