package top.flzjkl.fluency.simple.jmeter.rpc.registry.redis;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import top.flzjkl.fluency.simple.jmeter.rpc.common.factory.SingletonFactory;
import top.flzjkl.fluency.simple.jmeter.rpc.registry.ServiceDiscovery;
import top.flzjkl.fluency.simple.jmeter.rpc.remoting.dto.RpcRequest;


import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
public class RedisServiceDiscovery implements ServiceDiscovery {

    private final String REDIS_ROOT_PATH = "fluency:simple:";

    private Jedis jedis;

    private String host;
    private int port;

    private static JedisPool jedisPool;
    private static JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

    static {
        jedisPoolConfig.setMaxTotal(10);
        jedisPool = new JedisPool(jedisPoolConfig, "192.168.200.130");
    }

    @Override
    public InetSocketAddress lookupService(RpcRequest rpcRequest) {
        String key =  REDIS_ROOT_PATH + rpcRequest.getRpcServiceName();
        /* if (lookupServiceMap.containsKey(key)) {
            System.out.println("mingzhong");
            return lookupServiceMap.get(key);
        } else {
            try {
                // jedis = jedisPool.getResource();
                // String key = "fluency:simple:top.flzjkl.fluency.api.HelloServicetest1version1"; */
        try {
            jedis = jedisPool.getResource();
            String value = jedis.get(key);
            String[] split = value.split(":");
            host = split[0];
            port = Integer.parseInt(split[1]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            jedis.close();
        }
        // lookupServiceMap.put(key, new InetSocketAddress(host, port));
            /*} catch (NumberFormatException e) {
                throw new RuntimeException(e);
            } finally {
                jedis.close();
            }
        }*/

        return new InetSocketAddress(host, port);
    }
}
