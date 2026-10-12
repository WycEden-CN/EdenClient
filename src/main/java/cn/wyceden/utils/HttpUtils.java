package cn.wyceden.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import cn.wyceden.logger.EdenLogger;

public class HttpUtils {

	private static final String ACCEPTED_RESPONSE = "application/json";
	private static final String DEFAULT_USER_AGENT = "Mozilla/5.0";
	private static final int DEFAULT_TIMEOUT = 5000;
	private static final int MAX_REDIRECTS = 5;
	private static final Gson GSON = new Gson();

	public static JsonObject readJson(HttpURLConnection connection) {
		if (connection == null) {
			return null;
		}
		return GSON.fromJson(readResponse(connection), JsonObject.class);
	}

	public static JsonObject postJson(String url, Object request) {

		HttpURLConnection connection = setupConnection(url, DEFAULT_USER_AGENT, DEFAULT_TIMEOUT, false);

		if (connection == null) {
			return null;
		}

		try {
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.addRequestProperty("Content-Type", ACCEPTED_RESPONSE);
			connection.addRequestProperty("Accept", ACCEPTED_RESPONSE);

			try (OutputStream out = connection.getOutputStream()) {
				out.write(GSON.toJson(request).getBytes(StandardCharsets.UTF_8));
			}

			return readJson(connection);
		} catch (IOException e) {
			EdenLogger.error("HTTP", "Failed to send POST request to " + url, e);
			return null;
		} finally {
			connection.disconnect();
		}
	}

	public static String readResponse(HttpURLConnection connection) {
		return readResponse(connection, 0);
	}

	private static String readResponse(HttpURLConnection connection, int redirectCount) {

		if (connection == null) {
			return "";
		}

		try {
			String redirection = connection.getHeaderField("Location");

			if (redirection != null && redirectCount < MAX_REDIRECTS) {
				return readResponse(setupConnection(redirection, DEFAULT_USER_AGENT, DEFAULT_TIMEOUT, false),
						redirectCount + 1);
			}

			InputStream stream = connection.getResponseCode() >= 400 ? connection.getErrorStream()
					: connection.getInputStream();

			if (stream == null) {
				return "";
			}

			StringBuilder response = new StringBuilder();

			try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
				String line;
				while ((line = br.readLine()) != null) {
					response.append(line).append('\n');
				}
			}

			return response.toString();
		} catch (IOException e) {
			EdenLogger.error("HTTP", "Failed to read HTTP response", e);
			return "";
		} finally {
			connection.disconnect();
		}
	}

	public static JsonObject readJson(String url, Map<String, String> headers, String userAgent) {

		HttpURLConnection connection = setupConnection(url, userAgent, DEFAULT_TIMEOUT, false);

		if (connection == null) {
			return null;
		}

		try {
			if (headers != null) {
				for (Map.Entry<String, String> header : headers.entrySet()) {
					connection.addRequestProperty(header.getKey(), header.getValue());
				}
			}

			InputStream stream = connection.getResponseCode() != 200 ? connection.getErrorStream()
					: connection.getInputStream();

			if (stream == null) {
				return null;
			}

			try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
				return GSON.fromJson(readResponse(reader), JsonObject.class);
			}
		} catch (IOException e) {
			EdenLogger.error("HTTP", "Failed to read JSON from " + url, e);
			return null;
		} finally {
			connection.disconnect();
		}
	}

	public static JsonObject readJson(String url, Map<String, String> headers) {
		return readJson(url, headers, DEFAULT_USER_AGENT);
	}

	private static String readResponse(BufferedReader reader) {

		if (reader == null) {
			return null;
		}

		try {
			StringBuilder sb = new StringBuilder();
			String line;

			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

			return sb.toString();
		} catch (IOException e) {
			EdenLogger.error("HTTP", "Failed to read buffered response", e);
			return null;
		}
	}

	public static HttpURLConnection setupConnection(String url, String userAgent, int timeout, boolean useCaches) {

		try {
			HttpURLConnection connection = (HttpURLConnection) new URI(url).toURL().openConnection();

			connection.setRequestMethod("GET");
			connection.setUseCaches(useCaches);
			connection.addRequestProperty("User-Agent", userAgent);
			connection.setRequestProperty("Accept-Language", "en-US");
			connection.setRequestProperty("Accept-Charset", "UTF-8");
			connection.setReadTimeout(timeout);
			connection.setConnectTimeout(timeout);

			return connection;
		} catch (Exception e) {
			EdenLogger.error("HTTP", "Failed to open a connection to " + url, e);
			return null;
		}
	}
}
