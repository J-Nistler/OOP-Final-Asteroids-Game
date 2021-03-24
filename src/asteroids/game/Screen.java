package asteroids.game;

import static asteroids.game.Constants.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.Iterator;
import javax.swing.*;

/**
 * The area of the display in which the game takes place.
 */
@SuppressWarnings("serial")
public class Screen extends JPanel
{
    /** Legend that is displayed across the screen */
    private String legend;

    /** Game controller */
    private Controller controller;


    /**
     * Creates an empty screen
     */
    public Screen (Controller controller)
    {
        this.controller = controller;
        legend = "";
        setPreferredSize(new Dimension(SIZE, SIZE));
        setMinimumSize(new Dimension(SIZE, SIZE));
        setBackground(Color.black);
        setForeground(Color.white);
        setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 120));
        setFocusable(true);
    }

    /**
     * Set the legend
     */
    public void setLegend (String legend)
    {
        this.legend = legend;
    }

    /**
     * Paint the participants onto this panel
     */
    @Override
    public void paintComponent (Graphics graphics)
    {
        // Use better resolution
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Do the default painting
        super.paintComponent(g);

        // Draw each participant in its proper place
        Iterator<Participant> iter = controller.getParticipants();
        while (iter.hasNext())
        {
            iter.next().draw(g);
        }

        // Draw the legend across the middle of the panel
        int size = g.getFontMetrics().stringWidth(legend);
        g.drawString(legend, (SIZE - size) / 2, SIZE / 2);
        g.setFont(new Font("Comic sans", Font.PLAIN, 32));
        g.drawString(String.valueOf(controller.getScore()), LABEL_VERTICAL_OFFSET + 5, LABEL_VERTICAL_OFFSET);
        g.drawString(String.valueOf(controller.getLevel()), SIZE - LABEL_VERTICAL_OFFSET, LABEL_VERTICAL_OFFSET);
        if (Integer.valueOf(controller.getLives()) == 3)
        {
            Path2D.Double poly = new Path2D.Double();
            poly.moveTo(20 - 45 + 40, -40 + 80);
            poly.lineTo(32 - 45 + 40, 0 + 80);
            poly.lineTo(30 - 45 + 40, -7 + 80);
            poly.lineTo(10 - 45 + 40, -7 + 80);
            poly.lineTo(8 - 45 + 40, 0 + 80);
            poly.closePath();
            poly.moveTo(20 - 15 + 40, -40 + 80);
            poly.lineTo(32 - 15 + 40, 0 + 80);
            poly.lineTo(30 - 15 + 40, -7 + 80);
            poly.lineTo(10 - 15 + 40, -7 + 80);
            poly.lineTo(8 - 15 + 40, 0 + 80);
            poly.closePath();
            poly.moveTo(20 + 15 + 40, -40 + 80);
            poly.lineTo(32 + 15 + 40, 0 + 80);
            poly.lineTo(30 + 15 + 40, -7 + 80);
            poly.lineTo(10 + 15 + 40, -7 + 80);
            poly.lineTo(8 + 15 + 40, 0 + 80);
            poly.closePath();
            g.draw(poly);
        }
        else if (Integer.valueOf(controller.getLives()) == 2)
        {
            Path2D.Double poly = new Path2D.Double();
            poly.moveTo(20 - 45 + 40, -40 + 80);
            poly.lineTo(32 - 45 + 40, 0 + 80);
            poly.lineTo(30 - 45 + 40, -7 + 80);
            poly.lineTo(10 - 45 + 40, -7 + 80);
            poly.lineTo(8 - 45 + 40, 0 + 80);
            poly.closePath();
            poly.moveTo(20 - 15 + 40, -40 + 80);
            poly.lineTo(32 - 15 + 40, 0 + 80);
            poly.lineTo(30 - 15 + 40, -7 + 80);
            poly.lineTo(10 - 15 + 40, -7 + 80);
            poly.lineTo(8 - 15 + 40, 0 + 80);
            poly.closePath();
            g.draw(poly);

        }
        else if (Integer.valueOf(controller.getLives()) == 1)
        {
            Path2D.Double poly = new Path2D.Double();
            poly.moveTo(20 - 45 + 40, -40 + 80);
            poly.lineTo(32 - 45 + 40, 0 + 80);
            poly.lineTo(30 - 45 + 40, -7 + 80);
            poly.lineTo(10 - 45 + 40, -7 + 80);
            poly.lineTo(8 - 45 + 40, 0 + 80);
            poly.closePath();
            g.draw(poly);
        }
    }

}
