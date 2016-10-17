import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by LuoLiangchen on 16/9/1.
 *
 * Reference: http://alienryderflex.com/polygon/
 */
public class GeoFencingUtils {

    public static boolean isInPolygon(String position, String polygon)
            throws PointFormatException, PolygonFormatException {
        Point point = formatPoint(position);
        List<Edge> edges = formatPolygon(polygon);

        boolean result = false;

        for (Edge edge: edges) {
            if (((edge.start.longitude < point.longitude && edge.end.longitude >= point.longitude)
                    || (edge.end.longitude < point.longitude && edge.start.longitude >= point.longitude))
                    && (edge.start.latitude <= point.latitude || edge.end.latitude <= point.latitude)) {
                result ^= (edge.end.latitude +
                                (point.longitude - edge.end.longitude)
                                / (edge.start.longitude - edge.end.longitude)
                                * (edge.start.latitude - edge.end.latitude) < point.latitude);
            }
        }
        return result;
    }

    private static Point formatPoint(String position) throws PointFormatException {
        if (!position.contains(",")) throw new PointFormatException("Comma is not exist");

        String[] coordinate = position.split(",");
        if (coordinate.length != 2) throw new PointFormatException("There are more than one comma");

        float longitude;
        float latitude;
        try {
            longitude = Float.parseFloat(coordinate[0]);
            latitude = Float.parseFloat(coordinate[1]);
        } catch (NumberFormatException e) {
            throw new PointFormatException("Coordinate is not a float value");
        }
        return new Point(longitude, latitude);
    }

    private static List<Edge> formatPolygon(String polygon)
            throws PointFormatException, PolygonFormatException {
        if (!polygon.contains(";")) throw new PolygonFormatException("Semicolon is not exist");

        String[] pointArray = polygon.split(";");
        if (pointArray.length < 3) throw new PolygonFormatException("The edge number should 3 or larger");

        List<Point> points = Stream.of(pointArray).map(GeoFencingUtils::formatPoint).collect(Collectors.toList());
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(points.get(points.size() - 1), points.get(0)));
        for (int i = 1; i < points.size(); ++i) edges.add(new Edge(points.get(i - 1), points.get(i)));
        return edges;
    }
}
