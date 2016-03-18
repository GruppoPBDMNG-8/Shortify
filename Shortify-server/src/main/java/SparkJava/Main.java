package sparkjava;

import static logic.Services.redirectURL;
import static spark.Spark.*;
import static spark.Spark.options;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import exceptions.*;
import utilities.Populator;
import logic.Services;


/**
 * Created by Giuseppe on 24/02/2016.
 */
public class Main {

    public static void main(String[] args) {
        new Populator(15,1000);
        setRoutes();
    }

    private static void setRoutes(){
        enableCORS();
        createShortURL_route();
        redirect_route();
    }

    private static void createShortURL_route(){
        post("/inserturl", (req, res) ->
        {
            String response = new String();
            JsonObject error = new JsonObject();
            JsonObject jsonRequest = new Gson().fromJson(req.body(),JsonObject.class);
            System.out.println("createURL request(" + jsonRequest.get("longURL").getAsString() +
                    ", " + jsonRequest.get("customURL").getAsString() + ") from: " + req.ip());
            try{
                response = Services.setShortURL(jsonRequest.get("longURL").getAsString(),
                        jsonRequest.get("customURL").getAsString(),
                        req.ip(),req.userAgent());

                System.out.println("...operation successful.");
            }catch(CustomUrlUnavailableException e){
                System.out.println("ERROR(" + jsonRequest.get("longURL").getAsString() + ") " + e.getClass());
                error.addProperty("error", e.getMessage());
                res.status(500);
                response = new Gson().toJson(error);
            }catch(MaxAttemptsException e){
                System.out.println("ERROR(" + jsonRequest.get("longURL").getAsString() + ") " + e.getClass());
                error.addProperty("error", e.getMessage());
                res.status(500);
                response = new Gson().toJson(error);
            }catch (BlankUrlException e){
                System.out.println("ERROR(" + jsonRequest.get("longURL").getAsString() + ") " + e.getClass());
                error.addProperty("error", e.getMessage());
                res.status(500);
                response = new Gson().toJson(error);
            }catch(BadWordException e){
                System.out.println("ERROR(" + jsonRequest.get("longURL").getAsString() + ") " + e.getClass());
                error.addProperty("error", e.getMessage());
                res.status(500);
                response = new Gson().toJson(error);
            }
            finally{System.out.println("------");}
            return response;
        });

    }

    private static void redirect_route(){
        get(":shorturl", (req, res) -> {
            JsonObject error = new JsonObject();
            String response = new String();
            System.out.println("Redirect request(" + req.params("shorturl") + ") from: " + req.ip());

            try{
                response = redirectURL(req.params("shorturl"), req.ip(), req.userAgent());
                System.out.println("...operation successful.");
            }catch(UrlNotPresentException e){
                System.out.println("ERROR(" + req.params("shorturl") + ") " + e.getClass());
                error.addProperty("error", e.getMessage());
                res.status(500);
                response = new Gson().toJson(error);
            }
            catch(EmptyClicksException e){
                System.out.println("ERROR(" + req.params("shorturl") + ") " + e.getClass());
                error.addProperty("error", e.getMessage());
                res.status(500);
                response = new Gson().toJson(error);
            }
            finally{System.out.println("------");}
            return response;
        });

    }

    private static void enableCORS(){
        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request
                    .headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers",
                        accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request
                    .headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods",
                        accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
        });


    }


}
