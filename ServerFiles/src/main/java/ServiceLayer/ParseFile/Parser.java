package ServiceLayer.ParseFile;

import DomainLayer.Stores.Store;
import DomainLayer.SystemImplementor;
import DomainLayer.SystemInterface;
import ServiceLayer.Server;
import Utility.LogUtility;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Parser
{
    Map<String, FunctionHandler> functions;
    List<String> lines_to_initialize;
    Map<String, SystemInterface> implementors;
    Store last_store;
    public Parser()
    {
        functions = new HashMap<>();
        lines_to_initialize = new ArrayList<>();
        implementors = new HashMap<>();
        manageFunctions();
        manageFile();
        manageFirstAdmin();
    }

    public void runCommands()
    {
        for(String line : lines_to_initialize)
        {
            String[] command_and_params = line.split(",");
            String command = command_and_params[0];
            List<String> params = Arrays.stream(Arrays.copyOfRange(command_and_params, 1, command_and_params.length)).collect(Collectors.toList());

            if (!functions.containsKey(command))
            {
                LogUtility.error("Could not operate the command " + command + "./nFinishing initialization now!");
                break;
            }
            functions.get(command).handleFunction(params);
        }
    }

    private void manageFirstAdmin()
    {
        SystemImplementor implementor = new SystemImplementor();
        implementor.enter();
        this.implementors.put("admin", implementor);
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
        this.functions.put("Logout", this::logout);
        this.functions.put("AddAdmin", this::addAdmin);
        this.functions.put("OpenStore", this::openStore);
        this.functions.put("AddItemToStore", this::addItemToStore);
        this.functions.put("AddManager", this::addManagerToStore);
        this.functions.put("SetManagerPermission", this::setManagerPermission);
    }


    private void register(List<String> params)
    {
        System.out.println("in register");
        String user_email = params.get(0);
        String username = params.get(1);
        String first_name = params.get(2);
        String last_name = params.get(3);
        String password = params.get(4);
        this.implementors.get("admin").register(user_email, username, first_name, last_name, password);
        //user1@gmail.com,user1,user1first,user1last,pass1
    }

    private void login(List<String> params)
    {
        System.out.println("in login");
        String username = params.get(0);
        String password = params.get(1);
        SystemImplementor new_impl = new SystemImplementor();
        new_impl.enter();
        new_impl.login(username, password);
        this.implementors.put(username, new_impl);
        //Login,user2,pass2
    }

    private void logout(List<String> params)
    {
        System.out.println("in logout");
        String username_tries_to_logout = params.get(0);
        this.implementors.get(username_tries_to_logout).logout();
        //Logout,admin
    }

    private void addAdmin(List<String> params)
    {
        System.out.println("in addAdmin");
        String username_that_adds_admin = params.get(0);
        String username_to_admin = params.get(1);
        this.implementors.get(username_that_adds_admin).addAdmin(username_to_admin);
        //AddAdmin,admin,user1
    }

    private void openStore(List<String> params)
    {
        System.out.println("in openStore");
        String username_that_open_store = params.get(0);
        String store_name = params.get(1);
        this.last_store = this.implementors.get(username_that_open_store).addNewStore(store_name).getObject();
        //OpenStore,user2,s
    }

    private void addItemToStore(List<String> params)
    {
        System.out.println("in addItemToStore");
        String username_that_add_item = params.get(0);
        String store_name = params.get(1);
        String item_name = params.get(2);
        String category = params.get(3);
        double price = Double.parseDouble(params.get(4));
        int amount = Integer.parseInt(params.get(5));
       // int store_id = this.implementors.get(username_that_add_item).getUsersStores().getObject().stream().filter(s -> s.getName().equals(store_name)).collect(Collectors.toList()).get(0).getStoreId();
        this.implementors.get(username_that_add_item).addItemToStore(this.last_store.getStoreId(), item_name,category, price, amount);
        //AddItemToStore,user2,s,Bamba,food,30,20
    }

    private void addManagerToStore(List<String> params)
    {
        System.out.println("in add manager to store s");
        String username_that_adds_manager = params.get(0);
        String new_manager_name = params.get(1);
        String store_name = params.get(2);
        //int store_id = this.implementors.get(username_that_adds_manager).getUsersStores().getObject().stream().filter(s -> s.getName().equals(store_name)).collect(Collectors.toList()).get(0).getStoreId();
        this.implementors.get(username_that_adds_manager).addManager(new_manager_name, this.last_store.getStoreId());
        //AddManager,user2,user3,s
    }

    private void setManagerPermission(List<String> params)
    {
        System.out.println("in setManagerPermission");
        String username_that_adds_permission = params.get(0);
        String new_manager_name = params.get(1);
        String store_name = params.get(2);
        byte permission = Byte.parseByte(params.get(3));
        //int store_id = this.implementors.get(username_that_adds_permission).getUsersStores().getObject().stream().filter(s -> s.getName().equals(store_name)).collect(Collectors.toList()).get(0).getStoreId();
        this.implementors.get(username_that_adds_permission).setManagerPermission(new_manager_name, this.last_store.getStoreId(), permission);
        //SetManagerPermission, user2,user3,s,manageInventory
    }


    public void clean()
    {
        this.implementors.get("user1").logout();
        this.implementors.get("user2").logout();
        this.implementors.get("user3").logout();
        this.implementors.get("user4").logout();
    }
}
