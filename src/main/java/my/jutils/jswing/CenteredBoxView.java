package my.jutils.jswing;

import javax.swing.text.*;

/**
 * Centered Box View for {@link CenteredEditorKit}.
 * <p>
 *
 * This was used by {@code CenteredEditorKit} to center the text.
 *
 * @author Erieze Lagera
 * @see CenteredEditorKit
 */
public class CenteredBoxView extends BoxView {

    public CenteredBoxView(Element elem, int axis) {
        super(elem, axis);
    }

    @Override
    protected void layoutMajorAxis(int targetSpan, int axis, int[] offsets, int[] spans) {
        super.layoutMajorAxis(targetSpan, axis, offsets, spans);
        int textBlockHeight = 0;
        int offset = 0;

        for (int i = 0; i < spans.length; i++) {
            textBlockHeight = spans[i];
        }
        offset = (targetSpan - textBlockHeight) / 2;
        for (int i = 0; i < offsets.length; i++) {
            offsets[i] += offset;
        }

    }
}
