package dao;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Jedis;

import java.util.List;

import static java.lang.Math.toIntExact;

/**
 * Functions to connect the server to a Redis server through Jedis
 * Created by Giuseppe Perniola on 24/02/2016.
 */
public class RedisDAO {

        private static final String DB_NAME = "192.168.1.108";
        private static final int DB_PORT = 32768;
        private static final JedisPool pool = new JedisPool(new JedisPoolConfig(), DB_NAME, DB_PORT);


    /**
     * Given a key, returns the value associated in the db.
     * @param key a String containing the key to search in the db.
     * @return a String containing the value associated to the key.
     */
        public static String getString(String key){
            String longURL;
            try (Jedis jedis = pool.getResource()) {
                longURL = jedis.get(key);
            }
            return longURL;
        }

    /**
     * Sets a key and the value associated if it doesnt already exists.
     * @param key the key to memorize in the db.
     * @param value the value to memorize in the db.
     * @return returns 1 if the key was inserted, 0 if not.
     */
         public static Integer setString(String key, String value){
                 long inserted;
            try (Jedis jedis = pool.getResource()) {
                inserted = jedis.setnx(key, value);
            }
            return toIntExact(inserted);
        }

    /**
     * Given a key and a value, push the new value in the list associated to the key
     * @param key the key to search in the db..
     * @param value the value to push in the list.
     */
        public static void pushList(String key, String value){
            try (Jedis jedis = pool.getResource()) {
                jedis.rpush(key, value);
            }
        }

    /**
     * Given a key returns the list associated.
     * @param key the key to search in the db.
     * @return the list associated to the key.
     */
        public static List<String> getList(String key){
            List<String> clicksList;

            try (Jedis jedis = pool.getResource()) {
                clicksList = jedis.lrange(key, 0,-1);
            }
            return clicksList;
        }

    /**
     * Given a key, checks if it exists or not in the db.
     * @param key the key to search in the db.
     * @return true if the key exists, false otherwise
     */
        public static boolean exists(String key){
            boolean exists;
            try (Jedis jedis = pool.getResource()) {
                exists = jedis.exists(key);
            }
            return exists;
        }


    /**
     * Deletes the key and the value associated if it exists in the db.
     * @param key the key to search in the db.
     * @return the number of records deleted in the db.
     */
    public static Integer deleteKey(String key){
        long deleted;
        try (Jedis jedis = pool.getResource()) {
            deleted = jedis.del(key);
        }
        return toIntExact(deleted);
    }

}
