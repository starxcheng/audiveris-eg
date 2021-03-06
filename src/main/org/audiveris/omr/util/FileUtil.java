//----------------------------------------------------------------------------//
//                                                                            //
//                              F i l e U t i l                               //
//                                                                            //
//----------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">
//
// Copyright © Hervé Bitteur and others 2000-2017. All rights reserved.
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
//----------------------------------------------------------------------------//
// </editor-fold>
package org.audiveris.omr.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Class {@code FileUtil} gathers convenient utility methods for files
 *
 * @author Hervé Bitteur
 */
public class FileUtil
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            FileUtil.class);

    //~ Constructors -----------------------------------------------------------
    // Not meant to be instantiated
    private FileUtil ()
    {
    }

    //~ Methods ----------------------------------------------------------------
    //------//
    // copy //
    //------//
    /**
     * Copy one file to another
     *
     * @param source the file to be read
     * @param target the file to be written
     *
     * @exception IOException raised if operation fails
     */
    public static void copy (File source,
                             File target)
            throws IOException
    {
        FileChannel input = null;
        FileChannel output = null;

        try {
            input = new FileInputStream(source).getChannel();
            output = new FileOutputStream(target).getChannel();

            MappedByteBuffer buffer = input.map(
                    FileChannel.MapMode.READ_ONLY,
                    0,
                    input.size());
            output.write(buffer);
        } finally {
            if (input != null) {
                input.close();
            }

            if (output != null) {
                output.close();
            }
        }
    }

    //-----------//
    // deleteAll //
    //-----------//
    /**
     * Recursively delete the provided files and directories
     *
     * @param files the array of files/dir to delete
     */
    public static void deleteAll (File[] files)
    {
        for (File file : files) {
            if (file.isDirectory()) {
                deleteAll(file.listFiles());
            }

            if (!file.delete()) {
                logger.warn("Could not delete file {}", file);
            }
        }
    }

    //--------------//
    // getExtension //
    //--------------//
    /**
     * From a file "path/name.ext", return the ".ext" part.
     *
     * <P><b>Nota</b>, the dot character is part of the extension, since we
     * could have the following cases: <ul>
     *
     * <li> "path/name.ext" -> ".ext"
     *
     * <li> "path/name." -> "." (just the dot)
     *
     * <li> "path/name" -> "" (the empty string) </ul>
     *
     * @param file the File to process
     *
     * @return the extension, which may be ""
     */
    public static String getExtension (File file)
    {
        String name = file.getName();
        int i = name.lastIndexOf('.');

        if (i >= 0) {
            return name.substring(i);
        } else {
            return "";
        }
    }

    //----------------------//
    // getNameSansExtension //
    //----------------------//
    /**
     * From a file "path/name.ext", return the "name"
     *
     * @param file
     *
     * @return just the name, w/o path and extension
     */
    public static String getNameSansExtension (File file)
    {
        String name = file.getName();
        int i = name.lastIndexOf('.');

        if (i >= 0) {
            return name.substring(0, i);
        } else {
            return name;
        }
    }
}
