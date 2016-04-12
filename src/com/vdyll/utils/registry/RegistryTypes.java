//                   ::::::::          
//         :+:      :+:    :+:         
//    +++++++++++  +:+         +++++   
//       +:+      +#+         +#  +#   
//      +#+      +#+         +#        
//     #+#      #+#     +#  +#  +#     
//    ###       ########+   ####+      

package com.vdyll.utils.registry;

public enum RegistryTypes {
    SZ("REG_SZ"),
    BINARY("REG_BINARY"),
    DWORD("REG_DWORD"),
    QWORD("REG_QWORD"),
    DWORD_LE("REG_DWORD_LITTLE_ENDIAN"),
    DWORD_BE("REG_DWORD_BIG_ENDIAN"),
    QWORD_LE("REG_QWORD_LITTLE_ENDIAN"),
    X_SZ("REG_EXPAND_SZ"),
    LINK("REG_LINK"),
    M_SZ("REG_MULTI_SZ"),
    RES_LIST("REG_RESOURCE_LIST"),
    NONE("REG_NONE");
    
    private final String stringValue;
    
    private RegistryTypes(final String s) { stringValue = s; }
    
    public String toString() { return stringValue; }
    
    public boolean contains(final String s) {
        for (RegistryTypes registryType : values()) {
            if (registryType.stringValue.equals(s)) {
                return true;
            }
        }
        return false;
    }
}
