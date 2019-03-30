//                   ::::::::
//         :+:      :+:    :+:
//    +++++++++++  +:+         +++++
//       +:+      +#+         +#  +#
//      +#+      +#+         +#
//     #+#      #+#     +#  +#  +#
//    ###       ########+   ####+

package org.vdyll.utils.registry;

/**
 * Utility class to represent a Property in the Windows Registry
 *
 * @author MC_Crafty
 *
 */
public class RegistryProperty {

    private final String        name, value;
    private final RegistryTypes type;

    public RegistryProperty(final String name, final RegistryTypes type, final String value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public RegistryTypes getType() {
        return this.type;
    }

    public String getTypeString() {
        return this.type.toString();
    }

    public String getValue() {
        return this.value;
    }

    /**
     * Parse the Windows CMD output into an Object representation of the
     * Registry Property
     * 
     * @param in
     *            The Windows CMD output as a string
     * @return An Object representation of the Registry Property
     */
    public static RegistryProperty parseRegistryProperty(final String in) {
        String name = "", value = "";
        RegistryTypes type = RegistryTypes.NONE;

        final String str[] = in.split("    ");
        int j = 0;
        for (int i = 0; i < str.length; i++) {
            if (str[i].matches("^\\S+[\\s*\\S*]*\\S+$")) {
                switch (j) {
                    case 0:
                        name = str[i];
                        break;
                    case 1:
                        try {
                            type = RegistryTypes.valueOf(str[i]);
                        } catch (final IllegalArgumentException ex) {
                            type = RegistryTypes.NONE;
                        }
                        break;
                    case 2:
                        value = str[i];
                        break;
                }
                j++;
            }
        }

        return new RegistryProperty(name, type, value);
    }
}
