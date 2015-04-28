package my.jutils;

/**
 * Detect which type of operating system (OS) you are using now. <p>
 * 
 * <i>
 * Source: http://www.mkyong.com/java/how-to-detect-os-in-java-systemgetpropertyosname/
 * 
 * Modified by: Erieze Lagera
 * </i>
 * @author Mkyong
 */
public class OSValidator {
 
    private static final String OS = System.getProperty("os.name").toLowerCase();

    public static boolean isWindows() {
        return OS.contains("win");
    }

    public static boolean isMac() {
        return OS.contains("mac");
    }

    public static boolean isUnix() {
        return OS.contains("nix") || OS.contains("nux") || OS.contains("aix");
    }

    public static boolean isSolaris() {
        return OS.contains("sunos");
    }
 
}