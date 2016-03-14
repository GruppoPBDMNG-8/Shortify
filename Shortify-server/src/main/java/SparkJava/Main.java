package SparkJava;

import static Logic.Services.redirectURL;
import static spark.Spark.*;
import static spark.Spark.options;

import Exceptions.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import Entity.Click;
import Logic.Services;

import java.util.Objects;


/**
 * Created by Giuseppe on 24/02/2016.
 */
public class Main {
    //public RedisDAO redisDAO = new RedisDAO();

    public static void main(String[] args) {

        setRoutes();

        //Jedis jedis = new Jedis("192.168.1.107",32769);

      //  get("/hello", (req, res) -> "Hello World");
       // get("/", (req, res) -> jedis.ping());
       // post("inserturl", (req, res) -> jedis.set(req.body(),req.body()));
        //get("/:shorturl", (req, res) -> jedis.get(req.params(":shorturl")));
        /*get("/re/:shorturl", (req, res) -> {
            String url = jedis.get(req.params(":shorturl"));
            System.out.println(url);
            res.body("done");
            res.redirect("http://www.google.com");
            return null;
        });*/
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
            }
            finally{System.out.println("------");}
            return response;




            /*Gson gson = new Gson();
            JsonObject jsonRequest = gson.fromJson(req.body(), JsonObject.class);
            String longUrl = jsonRequest.get("longURL").getAsString();
            String custom = jsonRequest.get("customURL").getAsString();
            System.out.println(longUrl + " "  + custom);
            JsonObject jsonResponse = new JsonObject();



            if (Objects.equals(longUrl, "getError")){
                jsonResponse.addProperty("error", "this is an error message");
                res.status(500);
                System.out.println("sending error");
            }
            else{
                jsonResponse.addProperty("long", longUrl);
                jsonResponse.addProperty("cust", custom);
                System.out.println("sending normal response");
            }
            return jsonResponse;*/
        });

    }

    private static void redirect_route(){
        get(":shorturl", (req, res) -> {
            JsonObject error = new JsonObject();
            String response = new String();
            System.out.println("Redirect request(" + req.params("shorturl") + ") from: " + req.ip());

            try{
                response = redirectURL(req.params("shorturl"), req.ip(), req.userAgent());
                System.out.println("...operation successful." + response);
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
