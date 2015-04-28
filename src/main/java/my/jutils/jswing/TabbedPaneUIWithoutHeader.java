package my.jutils.jswing;

import java.awt.*;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

/**
 * Modified implementation of {@link BasicTabbedPaneUI} which
 * hides the header tab.
 * <p>
 * This type of UI is useful for hiding tab from the interface. In using this
 * UI, you'll need to use {@link JTabbedPane#setSelectedIndex(int)} to 
 * navigate thru tabs since it was now hidden to interface.
 * <br />
 * Usage: <i> {@code jTabbedPane.setUI(new TabbedPaneUIWithoutHeader());} </i>
 * 
 * @author Erieze Lagera
 */
public class TabbedPaneUIWithoutHeader extends BasicTabbedPaneUI {

    @Override
    protected int calculateTabAreaHeight(
            int tabPlacement, int horizRunCount, int maxTabHeight) {
        return 0;
    }

    @Override
    protected void paintTab(
            Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex,
            Rectangle iconRect, Rectangle textRect) {
    }

    @Override
    protected void paintContentBorder(
            Graphics g, int tabPlacement, int selectedIndex) {
    }

    @Override
    public int tabForCoordinate(JTabbedPane pane, int x, int y) {
        return -1;
    }
}
