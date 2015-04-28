package my.jutils;

import com.tutego.jrtf.Rtf;
import java.io.*;
import java.util.*;
import org.slf4j.*;

/**
 * Create receipt file in Rich Text Format (RTF).
 * <p>
 * 
 * 
 * @author Erieze Lagera
 */
public class ReceiptRTF {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiptRTF.class.getSimpleName());
    
    /**
     * Template file of the receipt.
     */
    private final String template;
    /**
     * Parameters to be loaded to the receipt.
     */
    private final Map<String, Object> params;
    /**
     * Resulting receipt file path.
     */
    private final String result;
    
    private File inFile;
    private FileInputStream fis;
    private File outFile;
    private FileOutputStream fos;

    /**
     * Create receipt file in Rich Text Format (RTF).
     * <p>
     * Template file will be based from the given filename and will supply
     * an empty parameters as default with this instance.
     * <br />
     * This instance is useful for printing RTF file which does not contains
     * any parameters.
     * 
     * @param template Path of the template file, absolute or relative path
     * @param result Resulting file path
     */
    public ReceiptRTF(String template, String result) {
        this(template, new HashMap<String, Object>(), result);
    }
    
    /**
     * Create receipt file in Rich Text Format (RTF).
     * <p>
     * Template file will be based from the filename "template.rtf" as default
     * and will supply parameters based on the given {@link Map}.
     * <br />
     * Template file should be located at the following directory: <br />
     * For development: <br />
     * <ul>
     * <li> NetBeans IDE - "project_folder/template.rtf" (together with 
     * folders like {@code src}, {@code nbproject} or {@code lib})
     * <li> Eclipse IDE - considering you have source folder named "templates",
     * "templates/template.rtf"
     * </ul>
     * For deployment: <br />
     * Just place "template.rtf" file together with the {@code jar} file of your
     * application.
     * <br/>
     * This is useful for using single template file which also contains
     * parameters.
     * 
     * @param params {@link Map} of parameters which contains 
     * key and values which will be injected into the template file
     * @param result Resulting file path
     */
    public ReceiptRTF(Map<String, Object> params, String result) {
        this("template.rtf", params, result);
    }
    
    /**
     * Create receipt file in Rich Text Format (RTF).
     * <p>
     * Template file will be based from the given template path
     * and will supply parameters based on the given {@link Map}.
     * <br />
     * Template file should be located at the following directory: <br />
     * For development: <br />
     * <ul>
     * <li> NetBeans IDE - "project_folder/template.rtf" (together with 
     * folders like {@code src}, {@code nbproject} or {@code lib})
     * <li> Eclipse IDE - considering you have source folder named "templates",
     * "templates/template.rtf"
     * </ul>
     * For deployment: <br />
     * Just place "template.rtf" file together with the {@code jar} file of your
     * application.
     * 
     * @param template Path of the template file, absolute or relative path
     * @param params 
     * @param result Resulting file path
     */
    public ReceiptRTF(String template, Map<String, Object> params, String result) {
        this.template = template;
        this.params = params;
        this.result = result;
    }
    
    /**
     * Loads template file into {@code InputStream}.
     */
    private void load() {
        try {
            inFile = new File(template);
            fis = new FileInputStream(inFile);
        } catch (FileNotFoundException e) {
            LOGGER.error(e.toString(), e);
        }
    }
    
    private void out() {
        try {
            outFile = new File(result);
            fos = new FileOutputStream(outFile);
        } catch (FileNotFoundException e) {
            LOGGER.error(e.toString(), e);
        }
    }
    
    /**
     * Check template file and result file type.
     * 
     * @return True if template file and result file does
     * have the correct file extension, otherwise false
     */
    private boolean check() {
        return JFile.checkExt(template, "rtf") && JFile.checkExt(result, "rtf");
    }
    
    /**
     * Create the receipt file.
     * <p>
     * This includes checking of the existence of the template file.
     * 
     * @return True if receipt file is created successfully, 
     * otherwise false
     */
    public boolean create() {
        if (!JFile.exists(template)) {
            LOGGER.error("[FileNotFound] Template file not found. template = {}", template);
            return false;
        }
        if (!check()) {
            return false;
        }
        load();
        out();
        
        Rtf.template(fis).inject(params).out(fos);
        LOGGER.info("Receipt file {} created, located at {}.", outFile.getName(), outFile.getAbsolutePath());
        return true;
    }
    
}
