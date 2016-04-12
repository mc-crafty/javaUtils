//                   ::::::::          
//         :+:      :+:    :+:         
//    +++++++++++  +:+         +++++   
//       +:+      +#+         +#  +#   
//      +#+      +#+         +#        
//     #+#      #+#     +#  +#  +#     
//    ###       ########+   ####+      

package com.vdyll.utils.registry;

import java.util.List;

public class RegistryKey {
    
    private String name;
    private List<String> subKeys;
    private List<RegistryProperty> properties;
    
    public RegistryKey(String name, List<String> subKeys, List<RegistryProperty> properties) {
        this.name = name;
        this.subKeys = subKeys;
        this.properties = properties;
    }

    public String getName() { return this.name; }
    public List<String> getSubKeys() { return this.subKeys; }
    public List<RegistryProperty> getProperties() { return this.properties; }
    
    public RegistryProperty findProperty(String name) {
        for (RegistryProperty rp : this.properties) {
            if (rp.getName().equalsIgnoreCase(name)) {
                return rp;
            }
        }
        
        return new RegistryProperty("", RegistryTypes.NONE, "");
    }
}
