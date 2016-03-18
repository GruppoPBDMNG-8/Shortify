package dao;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Jedis;

import java.util.List;

import static java.lang.Math.toIntExact;

/**
 * Created by Giuseppe on 24/02/2016.
 */
public class RedisDAO {

        private static final String DB_NAME = "192.168.1.108";
        private static final int DB_PORT = 32768;
        //private static final String clicks = ":clicks";
        private static final JedisPool pool = new JedisPool(new JedisPoolConfig(), DB_NAME, DB_PORT);

        public static String getString(String key){
            String longURL;
            try (Jedis jedis = pool.getResource()) {
                longURL = jedis.get(key);
            }
            return longURL;
        }

         public static Integer setString(String key, String value){
                 long inserted;
            try (Jedis jedis = pool.getResource()) {
                inserted = jedis.setnx(key, value);
            }
            return toIntExact(inserted);
        }

        public static void pushList(String key, String value){
            try (Jedis jedis = pool.getResource()) {
                jedis.rpush(key, value);
            }
        }

        public static List<String> getList(String key){
            List<String> clicksList;

            try (Jedis jedis = pool.getResource()) {
                clicksList = jedis.lrange(key, 0,-1);
            }
            return clicksList;
        }

        public static boolean exists(String shortURL){
            boolean exists;
            try (Jedis jedis = pool.getResource()) {
                exists = jedis.exists(shortURL);
            }
            return exists;
        }


    public static Integer deleteKey(String key){
        long deleted;
        try (Jedis jedis = pool.getResource()) {
            deleted = jedis.del(key);
        }
        return toIntExact(deleted);
    }

}
