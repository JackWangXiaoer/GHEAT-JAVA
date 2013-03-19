package JavaHeatMaps;

import gheat.datasources.*;
import gheat.graphics.ThemeManager;
import org.eclipse.jetty.server.Server;

import java.net.URL;

/**
 * ________    ___ ______________   ________________
 * /  _____/   /   |   \_   _____/  /  _  \__    ___/
 * /   \  ___  /    ~    \    __)_  /  /_\  \|    |
 * \    \_\  \ \    Y    /        \/    |    \    |
 * \______  /  \___|_  /_______  /\____|__  /____|
 * \/         \/        \/         \/
 */
public class App {
    public static DataManager dataManager = null;
    static URL classpathResource = Thread.currentThread().getContextClassLoader().getResource("");

    public static void main(String[] args) throws Exception {
        if (dataManager == null) {

            ThemeManager.init(classpathResource.getPath() + "res/etc/");

            //HeatMapDataSource dataSource = getFileDataSource();
            //HeatMapDataSource dataSource = getQuadTreeDataSource();
            HeatMapDataSource dataSource = getPostGisDataSource();
            dataManager = new DataManager(dataSource);

            System.out.println("======================================= Initialised =======================================");
        }

        Server server = new Server(8080);
        server.setHandler(new TileHandler());

        server.start();
        server.join();

    }
    /*
   Gets PostGIS data source.
   * */
    private static PostGisDataSource getPostGisDataSource() {
        String query = "SELECT ST_AsText(\"wkb_geometry\") as geom ,\"offences\" as weight FROM crimedata WHERE \"wkb_geometry\" @ ST_MakeEnvelope(?,?,?,?,4326)";
        return new PostGisDataSource(query);
    }
    /*
   Gets File tree data source.
   * */
    private static FileDataSource getFileDataSource() {
        return new FileDataSource(classpathResource.getPath() + "points.txt");
    }
    /*
    Gets quad tree data source.
    * */
    private static QuadTreeDataSource getQuadTreeDataSource() {
        return new QuadTreeDataSource(classpathResource.getPath() + "points.txt");
    }
}
