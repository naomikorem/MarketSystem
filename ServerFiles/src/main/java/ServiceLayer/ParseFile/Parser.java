package ServiceLayer.ParseFile;

import DomainLayer.Stores.Item;
import DomainLayer.Stores.Store;
import DomainLayer.SystemImplementor;
import DomainLayer.SystemInterface;
import Utility.LogUtility;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Parser
{
    private String initialization_file_path;
    private Map<String, FunctionHandler> functions;
    private List<String> lines_to_initialize;
    private Map<String, SystemInterface> implementors;
    private Store last_store;
    private Item last_item;
    private final String NO_KEYWORDS = "no_keywords";
    public Parser(String path)
    {
        this.initialization_file_path = path;
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
            InputStream file_input = new FileInputStream(this.initialization_file_path);
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
        this.functions.put("ModifyItem", this::modifyItem);
        this.functions.put("AddOwner", this::addOwnerToStore);
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
        int store_id = getStoreId(username_that_add_item, store_name);
        this.last_item = this.implementors.get(username_that_add_item).addItemToStore(store_id, item_name,category, price, amount).getObject();
        //AddItemToStore,user2,s,Bamba,food,30,20
    }

    private void addManagerToStore(List<String> params)
    {
        System.out.println("in add manager to store s");
        String username_that_adds_manager = params.get(0);
        String new_manager_name = params.get(1);
        String store_name = params.get(2);
        int store_id = getStoreId(username_that_adds_manager, store_name);
        this.implementors.get(username_that_adds_manager).addManager(new_manager_name, store_id);
        //AddManager,user2,user3,s
    }

    private void setManagerPermission(List<String> params)
    {
        System.out.println("in setManagerPermission");
        String username_that_adds_permission = params.get(0);
        String new_manager_name = params.get(1);
        String store_name = params.get(2);
        byte permission = Byte.parseByte(params.get(3));
        int store_id = getStoreId(username_that_adds_permission, store_name);
        this.implementors.get(username_that_adds_permission).setManagerPermission(new_manager_name, store_id, permission);
        //SetManagerPermission, user2,user3,s,manageInventory
    }

    private void modifyItem(List<String> params)
    {
        System.out.println("in modifyItem");
        String username_that_update_item = params.get(0);
        String store_name = params.get(1);
        String item_name = params.get(2);
        String category = params.get(3);
        double price = Double.parseDouble(params.get(4));
        int amount = Integer.parseInt(params.get(5));

        List<String> keywords;
        if(params.get(6).startsWith("[") && params.get(6).endsWith("]"))
        {
            // list of keywords
            keywords = Arrays.stream(params.get(6).split(";")).collect(Collectors.toList());
        }
        else if(params.get(6).equals(this.NO_KEYWORDS))
        {
            // no keywords, empty list
            keywords = new ArrayList<>();
        }
        else {
            System.out.println("Syntax of keywords should be: [k1;k2;...;kn]");
            return;
        }

        int store_id = getStoreId(username_that_update_item, store_name); //this.implementors.get(username_that_add_item).getUsersStores().getObject().stream().filter(s -> s.getName().equals(store_name)).collect(Collectors.toList()).get(0).getStoreId();
        int item_id = getItemId(username_that_update_item, store_id, item_name);
        this.implementors.get(username_that_update_item).modifyItem(store_id, item_id, item_name,category, price, amount, keywords);
        //AddItemToStore,user2,s,Bamba,Food,30,10,no_keywords
    }

    private void addOwnerToStore(List<String> params)
    {
        System.out.println("in add owner to store s");
        String username_that_adds_owner = params.get(0);
        String new_owner_name = params.get(1);
        String store_name = params.get(2);
        int store_id = getStoreId(username_that_adds_owner, store_name);
        this.implementors.get(username_that_adds_owner).addOwner(new_owner_name, store_id);
        //AddOwner,user2,user4,s
    }


    private int getItemId(String username, int store_id, String item_name)
    {
        if(this.last_item == null)
        {
            this.last_item = this.implementors.get(username).getItems(store_id).getObject().keySet().stream().filter(i -> i.getProductName().equals(item_name)).collect(Collectors.toList()).get(0);
        }

        return this.last_item.getId();
    }

    private int getStoreId(String store_founder, String store_name)
    {
        if(this.last_store == null)
        {
            this.last_store = this.implementors.get(store_founder).getUsersStores().getObject().stream().filter(s -> s.getName().equals(store_name)).collect(Collectors.toList()).get(0);
        }

        return this.last_store.getStoreId();
    }


    public void clean()
    {
        this.implementors.values().forEach(s -> s.logout());
    }
}
