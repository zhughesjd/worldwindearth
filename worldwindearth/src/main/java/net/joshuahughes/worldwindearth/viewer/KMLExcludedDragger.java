package net.joshuahughes.worldwindearth.viewer;

import gov.nasa.worldwind.Movable;
import gov.nasa.worldwind.View;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.event.DragSelectEvent;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.geom.Intersection;
import gov.nasa.worldwind.geom.Line;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.util.Logging;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashSet;

/**
 * @author Patrick Murris
 * @version $Id: BasicDragger.java 1171 2013-02-11 21:45:02Z dcollins $
 */
public class KMLExcludedDragger implements SelectListener
{
    private final WorldWindow wwd;
    private boolean dragging = false;
    private boolean useTerrain = true;

    private Point dragRefCursorPoint;
    private Vec4 dragRefObjectPoint;
    private double dragRefAltitude;

    public KMLExcludedDragger(WorldWindow wwd)
    {
        if (wwd == null)
        {
            String msg = Logging.getMessage("nullValue.WorldWindow");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }

        this.wwd = wwd;
    }

    public KMLExcludedDragger(WorldWindow wwd, boolean useTerrain)
    {
        if (wwd == null)
        {
            String msg = Logging.getMessage("nullValue.WorldWindow");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }

        this.wwd = wwd;
        this.setUseTerrain(useTerrain);
    }

    public boolean isUseTerrain()
    {
        return useTerrain;
    }

    public void setUseTerrain(boolean useTerrain)
    {
        this.useTerrain = useTerrain;
    }

    public boolean isDragging()
    {
        return this.dragging;
    }

    public void selected(SelectEvent event)
    {
        if (event == null)
        {
            String msg = Logging.getMessage("nullValue.EventIsNull");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }

        if (event.getEventAction().equals(SelectEvent.DRAG_END))
        {
            this.dragging = false;
            event.consume();
        }
        else if (event.getEventAction().equals(SelectEvent.DRAG))
        {
            DragSelectEvent dragEvent = (DragSelectEvent) event;
            Object topObject = dragEvent.getTopObject();
            if (topObject == null)
                return;

            if (!(topObject instanceof Movable) || topObject.getClass().getName().contains("gov.nasa.worldwind.ogc.kml"))
                return;

            dragObject = (Movable) topObject;
            View view = wwd.getView();
            Globe globe = wwd.getModel().getGlobe();

            // Compute dragged object ref-point in model coordinates.
            // Use the Icon and Annotation logic of elevation as offset above ground when below max elevation.
            Position refPos = dragObject.getReferencePosition();
            if (refPos == null)
                return;

            Vec4 refPoint = globe.computePointFromPosition(refPos);

            if (!this.isDragging())   // Dragging started
            {
                // Save initial reference points for object and cursor in screen coordinates
                // Note: y is inverted for the object point.
                this.dragRefObjectPoint = view.project(refPoint);
                // Save cursor position
                this.dragRefCursorPoint = dragEvent.getPreviousPickPoint();
                // Save start altitude
                this.dragRefAltitude = globe.computePositionFromPoint(refPoint).getElevation();
            }

            // Compute screen-coord delta since drag started.
            int dx = dragEvent.getPickPoint().x - this.dragRefCursorPoint.x;
            int dy = dragEvent.getPickPoint().y - this.dragRefCursorPoint.y;

            // Find intersection of screen coord (refObjectPoint + delta) with globe.
            double x = this.dragRefObjectPoint.x + dx;
            double y = event.getMouseEvent().getComponent().getSize().height - this.dragRefObjectPoint.y + dy - 1;
            Line ray = view.computeRayFromScreenPoint(x, y);
            Position pickPos = null;
            // Use intersection with sphere at reference altitude.
            Intersection inters[] = globe.intersect(ray, this.dragRefAltitude);
            if (inters != null)
                pickPos = globe.computePositionFromPoint(inters[0].getIntersectionPoint());

            if (pickPos != null)
            {
                // Intersection with globe. Move reference point to the intersection point,
                // but maintain current altitude.
                position = new Position(pickPos, dragObject.getReferencePosition().getElevation());
                dragObject.moveTo(position);
            }
            this.dragging = true;
            event.consume();
            fireActionListeners();
        }
    }
    private void fireActionListeners(){
    	for(ActionListener listener : listenerSet)
    		listener.actionPerformed(new ActionEvent(this,-1,"dragging"));
    }
    LinkedHashSet<ActionListener> listenerSet = new LinkedHashSet<>();
	private Position position;
	private Movable dragObject;
    public void addActionListener(ActionListener l){
    	listenerSet.add(l);
    }

	public Movable getMovable() {
		return dragObject;
	}
}