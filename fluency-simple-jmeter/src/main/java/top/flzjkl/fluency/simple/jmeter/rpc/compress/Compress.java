package top.flzjkl.fluency.simple.jmeter.rpc.compress;



import top.flzjkl.fluency.simple.jmeter.rpc.common.extension.SPI;


@SPI
public interface Compress {

    byte[] compress(byte[] bytes);

    byte[] decompress(byte[] bytes);
}
