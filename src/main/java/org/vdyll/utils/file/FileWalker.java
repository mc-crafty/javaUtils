//                   ::::::::
//         :+:      :+:    :+:
//    +++++++++++  +:+         +++++
//       +:+      +#+         +#  +#
//      +#+      +#+         +#
//     #+#      #+#     +#  +#  +#
//    ###       ########+   ####+

package org.vdyll.utils.file;

import java.io.File;
import java.util.List;

/**
 * Utility Class for listing and providing access to files with directories
 *
 * @author MC_Crafty
 *
 */
public class FileWalker {

    /**
     * Walks each file in a directory structure, adding the java.io.File object
     * to a list
     * 
     * @param path
     *            Initial path to begin walking from
     * @param debug
     *            Print debug output
     * @param fileList
     *            The list which we will be appending files to.
     * @param filterArray
     *            An array of file extensions. If provided, only add files which
     *            match an extension
     * @param recursive
     *            Recurse through sub-directories
     * @return The list of files we have been appending to; fileList parameter.
     */
    public static List<File> walk(final String path, final boolean debug, final List<File> fileList,
            final String[] filterArray, final boolean recursive) {

        // init objs
        final File root = new File(path);
        final File[] list = root.listFiles();
        final boolean filter = filterArray != null;
        boolean match = true;

        // as long as we give a valid path
        if (root.exists()) {
            if (list != null) {
                if (list.length > 0) {
                    // walk through every file in dir
                    for (final File f : list) {
                        if (f != null) {
                            if (f.isDirectory()) {
                                if (recursive) {
                                    if (debug) {
                                        System.out.println("Dir:" + f.getAbsoluteFile());
                                    }
                                    // if it's a dir, walk through all the files
                                    // in it
                                    walk(f.getAbsolutePath(), debug, fileList, filterArray, recursive);
                                } else if (debug) {
                                    System.out.println("Skipping Dir:" + f.getAbsoluteFile());
                                }
                            } else {
                                // if an extension filter has been specified
                                // loop through each
                                // extension until we find a match, or don't add
                                // the file
                                if (filter) {
                                    match = false;
                                    while (!match) {
                                        for (final String element : filterArray) {
                                            if (f.getName().endsWith(element)) {
                                                match = true;
                                                break;
                                            }
                                        }
                                        break;
                                    }
                                }
                                // add each file to main list if it matches the
                                // extension filter
                                if (match) {
                                    fileList.add(f);
                                    if (debug) {
                                        System.out.println("File:" + f.getAbsoluteFile());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return fileList;
    }
}
