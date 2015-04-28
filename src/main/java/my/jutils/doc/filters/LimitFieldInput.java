package my.jutils.doc.filters;

import javax.swing.text.*;

/**
 * Limits jTextField input length.
 * <p>
 * This will override the "column" property of the jTextField (it was somehow
 * not working as is). In order to apply this property to your jTextField, you
 * must invoke setDocument() from your jTextField to work as it has to be.
 *
 * @author Erieze Lagera
 */
public class LimitFieldInput extends PlainDocument {

    private int limit;

    /**
     * Setup the parameter for number of input characters.
     *
     * @param limit Possible limit of characters for a specified document or
     * field.
     */
    public LimitFieldInput(int limit) {
        super();
        setLimit(limit);
    }

    @Override
    public void insertString(int offset, String s, AttributeSet attributeSet) throws BadLocationException {
        if (offset < limit) {
            super.insertString(offset, s, attributeSet);
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

}
