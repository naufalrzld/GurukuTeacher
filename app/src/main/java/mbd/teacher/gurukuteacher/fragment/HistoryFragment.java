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
import mbd.teacher.gurukuteacher.adapter.HistoryAdapter;
import mbd.teacher.gurukuteacher.model.history.History;
import mbd.teacher.gurukuteacher.model.history.HistoryResponse;
import mbd.teacher.gurukuteacher.model.student.Student;
import mbd.teacher.gurukuteacher.model.teacher.Teacher;
import mbd.teacher.gurukuteacher.services.RetrofitServices;
import mbd.teacher.gurukuteacher.utils.SharedPreferencesUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvHistory)
    RecyclerView rvHistory;
    @BindView(R.id.tvNoHistory)
    TextView tvNoHistory;

    private SharedPreferencesUtils sharedPreferencesUtils;
    private HistoryAdapter adapter;
    private List<History> historyList = new ArrayList<>();

    private int teacherID = 0;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, v);

        swipeRefreshLayout.setOnRefreshListener(this);

        sharedPreferencesUtils = new SharedPreferencesUtils(getContext(), "DataMember");
        if (sharedPreferencesUtils.checkIfDataExists("profile")) {
            Teacher teacher = sharedPreferencesUtils.getObjectData("profile", Teacher.class);
            teacherID = teacher.getTeacherID();
        }

        adapter = new HistoryAdapter(getContext(), historyList);
        rvHistory.setHasFixedSize(true);
        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        rvHistory.setAdapter(adapter);

        getBookingHistory(teacherID);
        return v;
    }

    @Override
    public void onRefresh() {
        getBookingHistory(teacherID);
    }

    private void getBookingHistory(int teacherID) {
        swipeRefreshLayout.setRefreshing(true);
        historyList.clear();
        Call<HistoryResponse> call = RetrofitServices.sendTeacherRequest().APIGetBookingHistory(teacherID);
        if (call != null) {
            call.enqueue(new Callback<HistoryResponse>() {
                @Override
                public void onResponse(@NonNull Call<HistoryResponse> call, @NonNull Response<HistoryResponse> response) {
                    swipeRefreshLayout.setRefreshing(false);
                    if (response.isSuccessful()) {
                        int count = response.body().getHistory().size();

                        for (int i=0; i<count; i++) {
                            History history = response.body().getHistory().get(i);
                            int bookID = history.getBookID();
                            int studentID = history.getStudentID();
                            int status = history.getStatus();
                            int finish = history.getFinish();
                            String fNameMurid = history.getStudent().getFirstName();
                            String lNameMurid = history.getStudent().getLastName();

                            historyList.add(new History(bookID, status, finish, new Student(studentID, fNameMurid, lNameMurid)));
                        }

                        if (historyList.isEmpty()) {
                            tvNoHistory.setVisibility(View.VISIBLE);
                        } else {
                            tvNoHistory.setVisibility(View.GONE);
                        }

                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<HistoryResponse> call, @NonNull Throwable t) {
                    swipeRefreshLayout.setRefreshing(false);
                    Log.e("error", t.getMessage());
                }
            });
        }
    }
}
