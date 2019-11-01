package chapter3.conversion_problems;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.HttpMessageConverterExtractor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
public class MyController {

    public static void main(String[] args) {
        List<Integer> nums =  Arrays.stream("123456".split("")).map(Integer::parseInt).collect(Collectors.toList());

        IntStream.range(0,nums.size()).map(i -> value(i,nums)).sum();
    }

    private static Integer value(Integer i, List<Integer> nums){
        if(i % 2 ==0){
            int value = nums.get(i) * 2;
            return value < 10? value : value%10 + 1;
        }
        return nums.get(i);
    }
    private final List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
    {
        this.messageConverters.add(new ByteArrayHttpMessageConverter());
        this.messageConverters.add(new StringHttpMessageConverter());
        this.messageConverters.add(new MappingJackson2HttpMessageConverter());
    }

    @RequestMapping(value = "/future",produces = MediaType.TEXT_PLAIN_VALUE,method = RequestMethod.GET)
    public ListenableFuture<?> requestData(){
        AsyncRestTemplate httpClient = new AsyncRestTemplate();
        AsyncDatabaseClient databaseClient = new FakeAsyncDatabaseClient();
        CompletionStage<String> completionStage = AsyncAdapters.toCompletion(
                httpClient.execute("http://localhost:8080/hello",
                        HttpMethod.GET,
                        null,
                        new HttpMessageConverterExtractor<>(String.class,messageConverters))
        );
        return AsyncAdapters.toListenable(databaseClient.store(completionStage));
    }
}
