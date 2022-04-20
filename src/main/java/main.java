import org.apache.log4j.Logger;
import Logger.LogUtility;

public class main {
    public final static Logger logger = Logger.getRootLogger();
    public static void main(String[] args) {
        LogUtility.info("I AM MAIN");
        anotherMain.testfunc();
    }
}
