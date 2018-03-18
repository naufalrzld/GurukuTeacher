package mbd.teacher.gurukuteacher.services.InterfaceServices;

import org.json.JSONObject;

import mbd.teacher.gurukuteacher.model.history.HistoryResponse;
import mbd.teacher.gurukuteacher.model.student.StudentReqeustResponse;
import mbd.teacher.gurukuteacher.model.teacher.CategoryResponse;
import mbd.teacher.gurukuteacher.model.teacher.LoginResponse;
import mbd.teacher.gurukuteacher.model.teacher.UpdateProfileResponse;
import mbd.teacher.gurukuteacher.model.transaction.TransactionResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Naufal on 19/02/2018.
 */

public interface TeacherInterface {
    @GET("teacher/getCategory")
    Call<CategoryResponse> APIGetAllCategory();

    @POST("/teacher/register")
    Call<String> APIRegister(@Body JSONObject param);

    @POST("/teacher/editProfile")
    Call<UpdateProfileResponse> APIEditProfile(@Body JSONObject param);

    @POST("/teacher/login")
    Call<LoginResponse> APILogin(@Body JSONObject param);

    @GET("/teacher/getStudentRequest/{teacherID}")
    Call<StudentReqeustResponse> APIGetStudentReqeuest(@Path("teacherID") int teacherID);

    @POST("/teacher/acceptRequest")
    Call<String> APIAcceptRequest(@Body JSONObject param);

    @GET("/teacher/getBookingHistory/{teacherID}")
    Call<HistoryResponse> APIGetBookingHistory(@Path("teacherID") int teacherID);

    @GET("/teacher/transaction/{teacherID}")
    Call<TransactionResponse> APIGetTransaction(@Path("teacherID") int teacherID);
}
