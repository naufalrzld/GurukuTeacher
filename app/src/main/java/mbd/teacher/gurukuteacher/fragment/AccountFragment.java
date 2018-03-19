package mbd.teacher.gurukuteacher.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import butterknife.BindView;
import butterknife.ButterKnife;
import mbd.teacher.gurukuteacher.R;
import mbd.teacher.gurukuteacher.activity.EditProfileActivity;
import mbd.teacher.gurukuteacher.activity.LoginActivity;
import mbd.teacher.gurukuteacher.model.teacher.Teacher;
import mbd.teacher.gurukuteacher.utils.SessionManager;
import mbd.teacher.gurukuteacher.utils.SharedPreferencesUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {
    @BindView(R.id.profile_image)
    ImageView profileImage;
    @BindView(R.id.tvNama)
    TextView tvNama;
    @BindView(R.id.tvUsername)
    TextView tvUsername;
    @BindView(R.id.tvNoTlp)
    TextView tvNoTlp;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvNoWa)
    TextView tvNoWA;
    @BindView(R.id.tvLineAccount)
    TextView tvLineAccout;
    @BindView(R.id.lytEditProfile)
    LinearLayout lytEditProfile;
    @BindView(R.id.lytLogout)
    LinearLayout lytLogout;

    private SessionManager session;
    private SharedPreferencesUtils sharedPreferencesUtils;
    private ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
    private TextDrawable mDrawableBuilder;
    private Teacher teacher;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.bind(this, v);

        session = new SessionManager(getContext());
        sharedPreferencesUtils = new SharedPreferencesUtils(getContext(), "DataMember");

        lytEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), EditProfileActivity.class);
                startActivity(i);
            }
        });

        lytLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sharedPreferencesUtils.checkIfDataExists("profile")) {
            teacher = sharedPreferencesUtils.getObjectData("profile", Teacher.class);
            String nama = teacher.getFirstName() + " " + teacher.getLastName();
            String username = teacher.getUsername();
            String noTlp = teacher.getNoTlp();
            String email = teacher.getEmail();
            String noWA = teacher.getNoWA();
            String IDLine = teacher.getLineAccount();

            tvNama.setText(nama);
            tvUsername.setText(username);
            tvNoTlp.setText(noTlp);
            tvEmail.setText(email);
            tvNoWA.setText(noWA);
            tvLineAccout.setText(IDLine);

            setProfileImage(nama);
        }
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

    private void logout() {
        session.setLogin(false);
        sharedPreferencesUtils.clearAllData();
        startActivity(new Intent(getContext(), LoginActivity.class));
        getActivity().finish();
    }
}
