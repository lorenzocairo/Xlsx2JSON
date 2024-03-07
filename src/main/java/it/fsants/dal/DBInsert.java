package it.fsants.dal;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.Statement;

public class DBInsert {

    /**
     * Insert into SUPABASE using insertObservations functions
     * obs: {
     *     date: date
     *     observations: {
     *         specie: specie,
     *         nomeScientifico: scName,
     *         numero: num
     *     }
     * }
     * @param obs
     * @throws DBException
     */
    public static void addMonthObservations(JSONObject obs) throws DBException {
        try (Connection connection = DBConnection.connect();
             Statement stm = connection.createStatement();) {
            String s = obs.toString().replace("'", "''");
            final String query = String.format("SELECT public.\"insertObservations\"('%s')", s);
            stm.execute(query);
        } catch (Exception e) {
            throw new DBException(String.format("Cannot insert observations: %s", e.getMessage()));
        }
    }
}
