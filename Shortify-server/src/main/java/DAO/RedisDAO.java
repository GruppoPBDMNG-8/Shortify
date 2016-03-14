package DAO;

import Entity.Click;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toIntExact;

/**
 * Created by Giuseppe on 24/02/2016.
 */
public class RedisDAO {

        private static final String DB_NAME = "192.168.1.108";
        private static final int DB_PORT = 32768;
        private static final String clicks = ":clicks";
        private static final JedisPool pool = new JedisPool(new JedisPoolConfig(), DB_NAME, DB_PORT);

        public static String getLongURL(String shortURL){
            String longURL;
            try (Jedis jedis = pool.getResource()) {
                longURL = jedis.get(shortURL);
            }
            return longURL;
        }

         public static Integer setShortURL(String shortURL, String longURL){
            long inserted;
            try (Jedis jedis = pool.getResource()) {
                inserted = jedis.setnx(shortURL, longURL);
            }
            return toIntExact(inserted);
        }

        public static void setClick(String shortURL, String jsonClick){
            try (Jedis jedis = pool.getResource()) {
                jedis.rpush(shortURL + clicks, jsonClick);
            }
        }

        public static List<String> getAllClicks(String shortURL){
            List<String> jsonClicks;

            try (Jedis jedis = pool.getResource()) {
                jsonClicks = jedis.lrange(shortURL + clicks, 0,-1);
            }
            return jsonClicks;
        }

        public static boolean exists(String shortURL){
            boolean exists;
            try (Jedis jedis = pool.getResource()) {
                exists = jedis.exists(shortURL);
            }
            return exists;
        }


}
