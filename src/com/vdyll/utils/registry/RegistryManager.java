//                   ::::::::          
//         :+:      :+:    :+:         
//    +++++++++++  +:+         +++++   
//       +:+      +#+         +#  +#   
//      +#+      +#+         +#        
//     #+#      #+#     +#  +#  +#     
//    ###       ########+   ####+      

package com.vdyll.utils.registry;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RegistryManager {
    
    // 
    public static RegistryProperty readProperty(String keyName, String propertyName) throws Exception {
        RegistryProperty returnProperty = new RegistryProperty("", RegistryTypes.NONE, "");
        
        Process p = Runtime.getRuntime().exec("reg QUERY \"" + keyName + "\" /v \"" + propertyName + "\"");

        BufferedReader in = new BufferedReader( new InputStreamReader( p.getInputStream() ) );
        String out = "";

        while ( ( out = in.readLine() ) != null ) {
            if (out.matches("(.*)\\s+REG_(.*)")) {
                returnProperty = RegistryProperty.parseRegistryProperty(out);
                break;
            }
        }
        in.close();

        return returnProperty;
    }
    
    //
    public static RegistryKey readKey(String keyName) throws Exception {
        List<String> subKeyNames = new ArrayList<String>();
        List<RegistryProperty> properties = new ArrayList<RegistryProperty>();
        
        Process p = Runtime.getRuntime().exec("reg QUERY \"" + keyName + "\"");
        
        BufferedReader in = new BufferedReader( new InputStreamReader( p.getInputStream() ) );
        String out = "";
        
        while ( ( out = in.readLine() ) != null ) {
            if (out.equalsIgnoreCase("ERROR: Access is denied.")) { return null; }
            if (out.matches("(.*)\\s+REG_(.*)")) {
                RegistryProperty parsed = RegistryProperty.parseRegistryProperty(out);
                if (!parsed.getName().isEmpty() ) {
                    properties.add(parsed);
                }
            } else {
                if (out.matches("\\S+")) {
                    subKeyNames.add(out);
                }
            }
        }
        in.close();
        
        return new RegistryKey(keyName, subKeyNames, properties);
    }
    

    // 
    public static boolean addPropertyToKey(RegistryKey key, RegistryProperty prop) throws Exception {
        for (int i = 0; i < key.getProperties().size(); i++) {
            if (key.getProperties().get(i).getName().equalsIgnoreCase(prop.getName())) {
                return false;
            }
        }
        
        return addOrUpdatePropertyToKey(key, prop, false);
    }
    
    // 
    public static boolean addOrUpdatePropertyToKey(RegistryKey key, RegistryProperty prop) throws Exception {
        return addOrUpdatePropertyToKey(key, prop, true);
    }
    
    // 
    private static boolean addOrUpdatePropertyToKey(RegistryKey key, RegistryProperty prop, boolean allowUpdate) throws Exception {
        boolean comp = false, valid = false;

        valid = !(prop.getType().equals(RegistryTypes.NONE));

        if ( valid ) {
            Process p = Runtime.getRuntime().exec("reg ADD \"" + key.getName() + "\" /v \"" +
                    prop.getName() + "\" /t \"" + prop.getType() + "\" /d \"" + prop.getValue() + 
                    ( allowUpdate ? "\"  /f" : "\"" ) );

            BufferedReader in = new BufferedReader( new InputStreamReader( p.getInputStream() ) );
            String out = "";

            while ( (out = in.readLine() ) != null ) {
                if (out.equalsIgnoreCase("The operation completed successfully.")) {
                    comp = true;
                    boolean updated = false;
                    for (int i = 0; i < key.getProperties().size(); i++) {
                        if (key.getProperties().get(i).getName().equalsIgnoreCase(prop.getName())) {
                           key.getProperties().set(i, prop);
                           updated = true;
                           break;
                        }
                    }
                    if (!updated) {
                        key.getProperties().add(prop);
                    }
                }
            }
            in.close();
        }

        return comp;
    }

    // 
    public static boolean deletePropertyFromKey(RegistryKey key, RegistryProperty prop) throws Exception {
        boolean comp = false;
        Process p = Runtime.getRuntime().exec("reg DELETE \"" + key.getName() + "\" /v \"" +
                prop.getName() + "\" /f");

        BufferedReader in = new BufferedReader( new InputStreamReader( p.getInputStream() ) );
        String out = "";

        while ( ( out = in.readLine() ) != null ) {
            if (out.equalsIgnoreCase("The operation completed successfully.")) {
                comp = true;
                for (int i = 0; i < key.getProperties().size(); i++) {
                    if (key.getProperties().get(i).getName().equalsIgnoreCase(prop.getName())) {
                        key.getProperties().remove(i);
                        break;
                    }
                }
            }
        }
        in.close();

        return comp;
    }
}