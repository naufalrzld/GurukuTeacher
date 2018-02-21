package mbd.teacher.gurukuteacher.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import mbd.teacher.gurukuteacher.R;
import mbd.teacher.gurukuteacher.model.student.DataRequest;
import mbd.teacher.gurukuteacher.model.student.Student;
import mbd.teacher.gurukuteacher.model.teacher.Teacher;
import mbd.teacher.gurukuteacher.model.transaction.Data;
import mbd.teacher.gurukuteacher.services.RetrofitServices;
import mbd.teacher.gurukuteacher.utils.SharedPreferencesUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailStudentActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvDuration)
    TextView tvDuration;
    @BindView(R.id.profile_image)
    ImageView profileImage;
    @BindView(R.id.tvNama)
    TextView tvNama;
    @BindView(R.id.tvstatus)
    TextView tvStatus;

    @BindView(R.id.cvPaymentStatus)
    CardView cvPaymentStatus;
    @BindView(R.id.tvPaymentStatus)
    TextView tvPaymentStatus;

    @BindView(R.id.tvUsername)
    TextView tvUsername;
    @BindView(R.id.tvNoTlp)
    TextView tvNoTlp;
    @BindView(R.id.tvEmail)
    TextView tvEmail;

    @BindView(R.id.tvNoWa)
    TextView tvNoWa;
    @BindView(R.id.tvLineAccount)
    TextView tvLineAccount;

    @BindView(R.id.lytConfirmation)
    LinearLayout lytConfirmation;
    @BindView(R.id.btnAccept)
    Button btnAccept;
    @BindView(R.id.btnReject)
    Button btnReject;

    private ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
    private TextDrawable mDrawableBuilder;

    private SharedPreferencesUtils sharedPreferencesUtils;
    private Intent dataIntent;

    private int price;

    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_student);
        ButterKnife.bind(this);

        loading = new ProgressDialog(this);
        loading.setMessage("Loading . . .");
        loading.setCancelable(false);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.activity_title_detail_student);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        sharedPreferencesUtils = new SharedPreferencesUtils(getApplicationContext(), "DataMember");
        if (sharedPreferencesUtils.checkIfDataExists("profile")) {
            Teacher teacher = sharedPreferencesUtils.getObjectData("profile", Teacher.class);
            price = teacher.getPrice();
        }

        dataIntent = getIntent();

        String from = dataIntent.getStringExtra("from");
        Student student;
        final int bookID;
        int status, statusTrx = 0;
        final int duration;
        if (from.equals("Stdn")) {
            DataRequest dataRequest = new Gson().fromJson(dataIntent.getStringExtra("bookData"), DataRequest.class);
            student = dataRequest.getStudent();
            bookID = dataRequest.getBookID();
            status = dataRequest.getStatus();
            duration = dataRequest.getDuration();
        } else {
            Data data = new Gson().fromJson(dataIntent.getStringExtra("bookData"), Data.class);
            student = data.getStudent();
            bookID = data.getBookID();
            status = data.getStatus();
            duration = data.getDuration();
            statusTrx = data.getTransaction().getStatus();
        }

        String username = student.getUsername();
        String nama = student.getFirstName() + " " + student.getLastName();
        String email = student.getEmail();
        String noTlp = student.getNoTlp();
        String lineAccount = student.getLineAccount();
        String noWA = student.getNoWA();
        String igAccount = student.getIgAccount();
        String otherAccount = student.getOtherAccount();

        String information;
        String infoPayment;
        if (status == 0) {
            information = "Menunggu konfirmasi anda";
            lytConfirmation.setVisibility(View.VISIBLE);
        } else {
            information = "Sudah dikonfirmasi";
            lytConfirmation.setVisibility(View.GONE);
            cvPaymentStatus.setVisibility(View.VISIBLE);
        }

        if (statusTrx == 0) {
            infoPayment = "Belum dibayar";
        } else {
            infoPayment = "Sudah dibayar";
        }

        tvNama.setText(nama);
        tvStatus.setText(information);
        tvPaymentStatus.setText(infoPayment);
        tvDuration.setText(String.valueOf(duration));
        tvUsername.setText(username);
        tvNoTlp.setText(noTlp);
        tvEmail.setText(email);
        tvLineAccount.setText(lineAccount);
        tvNoWa.setText(noWA);

        setProfileImage(nama);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject param = new JSONObject();
                try {
                    param.put("bookID", bookID);
                    param.put("status", 1);
                    param.put("total_price", price*duration);

                    acceptRequest(param);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setProfileImage(String nama) {
        String letter = "A";

        if(nama != null && !nama.isEmpty()) {
            letter = nama.substring(0, 1);
        }

        int color = mColorGenerator.getRandomColor();
        mDrawableBuilder = TextDrawable.builder().buildRound(letter, color);
        profileImage.setImageDrawable(mDrawableBuilder);
    }

    private void acceptRequest(JSONObject param) {
        loading.show();
        Call<String> call = RetrofitServices.sendTeacherRequest().APIAcceptRequest(param);
        if (call != null) {
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    loading.dismiss();
                    if (response.isSuccessful()) {
                        try {
                            JSONObject result = new JSONObject(response.body());
                            String message = result.getString("message");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                            tvStatus.setText(message);
                            lytConfirmation.setVisibility(View.GONE);
                            cvPaymentStatus.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    loading.dismiss();
                    Log.e("error", t.getMessage());
                }
            });
        }
    }
}
