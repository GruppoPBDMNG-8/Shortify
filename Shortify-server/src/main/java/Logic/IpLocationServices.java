package logic;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;

/**
 * Created by Giuseppe on 08/03/2016.
 */
public class IpLocationServices {

    // A File object pointing to your GeoIP2 or GeoLite2 database
    private File database;
    private static final String dbPath = "/Shortify-server/src/main/java/Logic/GeoLite2-Country.mmdb";
    private DatabaseReader reader;

    // This creates the DatabaseReader object, which should be reused across
// lookups.
    public IpLocationServices() {
        database = new File(System.getProperty("user.dir").replace("/target", "") + dbPath);
        try {
            reader = new DatabaseReader.Builder(database).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCountry(String ip) throws IOException, GeoIp2Exception {

        InetAddress ipAddress = InetAddress.getByName(ip);
        CountryResponse response = reader.country(ipAddress);
        Country country = response.getCountry();

        return country.getIsoCode();
    }
}
