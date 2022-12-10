package top.flzjkl.fluency.simple.registry.redis;

import redis.clients.jedis.Jedis;
import top.flzjkl.fluency.simple.registry.ServiceRegistry;

import java.net.InetSocketAddress;

public class RedisServiceRegistry implements ServiceRegistry {


    private final String REDIS_ROOT_PATH = "fluency:simple:";

    @Override
    public void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        Jedis jedis = new Jedis("192.168.200.130");
        String key = REDIS_ROOT_PATH + rpcServiceName ;
        String value = inetSocketAddress.toString().substring(1);
        jedis.set(key, value);
        System.out.println("注册Key为:" + key);
        jedis.close();
    }
}
