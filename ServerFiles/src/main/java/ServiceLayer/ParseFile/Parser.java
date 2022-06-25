package ServiceLayer.ParseFile;

import DomainLayer.SystemImplementor;
import ServiceLayer.Server;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Parser
{
    Map<String, FunctionHandler> functions;
    List<String> lines_to_initialize;
    public Parser()
    {
        functions = new HashMap<>();
        lines_to_initialize = new ArrayList<>();
        manageFunctions();
        manageFile();
    }

    public void runCommands()
    {
        for(String line : lines_to_initialize)
        {
            String[] command_and_params = line.split(",");
            String command = command_and_params[0];
            List<String> params = Arrays.stream(Arrays.copyOfRange(command_and_params, 1, command_and_params.length)).collect(Collectors.toList());
            functions.get(command).handleFunction(params);
        }
    }

    private void manageFile()
    {
        try
        {
            InputStream file_input = new FileInputStream(Server.INIT_FILE_PATH);
            InputStreamReader input_reader = new InputStreamReader(file_input, StandardCharsets.UTF_8);
            BufferedReader buffer_reader = new BufferedReader(input_reader);
            buffer_reader.lines().forEach(line -> this.lines_to_initialize.add(line));
        }
        catch (Exception e)
        {
            System.out.println("Could not open initialization file!");
        }

    }

    private void manageFunctions()
    {
        this.functions.put("Register", this::register);
        this.functions.put("Login", this::login);
    }

    public void register(List<String> params)
    {
        System.out.println("in register");
    }

    public void login(List<String> params)
    {
        System.out.println("in login");

    }

}
