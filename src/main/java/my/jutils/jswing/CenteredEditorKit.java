package my.jutils.jswing;

import javax.swing.JTextPane;
import javax.swing.text.*;

/**
 * Centered Editor Kit for {@link JTextPane} component.
 * <p>
 * 
 * This centers the text both vertical and horizontal direction.
 * 
 * @author Erieze Lagera
 */
public class CenteredEditorKit extends StyledEditorKit {

    @Override
    public ViewFactory getViewFactory() {
        return new StyledViewFactory();
    }

    private class StyledViewFactory implements ViewFactory {

        @Override
        public View create(Element elem) {
            final String kind = elem.getName();
            if (kind != null) {
                if (kind.equals(AbstractDocument.ContentElementName)) {
                    return new LabelView(elem);
                } else if (kind.equals(AbstractDocument.ParagraphElementName)) {
                    return new ParagraphView(elem);
                } else if (kind.equals(AbstractDocument.SectionElementName)) {
                    return new CenteredBoxView(elem, View.Y_AXIS);
                } else if (kind.equals(StyleConstants.ComponentElementName)) {
                    return new ComponentView(elem);
                } else if (kind.equals(StyleConstants.IconElementName)) {
                    return new IconView(elem);
                }
            }
            return new LabelView(elem);
        }
    }
}
