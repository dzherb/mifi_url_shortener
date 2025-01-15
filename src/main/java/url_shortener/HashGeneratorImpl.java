package main.java.url_shortener;

public class HashGeneratorImpl implements HashGenerator {
    private Integer counter = 0;

    @Override
    public String generate(String url) {
        counter++;
        // Return hexadecimal representation
        return String.format("%X ", counter);
    }
}
