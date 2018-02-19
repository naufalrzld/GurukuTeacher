package mbd.teacher.gurukuteacher.utils;

import java.io.IOException;
import java.lang.annotation.Annotation;

import mbd.teacher.gurukuteacher.model.APIErrorModel;
import mbd.teacher.gurukuteacher.services.RetrofitBaseServices;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by Naufal on 10/02/2018.
 */

public class APIErrorUtils {
    public static APIErrorModel parserError(Response<?> response) {
        Converter<ResponseBody, APIErrorModel> converter = RetrofitBaseServices.getApiClient()
                .responseBodyConverter(APIErrorModel.class, new Annotation[0]);

        APIErrorModel error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new APIErrorModel();
        }

        return error;
    }
}
