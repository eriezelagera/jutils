package my.jutils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.atomic.*;
import javaxt.io.Directory;
import org.slf4j.*;

/**
 * Utility for File processing.
 * <p>
 * We've used {@code javaxt} on some of methods under this utility.
 * <br />
 * Note: <i> If a method's Javadoc has @since tag, it means it was not JDK 1.7,
 * since most of these tools uses JDK 1.7 </i>
 *
 * @author Erieze Lagera
 */
public class JFile {

    private static final Logger LOGGER = LoggerFactory.getLogger(JFile.class.getSimpleName());

    /**
     * Check if file exists.
     * <p>
     *
     * @param filename Filename may be relative or absolute, this consists of
     * filename and path
     * @return True if file does exists, otherwise false
     */
    public static boolean exists(final String filename) {
        return new File(filename).exists();
    }

    /**
     * Create a file.
     * <p>
     * This is useful for creating or recreating a config file. This supports
     * the escape characters \n for newline and \t for tab.
     *
     * @param filename Path of the file, absolute or relative
     * @param content Content of the config file extension
     * @return True if no error occurs and file created successfully, otherwise
     * false
     */
    public synchronized static boolean createFile(final String filename, final String content) {
        final char[] contentArr = content.toCharArray();
        final int length = contentArr.length;
        final AtomicInteger first = new AtomicInteger();
        final AtomicInteger next = new AtomicInteger(1);
        try (final BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename), Charset.defaultCharset(), StandardOpenOption.CREATE_NEW)) {
            final AtomicInteger columns = new AtomicInteger();
            while (first.get() < length - 1 && next.get() < length) {
                switch (Strings.concatenateArray(contentArr[first.get()], contentArr[next.get()])) {
                    case "\n":
                        writer.newLine();
                        columns.set(0);
                        break;
                    case "\t":
                        int nextTabStop = (columns.get() + 8) / 8 * 8;
                        writer.append(Strings.space(nextTabStop - columns.get()));
                        columns.addAndGet(nextTabStop);
                        break;
                    default:
                        writer.append(Strings.concatenateArray(contentArr[first.get()], contentArr[next.get()]));
                        columns.incrementAndGet();
                        break;
                }
                first.getAndAdd(2);
                next.getAndAdd(2);
            }
            writer.flush();
            writer.close();
            return true;
        } catch (Exception e) {
            LOGGER.error("Cause: {}", e.toString(), e);
            return false;
        }
    }

    /**
     * Create a blank file.
     * <p>
     * This supports the escape characters \n for newline and \t for tab.
     *
     * @param filename Path of the file, absolute or relative
     * @return True if no error occurs and file created successfully, otherwise
     * false
     */
    public synchronized static boolean createFile(final String filename) {
        return createFile(filename, "");
    }

    /**
     * Create a file.
     * <p>
     * This is useful for creating or recreating a config file. This supports
     * the escape characters \n for newline and \t for tab.
     * <br />
     * Note: <i> Writes text file with UTF-8 encoding. </i>
     *
     * @param filename Path of the file, absolute or relative
     * @param content Content of the config file extension
     * @return True if no error occurs and file created successfully, otherwise
     * false
     * @since JDK 1.6
     */
    public synchronized static boolean createFile6(final String filename, final String content) {
        final AtomicBoolean result = new AtomicBoolean();
        PrintWriter writer = null;
        final char[] contentArr = content.toCharArray();
        final int length = contentArr.length;
        final AtomicInteger first = new AtomicInteger();
        final AtomicInteger next = new AtomicInteger(1);
        try {
            writer = new PrintWriter(filename, "UTF-8");
            final AtomicInteger columns = new AtomicInteger();
            while (first.get() < length - 1 && next.get() < length) {
                switch (Strings.concatenateArray(contentArr[first.get()], contentArr[next.get()])) {
                    case "\n":
                        writer.println();
                        columns.set(0);
                        break;
                    case "\t":
                        int nextTabStop = (columns.get() + 8) / 8 * 8;
                        writer.append(Strings.space(nextTabStop - columns.get()));
                        columns.addAndGet(nextTabStop);
                        break;
                    default:
                        writer.append(Strings.concatenateArray(contentArr[first.get()], contentArr[next.get()]));
                        columns.incrementAndGet();
                        break;
                }
                first.getAndAdd(2);
                next.getAndAdd(2);
            }
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            LOGGER.error("Cause: {}", e.toString(), e);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Cause: {}", e.toString(), e);
        } finally {
            Utils.closeWriter(writer);
        }
        return result.get();
    }

    /**
     * Create a blank file.
     * <p>
     * This supports the escape characters \n for newline and \t for tab.
     * <br />
     * Note: <i> Writes text file with UTF-8 encoding. </i>
     *
     * @param filename Path of the file, absolute or relative
     * @return True if no error occurs and file created successfully, otherwise
     * false
     * @since JDK 1.6
     */
    public synchronized static boolean createFile6(final String filename) {
        return createFile6(filename, "");
    }

    /**
     * Make a folder and a text file inside it.
     *
     * @param directory Absolute path with folder name.
     * @param filename Filename of your text file.
     */
    public synchronized static void mkdir(final String directory, final String filename) {
        try {
            final String _filename = filename + ".txt";
            final File parentDir = new File(directory);
            parentDir.mkdir();
            final File file = new File(parentDir, _filename);
            file.createNewFile();
        } catch (IOException e) {
            LOGGER.error("Cause: {}", e.toString(), e);
        }
    }

    /**
     * Create a directory.
     * <p>
     * If directory does not exist, this will create the specified filename,
     * then set a write permission.
     *
     * @param filename Path of the file, absolute or relative
     * @return True if directory is created, false if directory already exists
     */
    public synchronized static boolean mkdir(final String filename) {
        final File dir = new File(filename);
        final AtomicBoolean created = new AtomicBoolean();
        if (!dir.exists()) {
            dir.mkdir();
            dir.setWritable(true);
            LOGGER.debug("New directory created: {}", filename);
            created.set(true);
        } else {
            LOGGER.debug("Directory already exists...");
            created.set(false);
        }
        return created.get();
    }
    
    /**
     * Create a directory including the non-existent parent directory.
     * <p>
     * If directory does not exist, this will create the specified filename,
     * then set a write permission.
     *
     * @param filename Path of the file, absolute or relative
     * @return True if directory is created, false if directory already exists
     */
    public synchronized static boolean mkdirs(final String filename) {
        final File dir = new File(filename);
        final AtomicBoolean created = new AtomicBoolean();
        if (!dir.exists()) {
            dir.mkdirs();
            dir.setWritable(true);
            LOGGER.debug("New directory created: {}", filename);
            created.set(true);
        } else {
            LOGGER.debug("Directory already exists...");
            created.set(false);
        }
        return created.get();
    }

    /**
     * Delete a file.
     *
     * @param filename Path of the file, absolute or relative
     * @return True if and only if the file or directory is successfully
     * deleted, otherwise false.
     * @see File
     */
    public synchronized static boolean delete(final String filename) {
        return new File(filename).delete();
    }

    /**
     * Create a new file.
     * <p>
     * This will overwrite the existing file, so use this with caution.
     * <br />
     * Note: <i> This uses {@code PrintWriter} in writing the file and wrapped
     * and buffered by {@code BufferedWriter} and {@code FileWriter}.</i>
     *
     * @param filename Path of the file, absolute or relative
     * @return True if file is created, otherwise false
     * @since JDK 1.6
     */
    public synchronized static boolean mkfile(final String filename) {
        PrintWriter writer = null;
        final AtomicBoolean success = new AtomicBoolean();
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
            writer.println();
            writer.flush();
            success.set(true);
        } catch (IOException e) {
            LOGGER.error(e.toString(), e);
            LOGGER.debug("File not created!");
            success.set(false);
        } finally {
            Utils.closeWriter(writer);
            LOGGER.debug("New file created: {}", filename);
        }
        return success.get();
    }

    /**
     * Read a line from file based on the specified line index.
     *
     * @param filename Path of the file, absolute or relative
     * @param index Index of line
     * @return Line from the specified index
     */
    public synchronized static String readLine(final String filename, int index) {
        BufferedReader reader = null;
        final AtomicInteger idx = new AtomicInteger();
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
            String line;
            while ((line = reader.readLine()) != null) {
                if (idx.getAndIncrement() == index) {
                    break;
                }
            }
            return line;
        } catch (IOException e) {
            LOGGER.error("Cause: {}", e.toString(), e);
            return null;
        } finally {
            Utils.closeReader(reader);
        }
    }

    /**
     * Read the first line from the specified file.
     *
     * @param filename Path of the file, absolute or relative
     * @return First line of the file
     */
    public synchronized static String readFirst(final String filename) {
        return readLine(filename, 0);
    }

    /**
     * Renames the file denoted by this abstract pathname.
     * <p>
     * This will only work if the source and destination is within the same file
     * system.
     *
     * @param src Source of file to be moved
     * @param dest Destination of the moved file
     * @return True if file was successfully moved, otherwise false if the
     * destination file exists or moved between the different file system
     */
    public synchronized static boolean renameTo(final String src, final String dest) {
        return new File(src).renameTo(new File(dest));
    }

    /***
     * Added by Einar Lagera Jan 8, 2015
     */

    /**
     * Gets the time when the file is created.
     *
     * @param file Instance of {@code java.io.File}
     * @return Date and time of file creation, otherwise null if there's an
     * exception thrown.
     */
    public static Date getFileCreationTime(final File file) {
        try {
            return new Date(Files.readAttributes(Paths.get(file.toURI()), 
                            BasicFileAttributes.class).creationTime().toMillis());
        } catch (Exception e) {
            LOGGER.error("Cause: {}", e.toString(), e);
            return null;
        }
    }
    
    /**
     * Gets the time when the file is created.
     *
     * @param filename Path of the file, absolute or relative
     * @return Date and time of file creation, otherwise null if there's an
     * exception thrown.
     */
    public static Date getFileCreationTime(final String filename) {
        return getFileCreationTime(new File(filename));
    }
    
    /**
     * Gets the time when the file is created.
     *
     * @param file Instance of {@code java.io.File}
     * @return Date of file creation, never throws exception
     * @since JDK 1.6
     */
    public static Date getFileCreationTime6(final File file) {
        return new javaxt.io.File(file).getCreationTime();
    }
    
    /**
     * Gets the time when the file is created.
     *
     * @param filename Path of the file, absolute or relative
     * @return Date of file creation, never throws exception
     * @since JDK 1.6
     */
    public static Date getFileCreationTime6(final String filename) {
        return getFileCreationTime6(new File(filename));
    }
    
    /**
     * Get the file extension of the given file.
     * <p>
     * Result will not include the dot (.) separator which
     * separates filename and file extension.
     * 
     * @param filename Path of the file, absolute or relative
     * @return File extension
     */
    public static String getFileExt(String filename) {
        final String[] result = filename.split("\\.");
        return result[result.length-1];
    }
    
    /**
     * Gets all the files in the directory/folder.
     * <p>
     * This will only gets all the files, directory is not included.
     *
     * @param folder Directory of the files.
     * @return Collection of File objects, otherwise an empty collection if
     * there's no file present or exception occurred
     * @since JDK 1.6
     */
    public static Collection<File> getFilesOfFolder(final File folder) {
        final Collection<File> files = new ArrayList<File>();
        try {
            for (final File fileEntry : folder.listFiles()) {
                if (!fileEntry.isDirectory()) { // Only add if it's not a directory
                    files.add(fileEntry);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Cause: {}", e.toString(), e);
        }
        return files;
    }
    
    /**
     * Get {@code Collection} of filename from the given directory.
     * 
     * @param directory Path of directory, absolute or relative
     * @param recursively Include child folders?
     * @return {@code Collection} of filename
     */
    public static Collection<String> getFileNamesFromDir(String directory, boolean recursively) {
        return getFileNamesFromDir(directory, "*.*", recursively);
    }
    
    /**
     * Get {@code Collection} of filename from the given directory.
     * 
     * @param directory Path of directory, absolute or relative
     * @param filter Filter file, can be regular expression
     * @param recursively Include child folders?
     * @return {@code Collection} of filename
     */
    public static Collection<String> getFileNamesFromDir(String directory, String filter, boolean recursively) {
        final Collection<String> filenames = new ArrayList<String>();
        for (javaxt.io.File file : new Directory(directory).getFiles(filter, recursively)) {
            filenames.add(file.getName());
        }
        return filenames;
    }

    /**
     * Check if the given {@code filename} has the extension equal to the given
     * {@code ext}.
     * 
     * @param filename Path of the file, absolute or relative
     * @param ext File extension to be evaluated
     * @return True if the given {@code filename} has the given {@code ext},
     * otherwise false
     */
    public static boolean checkExt(String filename, String ext) {
        return new javaxt.io.File(filename).getExtension().equalsIgnoreCase(ext);
    }
    
}
