package SparkJava;

import static spark.Spark.*;
import Logic.Services;


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
        createShortURL_route();
        redirect_route();
    }

    private static void createShortURL_route(){
        post("/inserturl", (req, res) -> Services.setShortURL(req.body()));

    }

    private static void redirect_route(){
        get(":shorturl", (req, res) -> {
            res.redirect(Services.redirectURL(req.params(":shorturl")));
            return null;
        });

    }

}
