package mbd.teacher.gurukuteacher.model.student;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Naufal on 20/02/2018.
 */

public class StudentReqeustResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private List<DataRequest> data = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<DataRequest> getData() {
        return data;
    }

    public void setData(List<DataRequest> data) {
        this.data = data;
    }
}
