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
import mbd.teacher.gurukuteacher.adapter.TransactionAdapter;
import mbd.teacher.gurukuteacher.model.student.Student;
import mbd.teacher.gurukuteacher.model.teacher.Teacher;
import mbd.teacher.gurukuteacher.model.transaction.Data;
import mbd.teacher.gurukuteacher.model.transaction.Transaction;
import mbd.teacher.gurukuteacher.model.transaction.TransactionResponse;
import mbd.teacher.gurukuteacher.services.RetrofitServices;
import mbd.teacher.gurukuteacher.utils.SharedPreferencesUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvStudent)
    RecyclerView rvStudent;
    @BindView(R.id.tvNoData)
    TextView tvNoData;

    private List<Data> listData = new ArrayList<>();
    private TransactionAdapter adapter;

    private SharedPreferencesUtils sharedPreferencesUtils;

    private int teacherID;

    public TransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_transaction, container, false);
        ButterKnife.bind(this, v);

        swipeRefreshLayout.setOnRefreshListener(this);

        sharedPreferencesUtils = new SharedPreferencesUtils(getContext(), "DataMember");
        if (sharedPreferencesUtils.checkIfDataExists("profile")) {
            Teacher teacher = sharedPreferencesUtils.getObjectData("profile", Teacher.class);
            teacherID = teacher.getTeacherID();
        }

        adapter = new TransactionAdapter(getContext(), listData);
        rvStudent.setHasFixedSize(true);
        rvStudent.setLayoutManager(new LinearLayoutManager(getContext()));
        rvStudent.setAdapter(adapter);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTransaction(teacherID);
    }

    @Override
    public void onRefresh() {
        getTransaction(teacherID);
    }

    private void getTransaction(int teacherID) {
        swipeRefreshLayout.setRefreshing(true);
        listData.clear();
        Call<TransactionResponse> call = RetrofitServices.sendTeacherRequest().APIGetTransaction(teacherID);
        if (call != null) {
            call.enqueue(new Callback<TransactionResponse>() {
                @Override
                public void onResponse(@NonNull Call<TransactionResponse> call, @NonNull Response<TransactionResponse> response) {
                    swipeRefreshLayout.setRefreshing(false);
                    if (response.isSuccessful()) {
                        int count = response.body().getData().size();

                        for (int i=0; i<count; i++) {
                            Data data = response.body().getData().get(i);
                            Student student = data.getStudent();
                            Transaction transaction = data.getTransaction();

                            int bookID = data.getBookID();
                            int status = data.getStatus();
                            int duration = data.getDuration();

                            int trasactionID = transaction.getTransactioID();
                            int statusTrx = transaction.getStatus();
                            String paymentMethod = transaction.getPaymentMethod();
                            int totalPrice = transaction.getTotalPrice();

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

                            listData.add(new Data(bookID, status, duration,
                                    new Student(studentID, username, fName, lName, email, noTlp,
                                            lineAccount, noWA, igAccount, otherAccount),
                                    new Transaction(trasactionID, bookID, statusTrx, paymentMethod, totalPrice)));
                        }

                        if (listData.isEmpty()) {
                            tvNoData.setVisibility(View.VISIBLE);
                        } else {
                            tvNoData.setVisibility(View.GONE);
                        }

                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<TransactionResponse> call, @NonNull Throwable t) {
                    swipeRefreshLayout.setRefreshing(false);
                    Log.d("error", t.getMessage());
                }
            });
        }
    }
}
