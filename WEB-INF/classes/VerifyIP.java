import java.io.File;
import java.io.IOException;
import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import com.maxmind.geoip.regionName;

public class VerifyIP {

	private static String path = "C:/Users/matho/apache-tomcat-8.5.9/webapps/Dark/GeoIP/GeoLiteCity.dat";

	public static void changepath(String newpath){
		path = newpath;
	}

	public static void main(String[] args) {
		VerifyIP obj = new VerifyIP();
		System.out.println(obj.getRegion("83.198.84.247"));
	}

	public String getRegion(String ip){
		ServerLocation location = getLocation(ip);
		return location.getRegionName();
	}

	public ServerLocation getLocation(String ipAddress) {

		File file = new File(path);
		return getLocation(ipAddress, file);

	}

	public ServerLocation getLocation(String ipAddress, File file) {

		ServerLocation serverLocation = null;

		try {

			serverLocation = new ServerLocation();

			LookupService lookup = new LookupService(file,LookupService.GEOIP_MEMORY_CACHE);
			Location locationServices = lookup.getLocation(ipAddress);

			serverLocation.setCountryCode(locationServices.countryCode);
			serverLocation.setCountryName(locationServices.countryName);
			serverLocation.setRegion(locationServices.region);
			serverLocation.setRegionName(regionName.regionNameByCode(
					locationServices.countryCode, locationServices.region));
			serverLocation.setCity(locationServices.city);
			serverLocation.setPostalCode(locationServices.postalCode);
			serverLocation.setLatitude(String.valueOf(locationServices.latitude));
			serverLocation.setLongitude(String.valueOf(locationServices.longitude));

		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		return serverLocation;

	}
}