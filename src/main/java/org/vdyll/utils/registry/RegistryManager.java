//                   ::::::::
//         :+:      :+:    :+:
//    +++++++++++  +:+         +++++
//       +:+      +#+         +#  +#
//      +#+      +#+         +#
//     #+#      #+#     +#  +#  +#
//    ###       ########+   ####+

package org.vdyll.utils.registry;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to manage the Windows Registry through Java. Only tested on
 * Win7, but should be fully compatible on Win10 This functions by executing CMD
 * operations, therefore the parent process must have sufficient rights
 * (probably has to be administrator)
 *
 * @author MC_Crafty
 *
 */
public class RegistryManager {

    /**
     * Read a property from the Registry
     *
     * @param keyName
     *            The Key which the property exists in
     * @param propertyName
     *            The name of the property to read
     * @return An object representation of the Registry Property
     * @throws Exception
     */
    public static RegistryProperty readProperty(final String keyName, final String propertyName) throws Exception {
        RegistryProperty returnProperty = new RegistryProperty("", RegistryTypes.NONE, "");

        final Process p = Runtime.getRuntime().exec("reg QUERY \"" + keyName + "\" /v \"" + propertyName + "\"");

        final BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String out = "";

        while ((out = in.readLine()) != null) {
            if (out.matches("(.*)\\s+REG_(.*)")) {
                returnProperty = RegistryProperty.parseRegistryProperty(out);
                break;
            }
        }
        in.close();

        return returnProperty;
    }

    /**
     * Read a key from the registry
     *
     * @param keyName
     *            The name of the key to read
     * @return An object representation of the Registry Key
     * @throws Exception
     */
    public static RegistryKey readKey(final String keyName) throws Exception {
        final List<String> subKeyNames = new ArrayList<String>();
        final List<RegistryProperty> properties = new ArrayList<RegistryProperty>();

        final Process p = Runtime.getRuntime().exec("reg QUERY \"" + keyName + "\"");

        final BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String out = "";

        while ((out = in.readLine()) != null) {
            if (out.equalsIgnoreCase("ERROR: Access is denied.")) { return null; }
            if (out.matches("(.*)\\s+REG_(.*)")) {
                final RegistryProperty parsed = RegistryProperty.parseRegistryProperty(out);
                if (!parsed.getName().isEmpty()) {
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

    /**
     * Add a property to a Registry Key. Do not allow Updating if the property
     * is found.
     *
     * @param key
     *            The key we will add the property to
     * @param prop
     *            The property to add
     * @return True if the property was added
     * @throws Exception
     */
    public static boolean addPropertyToKey(final RegistryKey key, final RegistryProperty prop) throws Exception {
        for (int i = 0; i < key.getProperties().size(); i++) {
            if (key.getProperties().get(i).getName().equalsIgnoreCase(prop.getName())) { return false; }
        }

        return addOrUpdatePropertyToKey(key, prop, false);
    }

    /**
     * Add a property to a Registry Key. Allow Updating if the property is
     * found.
     *
     * @param key
     *            The key we will add the property to
     * @param prop
     *            The property to add
     * @return True if the property was added or updated
     * @throws Exception
     */
    public static boolean addOrUpdatePropertyToKey(final RegistryKey key, final RegistryProperty prop) throws Exception {
        return addOrUpdatePropertyToKey(key, prop, true);
    }

    /**
     * Add a property to a Registry Key
     *
     * @param key
     *            The key we will add the property to
     * @param prop
     *            The property to add
     * @param allowUpdate
     *            Allow Updating if the property is found
     * @return True if the property was added or updated
     * @throws Exception
     */
    private static boolean addOrUpdatePropertyToKey(final RegistryKey key, final RegistryProperty prop,
            final boolean allowUpdate) throws Exception {
        boolean comp = false, valid = false;

        valid = !prop.getType().equals(RegistryTypes.NONE);

        if (valid) {
            final Process p = Runtime.getRuntime().exec("reg ADD \"" + key.getName() + "\" /v \"" + prop.getName()
                    + "\" /t \"" + prop.getType() + "\" /d \"" + prop.getValue() + (allowUpdate ? "\"  /f" : "\""));

            final BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String out = "";

            while ((out = in.readLine()) != null) {
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

    /**
     * Delete a property from a registry key.
     *
     * @param key
     *            The key we will delete the property from
     * @param prop
     *            The property to delete
     * @return True if the property was deleted
     * @throws Exception
     */
    public static boolean deletePropertyFromKey(final RegistryKey key, final RegistryProperty prop) throws Exception {
        boolean comp = false;
        final Process p = Runtime.getRuntime().exec("reg DELETE \"" + key.getName() + "\" /v \"" + prop.getName() + "\" /f");

        final BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String out = "";

        while ((out = in.readLine()) != null) {
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
