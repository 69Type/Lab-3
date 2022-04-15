import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

/**
 * This class stores the basic state necessary for the A* algorithm to compute a
 * path across a map.  This state includes a collection of "open waypoints" and
 * another collection of "closed waypoints."  In addition, this class provides
 * the basic operations that the A* pathfinding algorithm needs to perform its
 * processing.
 **/
public class AStarState
{
    HashMap<Location, Waypoint> openWaypoints = new HashMap();//открытые точки
    HashMap<Location, Waypoint> closedWaypoints = new HashMap();//закрытые точки


    // Этот метод возвращает количество точек в наборе открытых вершин.
    public int numOpenWaypoints(){
        return openWaypoints.size();
    }

    // Ищет путь с наименьшей общей стоимостью
    public Waypoint getMinOpenWaypoint()
    {
        if (numOpenWaypoints() == 0) return null;

        // инициализируем набор ключей всех открытых waypoint,
        //итератор для итерации по набору
        //и переменную для хранения лучшей waypoint и стоимости этой путевой точки.
        Set openWaypointKeys = openWaypoints.keySet();
        Iterator i = openWaypointKeys.iterator();
        Waypoint best = null;
        float best_cost = Float.MAX_VALUE;

        // сканируем все открытые waypoints.
        while (i.hasNext())
        {
            // сохраняет текущее местоположение.
            Location location = (Location) i.next();
            // сохраняет текущий waypoint.
            Waypoint waypoint = openWaypoints.get(location);
            // сохраняет общую стоимость текущего waypoint.
            float waypoint_total_cost = waypoint.getTotalCost();

            // если общая стоимость для текущей waypoint  лучше (ниже)
            // чем сохраненная стоимость для сохраненной лучшей waypoint, то обменяем их
            if (waypoint_total_cost < best_cost)
            {
                best = openWaypoints.get(location);
                best_cost = waypoint_total_cost;
            }

        }
        // возвращает путевую точку с минимальной общей стоимостью.
        return best;
    }

    /** This is a reference to the map that the A* algorithm is navigating. **/
    private Map2D map;


    /**
     * Initialize a new state object for the A* pathfinding algorithm to use.
     **/
    public AStarState(Map2D map)
    {
        if (map == null)
            throw new NullPointerException("map cannot be null");

        this.map = map;
    }

    /** Returns the map that the A* pathfinder is navigating. **/
    public Map2D getMap()
    {
        return map;
    }

    /**
     * This method adds a waypoint to (or potentially updates a waypoint already
     * in) the "open waypoints" collection.  If there is not already an open
     * waypoint at the new waypoint's location then the new waypoint is simply
     * added to the collection.  However, if there is already a waypoint at the
     * new waypoint's location, the new waypoint replaces the old one <em>only
     * if</em> the new waypoint's "previous cost" value is less than the current
     * waypoint's "previous cost" value.
     **/
    public boolean addOpenWaypoint(Waypoint newWP)
    {
        // Последней (верхней) точки линии (waypoint).
        Location location = newWP.getLocation();

        // Проверяет, есть ли уже открытая путевая точка в новом
        // местоположении путевой точки.
        if (openWaypoints.containsKey(location))
        {
            // Если в новой путевой точке уже есть открытая путевая точка
            // местоположения, проверяет, является ли у новой путевая точка предыдущее
            // значение "стоимости" меньше, чем предыдущее текущей путевой точки
            // стоимость значение.
            Waypoint current_waypoint = openWaypoints.get(location);
            if (newWP.getPreviousCost() < current_waypoint.getPreviousCost())
            {
                // Если значение "предыдущей стоимости" новой путевой точки меньше, чем
                // значение "предыдущей стоимости" текущей путевой точки, новая путевая точка
                // заменяет старую путевую точку и возвращает true.
                openWaypoints.put(location, newWP);
                return true;
            }
            // Если значение "предыдущей стоимости" новой путевой точки не меньше, чем
            // значение "предыдущей стоимости" текущей путевой точки, возврат false.
            return false;
        }
        // Если в новой путевой точке еще нет открытой путевой точки
        // местоположения, добавляем новую путевую точку в коллекцию открытых путевых точек
        // и вернем true.
        openWaypoints.put(location, newWP);
        return true;
    }



    /**
     * This method moves the waypoint at the specified location from the
     * open list to the closed list.
     **/
    public void closeWaypoint(Location loc)
    {
        Waypoint waypoint = openWaypoints.remove(loc);
        closedWaypoints.put(loc, waypoint);
    }


    /**
     * Returns true if the collection of closed waypoints contains a waypoint
     * for the specified location.
     **/
    public boolean isLocationClosed(Location loc)
    {
        return closedWaypoints.containsKey(loc);
    }
}
