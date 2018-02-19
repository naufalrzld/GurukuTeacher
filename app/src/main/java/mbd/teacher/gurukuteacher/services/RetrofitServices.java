package mbd.teacher.gurukuteacher.services;


import mbd.teacher.gurukuteacher.services.InterfaceServices.TeacherInterface;

/**
 * Created by Naufal on 10/02/2018.
 */

public class RetrofitServices {
    public static TeacherInterface sendTeacherRequest() {
        return RetrofitBaseServices.getApiClient().create(TeacherInterface.class);
    }
}
