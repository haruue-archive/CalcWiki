package org.calcwiki.data.network.converter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * 直接让 Retrofit 返回字符串的转换工厂
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class StringConverterFactory extends Converter.Factory {

    public static StringConverterFactory create() {
        return new StringConverterFactory();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type != String.class) {
            return null;
        }
        return new Converter<ResponseBody, String>() {
            @Override
            public String convert(ResponseBody value) throws IOException {
                try {
                    return value.string();
                } finally {
                    value.close();
                }
            }
        };
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        if (type != String.class) {
            return null;
        }
        return new Converter<String, RequestBody>() {
            @Override
            public RequestBody convert(String value) throws IOException {
                return RequestBody.create(MediaType.parse("text/plain"), value.getBytes(Charset.forName("UTF-8")));
            }
        };
    }

}
