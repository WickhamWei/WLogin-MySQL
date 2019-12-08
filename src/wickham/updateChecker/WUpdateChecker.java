package wickham.updateChecker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import com.google.gson.Gson;

public class WUpdateChecker {
	private String projectUrlString = "https://api.github.com/repos/WickhamWei/WUpdateChecker/releases/latest";
	private HttpURLConnection httpURLConnection;
	private String newestVersionString;
	private String newestVersionPTimeString;
	private String currentVersionString;
	private boolean networkNormal = true;

	public WUpdateChecker(String currentVersionString, String projectUrlString) {
		this.currentVersionString = currentVersionString;
		this.projectUrlString = projectUrlString;
		// TODO 自动生成的构造函数存根
	}

	public String getNewestVersionPTimeString() {
		return newestVersionPTimeString;
	}

	public String getCurrentVersionString() {
		return currentVersionString;
	}

	public String getNewestVersionString() {
		return newestVersionString;
	}

	public boolean isNetworkNormal() {
		return networkNormal;
	}

	public boolean isUpTodate() {
		URL url;
		try {
			url = new URL(projectUrlString);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setConnectTimeout(15000);
			httpURLConnection.setReadTimeout(15000);
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.connect();
			BufferedReader responseReader = new BufferedReader(
					new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
			String allOriginJsonString = responseReader.readLine();
			Gson gson = new Gson();
			Github github = gson.fromJson(allOriginJsonString, Github.class);
			newestVersionString = github.getLastVersion();
			newestVersionPTimeString = github.getLastVersionPublishedTime();
			if (!currentVersionString.equalsIgnoreCase(github.getLastVersion())) {
				httpURLConnection.disconnect();
				return false;
			}
		} catch (SocketTimeoutException e) {
			networkNormal = false;
			// TODO: handle exception
		} catch (IOException e) {
			// TODO: handle exception
			networkNormal = false;
		} finally {
			httpURLConnection.disconnect();
		}
		return true;
	}
}