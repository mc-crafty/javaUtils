//                   ::::::::
//         :+:      :+:    :+:
//    +++++++++++  +:+         +++++
//       +:+      +#+         +#  +#
//      +#+      +#+         +#
//     #+#      #+#     +#  +#  +#
//    ###       ########+   ####+

package org.vdyll.utils.registry;

import java.util.List;

/**
 * Utility class representing a Key in the Windows Registry
 *
 * @author MC_Crafty
 *
 */
public class RegistryKey {

    private final String                 name;
    private final List<String>           subKeys;
    private final List<RegistryProperty> properties;

    public RegistryKey(final String name, final List<String> subKeys, final List<RegistryProperty> properties) {
        this.name = name;
        this.subKeys = subKeys;
        this.properties = properties;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getSubKeys() {
        return this.subKeys;
    }

    public List<RegistryProperty> getProperties() {
        return this.properties;
    }

    /**
     * Find a property by name within this Key
     * 
     * @param name
     *            Property name to search for
     * @return The found property or, if not found, a blank Registry Property
     *         object
     */
    public RegistryProperty findProperty(final String name) {
        for (final RegistryProperty rp : this.properties) {
            if (rp.getName().equalsIgnoreCase(name)) { return rp; }
        }

        return new RegistryProperty("", RegistryTypes.NONE, "");
    }
}
