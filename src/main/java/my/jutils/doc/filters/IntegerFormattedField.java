package my.jutils.doc.filters;

import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.*;
import javax.swing.text.*;
import org.slf4j.*;

/**
 * Force a jTextField to accept integer values only.
 * <p>
 * In order to invoke this to a {@link JFormattedTextField}, you must customize your jTextField's
 * code, from default-code to custom-creation. Then change the creation of
 * jTextField's instance to the instance of this class (IntegerField).
 *
 * @author Erieze Lagera
 */
public class IntegerFormattedField extends JFormattedTextField {

    private int limit;
    private String fieldName;

    private static final Logger LOGGER = LoggerFactory.getLogger(IntegerFormattedField.class.getSimpleName());

    /**
     * Create a simple instance of IntegerField.
     *
     * @param name Name of jTextField who created this instance
     */
    public IntegerFormattedField(String name) {
        super();
        setFieldName(name);
    }

    /**
     * Create an instance with column length limitation. This will limit user
     * input length based on your set limit.
     *
     * @param name Name of jTextField who created this instance
     * @param limit Preferred column length
     */
    public IntegerFormattedField(String name, int limit) {
        super(limit);
        setLimit(limit);
        setFieldName(name);
        if (limit <= 0) {
            LOGGER.warn("Bad limit value from field " + getFieldName() + ", no limitation will be implemented...");
        }
    }

    @Override
    protected Document createDefaultModel() {
        return new UpperCaseDocument();
    }

    /**
     * Overrides the document of your jTextField, to perform the hack.
     */
    private class UpperCaseDocument extends PlainDocument {
        @Override
        public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
            if (str == null) {
                return;
            }
            final char[] charArr = str.toCharArray();
            final AtomicBoolean ok = new AtomicBoolean(true);
            for (int i = 0; i < charArr.length; i++) {
                final String s = String.valueOf(charArr[i]);
                if (s.equals(".")) {
                    continue;
                }
                try {
                    Integer.parseInt(s);
                } catch (NumberFormatException e) {
                    ok.set(false);
                    break;
                }
            }
            if (ok.get()) {
                if (limit > 0) {
                    if (offset < limit) {
                        super.insertString(offset, new String(charArr), a);
                    }
                } else {
                    super.insertString(offset, new String(charArr), a);
                }
            }
        }
    }

    /**
     * Get the current value for limit.
     *
     * @return The value of <i> limit </i>.
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Set the input limit for your jTextField.
     *
     * @param newValue Limit value
     */
    private void setLimit(int newValue) {
        this.limit = newValue;
    }

    /**
     * Get the jTextField name who create this instance.
     *
     * @return Name of your jTextField
     */
    public final String getFieldName() {
        return fieldName;
    }

    /**
     * Set the name of jTextField who create this instance.
     *
     * @param fieldName Name of your jTextField
     */
    public final void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

}
