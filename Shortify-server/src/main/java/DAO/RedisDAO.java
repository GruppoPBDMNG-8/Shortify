package DAO;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Jedis;

import static java.lang.Math.toIntExact;

/**
 * Created by Giuseppe on 24/02/2016.
 */
public class RedisDAO {

        private static final String DB_NAME = "192.168.1.107";
        private static final int DB_PORT = 32768;
        private static final JedisPool pool = new JedisPool(new JedisPoolConfig(), DB_NAME, DB_PORT);

        public static String getLongURL(String shortURL){
            String longURL;
            try (Jedis jedis = pool.getResource()) {
                longURL = jedis.get(shortURL);
            }
            return longURL;
        }

    public static int setShortURL(String shortURL, String longURL){
        long inserted;
        try (Jedis jedis = pool.getResource()) {
            inserted = jedis.setnx(shortURL, longURL);
        }
        return toIntExact(inserted);
    }

}
