//                   ::::::::          
//         :+:      :+:    :+:         
//    +++++++++++  +:+         +++++   
//       +:+      +#+         +#  +#   
//      +#+      +#+         +#        
//     #+#      #+#     +#  +#  +#     
//    ###       ########+   ####+      

package com.vdyll.utils.registry;

public class RegistryProperty {
    
    private String name, value;
    private RegistryTypes type;
    
    public RegistryProperty(String name, RegistryTypes type, String value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }
    
    public String getName()  { return this.name; }
    public RegistryTypes getType()  { return this.type; }
    public String getTypeString() {return this.type.toString(); }
    public String getValue() { return this.value; }
    
    public static RegistryProperty parseRegistryProperty(String in) {
        String name = "", value = "";
        RegistryTypes type = RegistryTypes.NONE;
        
        String str[] = in.split("    ");
        int j = 0;
        for (int i = 0; i < str.length; i++) {
            if ( str[i].matches("^\\S+[\\s*\\S*]*\\S+$") ) {
                switch (j) {
                    case 0:
                        name = str[i]; break;
                    case 1:
                        try {
                            type = RegistryTypes.valueOf(str[i]);
                        } catch (IllegalArgumentException ex) { 
                            type = RegistryTypes.NONE;
                        } break;
                    case 2:
                        value = str[i]; break;
                }
                j++;
            }
        }
        
        return new RegistryProperty(name, type, value);
    }
}
