package my.jutils.doc.filters;

import javax.swing.text.*;

/**
 * Document filter for Lowercase text.
 * <p>
 * Usage: <i> ((AbstractDocument) <b>jTextField</b>.getDocument()).setDocumentFilter(<u>new LowercaseDocumentFilter()</u>);</i>
 *
 * @author Erieze Lagera
 * @see DocumentFilter
 */
public class LowercaseDocumentFilter extends DocumentFilter {

    @Override
    public void insertString(DocumentFilter.FilterBypass fb, int offset,
            String text, AttributeSet attr) throws BadLocationException {
        fb.insertString(offset, text.toLowerCase(), attr);
    }

    @Override
    public void replace(DocumentFilter.FilterBypass fb, int offset, int length,
            String text, AttributeSet attrs) throws BadLocationException {
        fb.replace(offset, length, text.toLowerCase(), attrs);
    }
}
