//                   ::::::::          
//         :+:      :+:    :+:         
//    +++++++++++  +:+         +++++   
//       +:+      +#+         +#  +#   
//      +#+      +#+         +#        
//     #+#      #+#     +#  +#  +#     
//    ###       ########+   ####+      

package com.vdyll.utils.file;

import java.io.File;
import java.util.List;

public class FileWalker {

    public static List<File> walk(String path, boolean debug, List<File> fileList,
            String[] filterArray, boolean recursive) {

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
                                    // if it's a dir, walk through all the files in it
                                    walk(f.getAbsolutePath(), debug, fileList, filterArray,
                                            recursive);
                                } else if (debug) {
                                    System.out.println("Skipping Dir:" + f.getAbsoluteFile());
                                }
                            } else {
                                // if an extension filter has been specified loop through each
                                // extension until we find a match, or don't add the file
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
                                // add each file to main list if it matches the extension filter
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
