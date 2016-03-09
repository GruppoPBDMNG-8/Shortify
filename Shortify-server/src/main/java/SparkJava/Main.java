package SparkJava;

import static spark.Spark.*;
import static spark.Spark.options;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import Entity.Click;

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
        post("/inserturl", (req, res) -> /*Services.setShortURL(req.body())*/
        {
            Click click = new Click(req.ip(), req.userAgent());
            System.out.println(click.toString());
            System.out.println(req.userAgent());

            Gson gson = new Gson();
            String jsonString = gson.toJson(click);

            return jsonString;




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
            //res.redirect(Services.redirectURL(req.params(":shorturl")));
            // String par = req.params("shorturl");
            //JsonObject jsonResponse = new JsonObject();
            //jsonResponse.addProperty("redirectURL", "http://www.google.it");
            //System.out.println("getting" + " " + par + jsonResponse.toString());
            res.redirect("http://www.google.com");
            //return jsonResponse;
            return "";
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
