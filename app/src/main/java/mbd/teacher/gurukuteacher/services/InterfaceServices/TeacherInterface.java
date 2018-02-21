package mbd.teacher.gurukuteacher.services.InterfaceServices;

import org.json.JSONObject;

import mbd.teacher.gurukuteacher.model.student.StudentReqeustResponse;
import mbd.teacher.gurukuteacher.model.teacher.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Naufal on 19/02/2018.
 */

public interface TeacherInterface {
    @POST("/teacher/register")
    Call<String> APIRegister(@Body JSONObject param);

    @POST("/teacher/login")
    Call<LoginResponse> APILogin(@Body JSONObject param);

    @GET("/teacher/getStudentRequest/{teacherID}")
    Call<StudentReqeustResponse> APIGetStudentReqeuest(@Path("teacherID") int teacherID);
}
