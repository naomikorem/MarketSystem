package acceptenceTests;

public abstract class AbstractTest {
    protected Bridge bridge;

    public AbstractTest() {
        this.bridge = new Proxy(new Real());
    }
}
