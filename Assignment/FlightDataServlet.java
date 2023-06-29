import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class FlightDataServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String flightTiming = request.getParameter("flightTiming");
        String flightName = request.getParameter("flightName");
        String stopType = request.getParameter("stopType");

        String urlString = "https://partner.imwallet.in/web_services/statudentFilter.jsp";
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder responseBuilder = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                responseBuilder.append(inputLine);
            }
            in.close();

            JSONArray flightDataArray = new JSONArray(responseBuilder.toString());
            List<Flight> flights = parseFlightData(flightDataArray);

            List<Flight> filteredFlights = filterFlights(flights, flightTiming, flightName, stopType);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(filteredFlights.toString());
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private List<Flight> parseFlightData(JSONArray flightDataArray) {
        List<Flight> flights = new ArrayList<>();

        for (int i = 0; i < flightDataArray.length(); i++) {
            JSONObject flightData = flightDataArray.getJSONObject(i);

            String flightTime = flightData.getString("flight_time");
            double price = flightData.getDouble("price");
            String flightName = flightData.getString("flight_name");
            String stopType = flightData.getString("stop");

            Flight flight = new Flight(flightTime, price, flightName, stopType);
            flights.add(flight);
        }

        return flights;
    }

    private List<Flight> filterFlights(List<Flight> flights, String flightTiming, String flightName, String stopType) {
        List<Flight> filteredFlights = new ArrayList<>();

        for (Flight flight : flights) {
            boolean isFlightTimingMatched = flightTiming.isEmpty() || flight.getFlightTime().equalsIgnoreCase(flightTiming);
            boolean isFlightNameMatched = flightName.isEmpty() || flight.getFlightName().toLowerCase().contains(flightName.toLowerCase());
            boolean isStopTypeMatched = stopType.isEmpty() || flight.getStopType().equalsIgnoreCase(stopType);

            if (isFlightTimingMatched && isFlightNameMatched && isStopTypeMatched) {
                filteredFlights.add(flight);
            }
        }

        return filteredFlights;
    }
}
