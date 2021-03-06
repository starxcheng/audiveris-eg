//----------------------------------------------------------------------------//
//                                                                            //
//                         R u n s T a b l e V i e w                          //
//                                                                            //
//----------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">
//
// Copyright © Hervé Bitteur and others 2000-2017. All rights reserved.
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
//----------------------------------------------------------------------------//
// </editor-fold>
package org.audiveris.omr.run;

import org.audiveris.omr.selection.LocationEvent;
import org.audiveris.omr.selection.MouseMovement;
import org.audiveris.omr.selection.RunEvent;
import org.audiveris.omr.selection.SelectionHint;
import org.audiveris.omr.selection.SelectionService;
import org.audiveris.omr.selection.UserEvent;

import org.audiveris.omr.ui.view.RubberPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

/**
 * Class {@code RunsTableView} displays a view on an underlying runs
 * table.
 *
 * @author Hervé Bitteur
 */
public class RunsTableView
        extends RubberPanel
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            RunsTableView.class);

    //~ Instance fields --------------------------------------------------------
    /** The underlying table of runs */
    private final RunsTable table;

    //~ Constructors -----------------------------------------------------------
    //---------------//
    // RunsTableView //
    //---------------//
    /**
     * Creates a new RunsTableView object.
     *
     * @param table the underlying table of runs
     */
    public RunsTableView (RunsTable table,
                          SelectionService locationService)
    {
        this.table = table;
        setName(table.getName());

        // Location service
        setLocationService(locationService);

        // Set background color
        setBackground(Color.white);
    }

    //~ Methods ----------------------------------------------------------------
    //---------//
    // onEvent //
    //---------//
    /**
     * Notification about selection objects.
     * We catch:
     * SheetLocation (-> Run)
     *
     * @param event the notified event
     */
    @Override
    @SuppressWarnings("unchecked")
    public void onEvent (UserEvent event)
    {
        try {
            // Ignore RELEASING
            if (event.movement == MouseMovement.RELEASING) {
                return;
            }

            // Default behavior: making point visible & drawing the markers
            super.onEvent(event);

            if (event instanceof LocationEvent) { // Location => Section(s) & Run
                handleEvent((LocationEvent) event);
            }
        } catch (Exception ex) {
            logger.warn(getClass().getName() + " onEvent error", ex);
        }
    }

    //--------//
    // render //
    //--------//
    /**
     * Render the table in the provided Graphics context, which may be
     * already scaled.
     *
     * @param g the graphics context
     */
    @Override
    public void render (Graphics2D g)
    {
        // Render all sections, using the colors they have been assigned
        renderRuns(g);

        // Paint additional items, such as recognized items, etc...
        renderItems(g);
    }

    //-------------//
    // renderItems //
    //-------------//
    /**
     * Room for rendering additional items, if any.
     *
     * @param g the graphic context
     */
    protected void renderItems (Graphics2D g)
    {
        // Void
    }

    //------------//
    // renderRuns //
    //------------//
    protected void renderRuns (Graphics2D g)
    {
        Rectangle clip = g.getClipBounds();

        switch (table.getOrientation()) {
        case HORIZONTAL: {
            int minRow = Math.max(clip.y, 0);
            int maxRow = Math.min((clip.y + clip.height), table.getHeight())
                         - 1;

            for (int row = minRow; row <= maxRow; row++) {
                List<Run> seq = table.getSequence(row);

                for (Run run : seq) {
                    g.setColor(runColor(run));
                    g.fillRect(run.getStart(), row, run.getLength(), 1);
                }
            }
        }

        break;

        case VERTICAL: {
            int minRow = Math.max(clip.x, 0);
            int maxRow = Math.min((clip.x + clip.width), table.getWidth()) - 1;

            for (int row = minRow; row <= maxRow; row++) {
                List<Run> seq = table.getSequence(row);

                for (Run run : seq) {
                    g.setColor(runColor(run));
                    g.fillRect(row, run.getStart(), 1, run.getLength());
                }
            }
        }

        break;
        }
    }

    //----------//
    // runColor //
    //----------//
    protected Color runColor (Run run)
    {
        //        int level = run.getLevel();
        //
        //        return new Color(level, level, level);
        return Color.BLACK;
    }

    //-------------//
    // handleEvent //
    //-------------//
    /**
     * Interest in Location => Run
     *
     * @param locationEvent
     */
    private void handleEvent (LocationEvent locationEvent)
    {
        logger.debug("sheetLocation: {}", locationEvent);

        // Lookup for Run pointed by this pixel location
        // Search and forward run & section info
        Rectangle rect = locationEvent.getData();

        if (rect == null) {
            return;
        }

        SelectionHint hint = locationEvent.hint;
        MouseMovement movement = locationEvent.movement;

        if (!hint.isLocation()) {
            return;
        }

        Point pt = rect.getLocation();
        Run run = table.lookupRun(pt);

        // Publish Run information
        table.getRunService()
                .publish(new RunEvent(this, hint, movement, run));
    }
}
