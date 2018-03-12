package mbd.teacher.gurukuteacher.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mbd.teacher.gurukuteacher.R;
import mbd.teacher.gurukuteacher.adapter.StudentAdapter;
import mbd.teacher.gurukuteacher.model.student.DataRequest;
import mbd.teacher.gurukuteacher.model.student.Student;
import mbd.teacher.gurukuteacher.model.student.StudentReqeustResponse;
import mbd.teacher.gurukuteacher.model.teacher.Teacher;
import mbd.teacher.gurukuteacher.services.RetrofitServices;
import mbd.teacher.gurukuteacher.utils.SharedPreferencesUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvStudent)
    RecyclerView rvStudent;
    @BindView(R.id.tvNoData)
    TextView tvNoData;

    private List<DataRequest> dataRequestList = new ArrayList<>();
    private StudentAdapter adapter;

    private SharedPreferencesUtils sharedPreferencesUtils;

    private int teacherID;

    public StudentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_student, container, false);
        ButterKnife.bind(this, v);

        swipeRefreshLayout.setOnRefreshListener(this);

        sharedPreferencesUtils = new SharedPreferencesUtils(getContext(), "DataMember");
        if (sharedPreferencesUtils.checkIfDataExists("profile")) {
            Teacher teacher = sharedPreferencesUtils.getObjectData("profile", Teacher.class);
            teacherID = teacher.getTeacherID();
        }

        adapter = new StudentAdapter(getContext(), dataRequestList);
        rvStudent.setHasFixedSize(true);
        rvStudent.setLayoutManager(new LinearLayoutManager(getContext()));
        rvStudent.setAdapter(adapter);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getStudentRequest(teacherID);
    }

    private void getStudentRequest(int teacherID) {
        swipeRefreshLayout.setRefreshing(true);
        dataRequestList.clear();
        Call<StudentReqeustResponse> call = RetrofitServices.sendTeacherRequest().APIGetStudentReqeuest(teacherID);
        if (call != null) {
            call.enqueue(new Callback<StudentReqeustResponse>() {
                @Override
                public void onResponse(@NonNull Call<StudentReqeustResponse> call, @NonNull Response<StudentReqeustResponse> response) {
                    swipeRefreshLayout.setRefreshing(false);
                    if (response.isSuccessful()) {
                        int count = response.body().getData().size();

                        for (int i=0; i<count; i++) {
                            DataRequest dataRequest = response.body().getData().get(i);
                            Student student = dataRequest.getStudent();

                            int bookID = dataRequest.getBookID();
                            int status = dataRequest.getStatus();
                            int duration = dataRequest.getDuration();
                            String location = dataRequest.getLocation();
                            String date = dataRequest.getDate();
                            String time = dataRequest.getTime();

                            int studentID = student.getStudentID();
                            String username = student.getUsername();
                            String fName = student.getFirstName();
                            String lName = student.getLastName();
                            String email = student.getEmail();
                            String noTlp = student.getNoTlp();
                            String lineAccount = student.getLineAccount();
                            String noWA = student.getNoWA();
                            String igAccount = student.getIgAccount();
                            String otherAccount = student.getOtherAccount();

                            dataRequestList.add(new DataRequest(bookID, status, duration, location, date, time,
                                    new Student(studentID, username, fName, lName, email, noTlp, lineAccount,
                                            noWA, igAccount, otherAccount)));
                        }

                        if (dataRequestList.isEmpty()) {
                            tvNoData.setVisibility(View.VISIBLE);
                        } else {
                            tvNoData.setVisibility(View.GONE);
                        }

                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<StudentReqeustResponse> call, @NonNull Throwable t) {
                    swipeRefreshLayout.setRefreshing(false);
                    Log.d("error", t.getMessage());
                }
            });
        }
    }

    @Override
    public void onRefresh() {
        getStudentRequest(teacherID);
    }
}
