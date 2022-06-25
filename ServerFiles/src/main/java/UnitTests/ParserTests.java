package UnitTests;

import ServiceLayer.ParseFile.Parser;
import acceptenceTests.AbstractTest;
import org.junit.Before;
import org.junit.Test;

public class ParserTests extends AbstractTest
{
    private Parser parser;
    @Before
    public void setup()
    {
        parser = new Parser();
    }

    @Test
    public void successUseOfInitializationFile()
    {
        parser.runCommands();
    }
}
