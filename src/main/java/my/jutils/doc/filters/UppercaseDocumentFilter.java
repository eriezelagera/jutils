package my.jutils.doc.filters;

import javax.swing.text.*;

/**
 * Document filter for Uppercase text.
 * <p>
 * Usage: <i> ((AbstractDocument) <b>jTextField</b>.getDocument()).setDocumentFilter(<u>new UppercaseDocumentFilter()</u>);</i>
 *
 * @author Erieze Lagera
 * @see DocumentFilter
 */
public class UppercaseDocumentFilter extends DocumentFilter {

    @Override
    public void insertString(DocumentFilter.FilterBypass fb, int offset,
            String text, AttributeSet attr) throws BadLocationException {
        fb.insertString(offset, text.toUpperCase(), attr);
    }

    @Override
    public void replace(DocumentFilter.FilterBypass fb, int offset, int length,
            String text, AttributeSet attrs) throws BadLocationException {
        fb.replace(offset, length, text.toUpperCase(), attrs);
    }
}
