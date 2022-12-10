package top.flzjkl.fluency.simple.compress;


import top.flzjkl.fluency.simple.common.extension.SPI;



@SPI
public interface Compress {

    byte[] compress(byte[] bytes);

    byte[] decompress(byte[] bytes);
}
